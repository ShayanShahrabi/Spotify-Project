package spotifyserver;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.Formatter;
import java.util.Scanner;
import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SpotifyServer {
    final int PORTNUMBER = 4444;

    //------------------------------------------------------------
    SpotifyServer(){
    }
    //------------------------------------------------------------
    private String readFromSocket(Scanner scannerSocket){
        String result = "";
        try{
            if(scannerSocket.hasNext()){
                result = scannerSocket.nextLine();
            }        
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }     
        return result;
    }
    
    //------------------------------------------------------------
    private void write2Socket(Socket socket, String message){
        try{
            Formatter formatterSocket = new Formatter(socket.getOutputStream());
            formatterSocket.format(message + "\n");
            formatterSocket.flush();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    //------------------------------------------------------------
    //از این روتین برای ارسال فایل روی پورت استفاده می شود
    private void sendFile2Port(Socket socket, String fileName){
        
        try {
            File fileToSend = new File(fileName);
            
            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();

            // Write the file to the socket's output stream
            byte[] buffer = new byte[204800];         //Instead of 4096
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            // Close the resources
            bufferedInputStream.close();
            outputStream.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }        
    }
    
    //------------------------------------------------------------
    //از این روتین برای ارسال فایل روی پورت استفاده می شود
    private void sendFile2PortUsingOutputStream(OutputStream outputStream, String fileName){
        
        try {
            File fileToSend = new File(fileName);
            
            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            // Write the file to the socket's output stream
            byte[] buffer = new byte[204800];         //Instead of 4096
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            // Close the resources
            bufferedInputStream.close();
        } catch (IOException ex) {
            System.out.println("Exception in sendFile2PortUsingOutputStream: " + ex.getMessage());
        }        
    }
    
    //------------------------------------------------------------
    //یک فایل جدید ایجاد می کند و در مسیر مشخص شده ذخیره می شود
    private void getFileFromPort(Socket socket, String fileName){

        try{
            //حالا با یک پروتکلی باید فایل را از سوکت بخوانیم
            try {
                // Get the input stream of the socket
                InputStream inputStream = socket.getInputStream();

                // Create a file to save the received data
                fileName = System.getProperty("user.dir") + "\\Media\\" + fileName;
                
                File receivedFile = new File(fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);                
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                // Read the data from the socket's input stream and write it to the file
                byte[] buffer = new byte[204800];         //Instead of 4096
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                }

                // Close the resources
                bufferedOutputStream.close();
                inputStream.close();
                //
                System.out.println("File " + fileName + " received successfully.\n");
            } catch (IOException e) {
                e.printStackTrace();
            }            
            socket.close();
        }
        catch (Exception ex){
            System.out.println("خطا در برقراری ارتباط با سرور");
        }
    }    
    
    //------------------------------------------------------------
    public static String hash(String input) {
        String result;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            
            // Convert the byte array to hexadecimal representation
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }            
            result = hexString.toString();
        } catch (Exception ex) {
            result = "Error Hashing!";
        }
        //
        return result;
    }

    //------------------------------------------------------------
    private void createAccountHandler(Scanner scannerSocket, Socket socket){
        String userID, password, type, fullName, email, address, phoneNumber;
        String SQLCommand, returnValue = "Error";
        System.out.println("Request: Create Account\n");
        //شناسه و رمز عبور و تاریخ تولد از سوکت خواند شود
        userID = readFromSocket(scannerSocket);
        password = readFromSocket(scannerSocket);
        type = readFromSocket(scannerSocket);
        fullName = readFromSocket(scannerSocket);
        email = readFromSocket(scannerSocket);
        address = readFromSocket(scannerSocket);
        phoneNumber = readFromSocket(scannerSocket);
        //
        String hashedPassword = hash(password.trim());
        //
        try{
            SQLCommand = "Insert Into Accounts (userID, pass, type, fullName, email, address, phoneNumber, likes) Values( '" + userID + "', '" + hashedPassword + "', '" + type + "', '" + fullName + "', '" +
                         email + "', '" + address + "', '" + phoneNumber + "', 0)";
            DBManager dbManager = new DBManager();
            returnValue = dbManager.runSQLCommand(SQLCommand);       //Will return "Error" or "OK"
        }
        catch (Exception ex){            
        }
        write2Socket(socket, returnValue);
    }

    //------------------------------------------------------------
    private void loginHandler(Scanner scannerSocket, Socket socket){
        String userID, password, hashedPassword;
        String returnValue;
        String SQLCommand;
        
        System.out.println("Request: Login\n");
        //شناسه و رمز عبور و تاریخ تولد از سوکت خواند شود
        userID = readFromSocket(scannerSocket);
        password = readFromSocket(scannerSocket);
        //
        hashedPassword = hash(password.trim());
        DBManager dbManager = new DBManager();

        returnValue = dbManager.getUserType(userID, hashedPassword);
        
        write2Socket(socket, returnValue);
        
    }
        
    //------------------------------------------------------------
    private void addAlbumHandler(Scanner scannerSocket, Socket socket){
        String userID, albumTitle, genre, releasedDate;
        String albumCoverFileName;
        String SQLCommand, returnValue = "Error";
        System.out.println("Request: Add Album\n");
        //خواندن اطلاعات از ورودی
        userID = readFromSocket(scannerSocket);
        albumTitle = readFromSocket(scannerSocket);
        genre = readFromSocket(scannerSocket);
        releasedDate = readFromSocket(scannerSocket);
        //
        try{
            SQLCommand = "Insert Into Album (userID, albumTitle, genre, releasedDate, popularity) Values( '" + userID + "', '" + albumTitle + "', '" + genre + "', '" + releasedDate + "', 0)";
            DBManager dbManager = new DBManager();
            returnValue = dbManager.runSQLCommand(SQLCommand);       //Will return "Error" or "OK"
        }
        catch (Exception ex){            
        }
        write2Socket(socket, returnValue);
        if(returnValue.equals("OK")){             //فایل روکش آلبوم دریافت شود
            albumCoverFileName = "Album-" + userID + "-" + albumTitle + ".jpg";
            getFileFromPort(socket, albumCoverFileName);
        }
    }
    
    //------------------------------------------------------------
    private void getListHandler(Scanner scannerSocket, Socket socket){
        String SQLSelect, selectField;
        List<String> list = new ArrayList<String>();
        //
        System.out.println("Request: Get List\n");
        //خواندن اطلاعات از ورودی
        SQLSelect = readFromSocket(scannerSocket);
        selectField = readFromSocket(scannerSocket);
        //
        try{
            //اطلاعات از پایگاه داده خوانده شود
            DBManager dbManager = new DBManager();
            dbManager.getList(SQLSelect, selectField, list);  //اینجا لیست پر می شود
            //حالا لیست روی سوکت نوشته شود
            for(int i = 0; i < list.size(); i++){
                write2Socket(socket, list.get(i));
            }
        }
        catch (Exception ex){            
        }
        write2Socket(socket, "Finished");
    }
    
    //------------------------------------------------------------
    private void addSongHandler(Scanner scannerSocket, Socket socket){
        String userID, songTitle, albumTitle, genre, duration, releasedDate;
        String songFileName;
        String SQLCommand, returnValue = "Error";
        System.out.println("Request: Add Song\n");
        //خواندن اطلاعات از ورودی
        userID = readFromSocket(scannerSocket);
        songTitle = readFromSocket(scannerSocket);
        albumTitle = readFromSocket(scannerSocket);
        genre = readFromSocket(scannerSocket);
        duration  = readFromSocket(scannerSocket);
        releasedDate = readFromSocket(scannerSocket);
        //
        try{
            SQLCommand = "Insert Into Song (userID, SongTitle, albumTitle, genre, duration, releasedDate, popularity) Values( '" + userID + "', '" + songTitle + "', '" + albumTitle + "', '" + genre + "', '" + duration + "', '" + releasedDate + "', 0)";
            DBManager dbManager = new DBManager();
            returnValue = dbManager.runSQLCommand(SQLCommand);       //Will return "Error" or "OK"
        }
        catch (Exception ex){            
        }
        write2Socket(socket, returnValue);
        if(returnValue.equals("OK")){             //فایل موسیقی دریافت شود
            songFileName = "Song-" + userID + "-" + songTitle + ".MP3";
            getFileFromPort(socket, songFileName);
        }
    }

    //------------------------------------------------------------
    private void addPlaylistHandler(Scanner scannerSocket, Socket socket){                
        String userID, playlistTitle, description, artistID, songTitle;
        String SQLResult, returnValue;
        List <String> SQLCommandList = new ArrayList<>();
        int index;
        System.out.println("Request: Add Playlist\n");
        //
        DBManager dbManager;
        //خواندن اطلاعات از ورودی
        userID = readFromSocket(scannerSocket);
        playlistTitle = readFromSocket(scannerSocket);
        description = readFromSocket(scannerSocket);
        //
        try{
            SQLCommandList.add("Insert Into Playlist (userID, playlistTitle, description, popularity) Values( '" + userID + "', '" + playlistTitle + "', '" + description + "', 0)");
            //حالا موسیقی های مربوط به این پلی لیست اضافه شود
            //یکی یکی از پورت خواند شود
            returnValue = readFromSocket(scannerSocket);
            while(!returnValue.equals("Finished")){
                index = returnValue.indexOf(" / ");
                artistID = returnValue.substring(0, index);
                songTitle = returnValue.substring(index + 3);
                SQLCommandList.add("Insert Into PlaylistSong (userID, playlistTitle, artistID, songTitle) Values( '" + userID + "', '" + playlistTitle + "', '" + artistID + "', '" + songTitle + "')");
                returnValue = readFromSocket(scannerSocket);
            }
            dbManager = new DBManager();
            SQLResult = dbManager.runSQLCommandList(SQLCommandList);       //Will return "Error" or "OK"
        }
        catch (Exception ex){            
            SQLResult = "Error";
        }
        //موفقیت یا عدم موفقیت در پورت نوشته شود
        write2Socket(socket, SQLResult);
    }

    //------------------------------------------------------------
    private void searchAlbumHandler(Scanner scannerSocket, Socket socket){                
        String searchKey, fileName;
        String userID, albumTitle, SQLSelect, songs;
        List <Album> albumList;
        List <String> songList;
        System.out.println("Request: Search on Album\n");
        //خواندن اطلاعات از ورودی
        searchKey = readFromSocket(scannerSocket);
        //
        try{
            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();
            //
            DBManager dbManager = new DBManager();           
            albumList = new ArrayList<>();
            //درخواست دریافت اطلاعات در پایگاه داده
            dbManager.albumSearch(searchKey, albumList);
            //ارسال خروجی روی پورت
            for (Album album : albumList) {
                //ارسال آماده باش که سطر شروع شد
                write2Socket(socket, "Row Started");
                //قبل و بعد از ارسال فایل تصویر باید از سوکت بخوانیم تا برنامه درست کار کند
                readFromSocket(scannerSocket);                      //Message: "Get Image"                
                //عکس کاور آلبوم را ابتدا می فرستیم
                fileName = System.getProperty("user.dir") + "\\Media\\Album-" + album.getUserID() + "-" + album.getAlbumTitle() + ".jpg";
                sendFile2PortUsingOutputStream(outputStream, fileName);
                //حالا تاییدیه می گیرد که تصویر خوانده شده است این لازم است زیرا اگر درجا شروع به نوشتن باقی اطلاعات کنیم برنامه درست کار نمی کند
                readFromSocket(scannerSocket);                      //Message: "Image Received"
                //حالا فیلدهای آلبوم را ارسال می کنیم
                albumTitle = album.getAlbumTitle();
                write2Socket(socket, "Title: " + albumTitle);
                write2Socket(socket, "Singer:" + album.getSinger());
                write2Socket(socket, "Genre: " + album.getGenre());
                write2Socket(socket, "Release Date: " + album.getReleaseDate());
                userID = album.getUserID();
                //حالا آهنگها را اولا از پایگاه داده می خوانیم و بعد ارسال می کنیم
                songList = new ArrayList<>();
                SQLSelect = "Select SongTitle From Song Where userID = '" + userID + "' And albumTitle = '" + albumTitle + "' Order By SongTitle";
                dbManager.getList(SQLSelect, "SongTitle", songList);
                songs = "Songs: ";
                for(String song : songList){
                    if(!songs.equals("Songs: ")){
                        songs += ", ";
                    }
                    songs += song;
                }                
                //حالا در یک خط کل موسیقی ها ارسال شود
                write2Socket(socket, songs);           
            }
            //
            write2Socket(socket, "Finished");
            //
            outputStream.close();
        }
        catch (Exception ex){            
        }
    }
    
    //------------------------------------------------------------
    private void searchSingerHandler(Scanner scannerSocket, Socket socket){                
        String searchKey, fileName;
        String userID, SQLSelect, songs;
        List <Singer> singerList;
        List <String> songList;
        System.out.println("Request: Search on Singer\n");
        //خواندن اطلاعات از ورودی
        searchKey = readFromSocket(scannerSocket);
        //
        try{
            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();
            //
            DBManager dbManager = new DBManager();           
            singerList = new ArrayList<>();
            //درخواست دریافت اطلاعات در پایگاه داده
            dbManager.singerSearch(searchKey, singerList);
            //ارسال خروجی روی پورت
            for (Singer singer : singerList) {
                //ارسال آماده باش که سطر شروع شد
                write2Socket(socket, "Row Started");
                //قبل و بعد از ارسال فایل تصویر باید از سوکت بخوانیم تا برنامه درست کار کند
                readFromSocket(scannerSocket);                      //Message: "Get Image"                
                //عکس کاور آلبوم را ابتدا می فرستیم
                fileName = System.getProperty("user.dir") + "\\Media\\User-" + singer.getUserID() + ".jpg";
                sendFile2PortUsingOutputStream(outputStream, fileName);
                //حالا تاییدیه می گیرد که تصویر خوانده شده است این لازم است زیرا اگر درجا شروع به نوشتن باقی اطلاعات کنیم برنامه درست کار نمی کند
                readFromSocket(scannerSocket);                      //Message: "Image Received"
                //حالا فیلدهای آلبوم را ارسال می کنیم
                write2Socket(socket, singer.getUserID());           //شناسه خواننده ارسال شود
                write2Socket(socket, "Singer: " + singer.getFullName());
                write2Socket(socket, "likes: " + singer.getLikes());
                //
                userID = singer.getUserID();
                //حالا آهنگها را اولا از پایگاه داده می خوانیم و بعد ارسال می کنیم
                songList = new ArrayList<>();
                SQLSelect = "Select albumTitle || ' / ' || SongTitle As song From Song Where userID = '" + userID + "' Order By albumTitle, SongTitle";
                dbManager.getList(SQLSelect, "song", songList);
                songs = "Songs: ";
                for(String song : songList){
                    if(!songs.equals("Songs: ")){
                        songs += ", ";
                    }
                    songs += song;
                }                
                //حالا در یک خط کل موسیقی ها ارسال شود
                write2Socket(socket, songs);           
            }
            //
            write2Socket(socket, "Finished");
            //
            outputStream.close();
        }
        catch (Exception ex){            
        }
    }    
    
    //------------------------------------------------------------
    private void searchPlaylistHandler(Scanner scannerSocket, Socket socket){                
        String searchKey, SQLSelect, userID, playlistTitle, songs, select1;
        List <Playlist> playlistList;
        List <String> songList;
        System.out.println("Request: Search on Playlist\n");
        //خواندن اطلاعات از ورودی
        searchKey = readFromSocket(scannerSocket);
        //
        try{
            // Get the output stream of the socket
            //
            DBManager dbManager = new DBManager();           
            playlistList = new ArrayList<>();
            //درخواست دریافت اطلاعات در پایگاه داده
            dbManager.playlistSearch(searchKey, playlistList);
            //ارسال خروجی روی پورت
            for (Playlist playlist : playlistList) {
                //ارسال آماده باش که سطر شروع شد
                write2Socket(socket, "Row Started");
                //حالا فیلدهای آلبوم را ارسال می کنیم
                userID = playlist.getUserID();
                playlistTitle = playlist.getPlaylistTitle();
                write2Socket(socket, "Title: " + playlistTitle);
                write2Socket(socket, "Creator: " + playlist.getFullName());
                write2Socket(socket, "Description: " + playlist.getDescription());
                //حالا آهنگ های پلی لیست را اولا از پایگاه داده می خوانیم و بعد ارسال می کنیم
                songList = new ArrayList<>();
                select1 = "(Select artistID, songTitle From Playlist Left Join PlaylistSong on Playlist.userID = PlaylistSong.userID And Playlist.playlistTitle = PlaylistSong.playlistTitle Where Playlist.userID = '" + userID + "' And Playlist.playlistTitle = '" + playlistTitle + "') As Select1";
                SQLSelect = "Select fullName || ' / ' || songTitle As song From " + select1 + " Left Join Accounts On Select1.artistID = Accounts.userID Order By fullName, SongTitle";
                dbManager.getList(SQLSelect, "song", songList);
                songs = "Songs: ";
                for(String song : songList){
                    if(!songs.equals("Songs: ")){
                        songs += ", ";
                    }
                    songs += song;
                }                
                //حالا در یک خط کل موسیقی ها ارسال شود
                write2Socket(socket, songs);           
            }
            //
            write2Socket(socket, "Finished");
        }
        catch (Exception ex){            
        }
    }    
    
    //------------------------------------------------------------
    private void searchSongHandler(Scanner scannerSocket, Socket socket){                
        String searchKey;
        List <Song> songList;
        System.out.println("Request: Search on Song\n");
        //خواندن اطلاعات از ورودی
        searchKey = readFromSocket(scannerSocket);
        //
        try{
            // Get the output stream of the socket
            //
            DBManager dbManager = new DBManager();           
            songList = new ArrayList<>();
            //درخواست دریافت اطلاعات در پایگاه داده
            dbManager.songSearch(searchKey, songList);
            //ارسال خروجی روی پورت
            for (Song song : songList) {
                //ارسال آماده باش که سطر شروع شد
                write2Socket(socket, "Row Started");
                //حالا فیلدهای آلبوم را ارسال می کنیم
                write2Socket(socket, "UserID: " + song.getUserID());
                write2Socket(socket, "Song: " + song.getSongTitle());
                write2Socket(socket, "Album: " + song.getAlbumTitle());
                write2Socket(socket, "Singer: " + song.getSinger());
                write2Socket(socket, "Genre: " + song.getGenre());
                write2Socket(socket, "Duration: " + song.getDuration());
                write2Socket(socket, "Release Date: " + song.getReleaseDate());
            }
            //
            write2Socket(socket, "Finished");
        }
        catch (Exception ex){            
        }
    }    
    
    //------------------------------------------------------------
    private void myProfileHandler(Scanner scannerSocket, Socket socket){                
        String userID, message, fileName;
        System.out.println("Request: Profile Request\n");
        //خواندن اطلاعات از ورودی
        userID = readFromSocket(scannerSocket);
        //
        try{
            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();
            //درخواست دریافت اطلاعات در پایگاه داده
            DBManager dbManager = new DBManager();   
            Profile profile = new Profile(userID, "", "", "", "", "", "", "");
            dbManager.getProfile(profile);
            //حالا فیلدهای آلبوم را ارسال می کنیم
            write2Socket(socket, profile.getFullName());
            write2Socket(socket, profile.getRole());
            write2Socket(socket, profile.getPhoneNumber());
            write2Socket(socket, profile.getEmail());
            write2Socket(socket, profile.getAddress());
            write2Socket(socket, profile.getPlaylists());
            write2Socket(socket, profile.getAlbums());
            //حالا فایل عکس اگر هست ارسال شود
            //اول کلاینت سوال می کند عکس داری؟
            message = readFromSocket(scannerSocket);       //The massage is "Have Photo?"
            //حالا بررسی می کنیم ببینیم فایل موجود هست با خیر
            fileName = System.getProperty("user.dir") + "\\Media\\User-" + userID + ".jpg";
            File file = new File(fileName);
            if (file.exists()){
                write2Socket(socket, "Yes");
                message = readFromSocket(scannerSocket);       //The massage is "Please send the image file"
                //حالا فایل تصویر ارسال شود
                sendFile2PortUsingOutputStream(outputStream, fileName);
                //حالا تاییدیه می گیرد که تصویر خوانده شده است این لازم است زیرا اگر درجا شروع به نوشتن باقی اطلاعات کنیم برنامه درست کار نمی کند
                message = readFromSocket(scannerSocket);                      //Message: "Image Received"
            }
            else{
                write2Socket(socket, "No");
            }
        }
        catch (Exception ex){            
            System.out.println("Exception in myProfileHandler : " + ex.getMessage());
        }
    }
    
    //------------------------------------------------------------
    private void userImageHandler(Scanner scannerSocket, Socket socket){                
        String userID;
        String userPhotoFileName;
        System.out.println("Request: Get User Image\n");
        //خواندن اطلاعات از ورودی
        userID = readFromSocket(scannerSocket);
        //خط بعد صرفا برای این است که بین دو نوشتن یک خواندن انجام شود
        write2Socket(socket, "Send File");
        //خواندن فایل عکس کاربر از سوکت
        userPhotoFileName = "User-" + userID + ".jpg";
        getFileFromPort(socket, userPhotoFileName);
    }
    
    //------------------------------------------------------------
    private void homeHandler(Scanner scannerSocket, Socket socket){                
        String fileName, message;
        String albumTitle;
        List <Album> albumList;
        System.out.println("Request: Home Request\n");
        //
        try{
            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();
            //
            DBManager dbManager = new DBManager();           
            albumList = new ArrayList<>();
            //درخواست دریافت اطلاعات در پایگاه داده
            dbManager.get8RecentAlbums(albumList);
            //ارسال خروجی روی پورت
            for (Album album : albumList) {
                //ارسال آماده باش که سطر شروع شد
                write2Socket(socket, "Row Started");
                //قبل و بعد از ارسال فایل تصویر باید از سوکت بخوانیم تا برنامه درست کار کند
                readFromSocket(scannerSocket);                      //Message: "Have Photo?"
                //حالا بررسی می کنیم ببینیم فایل موجود هست با خیر
                fileName = System.getProperty("user.dir") + "\\Media\\Album-" + album.getUserID() + "-" + album.getAlbumTitle() + ".jpg";
                File file = new File(fileName);
                if (file.exists()){
                    write2Socket(socket, "Yes");
                    message = readFromSocket(scannerSocket);       //The massage is "Please send the image file"
                    //حالا فایل تصویر ارسال شود
                    sendFile2PortUsingOutputStream(outputStream, fileName);
                    //حالا تاییدیه می گیرد که تصویر خوانده شده است این لازم است زیرا اگر درجا شروع به نوشتن باقی اطلاعات کنیم برنامه درست کار نمی کند
                    message = readFromSocket(scannerSocket);                      //Message: "Image Received"
                }
                else{
                    write2Socket(socket, "No");
                }
                //حالا فیلدهای آلبوم را ارسال می کنیم
                albumTitle = album.getAlbumTitle();
                write2Socket(socket, "Title: " + albumTitle);
                write2Socket(socket, "Singer:" + album.getSinger());
                write2Socket(socket, "Genre: " + album.getGenre());
                write2Socket(socket, "Release Date: " + album.getReleaseDate());
            }
            //
            write2Socket(socket, "Finished");
            //
            outputStream.close();
        }
        catch (Exception ex){            
        }
    }
     
    //------------------------------------------------------------
    private void getSongHandler(Scanner scannerSocket, Socket socket){                
        String userID, songTitle, fileName, message;
        String songFileName;
        System.out.println("Request: Get Song\n");
        try{
            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();
            //خواندن اطلاعات از ورودی
            userID = readFromSocket(scannerSocket);
            songTitle = readFromSocket(scannerSocket);
            //خط بعد صرفا برای این است که بین دو نوشتن یک خواندن انجام شود
            message = readFromSocket(scannerSocket);                            //message = "Have MP3?"
            //خواندن فایل موسیقی کاربر از سوکت
            songFileName = "Song-" + userID + "-" + songTitle + ".MP3";
            //چک شود آیا فایل هست یا خیر
            fileName = System.getProperty("user.dir") + "\\Media\\" + songFileName;
            File file = new File(fileName);
            if (file.exists()){
                write2Socket(socket, "Yes");
                message = readFromSocket(scannerSocket);       //The massage is "Please send the song file"
                //حالا فایل تصویر ارسال شود
                sendFile2PortUsingOutputStream(outputStream, fileName);
                //حالا تاییدیه می گیرد که تصویر خوانده شده است این لازم است زیرا اگر درجا شروع به نوشتن باقی اطلاعات کنیم برنامه درست کار نمی کند
                message = readFromSocket(scannerSocket);                      //Message: "File Received"
            }
            else{
                write2Socket(socket, "No");
            }
        }
        catch (Exception ex){
            System.out.println("Exception in getSongHandler : " + ex.getMessage());
        }
    }

    //------------------------------------------------------------
    //اگر قبلا کاربر خواننده را لایک کرده باشد آلردی لایکد و در غیراینصورت اکی در پورت نوشته می شود
    private void likeSingerHandler(Scanner scannerSocket, Socket socket){                
        String userID, artistID;
        String SQLSelect, result;
        System.out.println("Request: Like Singer\n");
        try{
            //خواندن اطلاعات از ورودی
            userID = readFromSocket(scannerSocket);
            artistID = readFromSocket(scannerSocket);
            //
            DBManager dbManager = new DBManager();           
            //




            SQLSelect = "Insert Into LikeArtist (userID, artistID) Values( '" + userID + "', '" + artistID + "')";
            result = dbManager.runSQLCommand(SQLSelect);
            //            
            if (result.equals("Error")){
                write2Socket(socket, "Already Liked!");
            }
            else{
                write2Socket(socket, "OK");
                //حالا یکی به لایک های این خواننده اضافه شود
                SQLSelect = "Update Accounts Set likes = likes + 1 Where userID = '" + artistID + "'";
                dbManager.runSQLCommand(SQLSelect);
            }
        }
        catch (Exception ex){
            System.out.println("Exception in likeSingerHandler : " + ex.getMessage());
        }
    }
    
    //------------------------------------------------------------
    private void distibuteJobs(){
        Socket socket;
        String serviceName;
        try{
            ServerSocket serverSocket = new ServerSocket(PORTNUMBER);
            while(true) {
                System.out.println("Listening ...\n");
                //بعد از خط زیر اجرا متوقف می شود تا ورودی بیاید
                socket = serverSocket.accept();
                Scanner scannerSocket = new Scanner(socket.getInputStream());        
                //خواندن از پورت
                serviceName = readFromSocket(scannerSocket);
                //
                ThreadParameters tp = new ThreadParameters(scannerSocket, socket);                
                //
                switch(serviceName){
                    case "_CreateAccount":
                        // createAccountHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread createAccountThread = new Thread(() -> createAccountHandler(tp.getScannerSocket(), tp.getSocket()));
                        createAccountThread.start();
                        //
                        System.out.println("Create Account Thread ID: " + createAccountThread.getId() + "\n");
                        //
                        break;
                    case "_Login":
                        // loginHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread loginThread = new Thread(() -> loginHandler(tp.getScannerSocket(), tp.getSocket()));
                        loginThread.start();
                        //
                        System.out.println("Login Thread ID: " + loginThread.getId() + "\n");
                        //
                        break;
                    case "_Home":
                        // homeHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread homeThread = new Thread(() -> homeHandler(tp.getScannerSocket(), tp.getSocket()));
                        homeThread.start();
                        //
                        System.out.println("Home Thread ID: " + homeThread.getId() + "\n");
                        //
                        break;
                    case "_AddAlbum":
                        // addAlbumHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread addAlbumThread = new Thread(() -> addAlbumHandler(tp.getScannerSocket(), tp.getSocket()));
                        addAlbumThread.start();
                        //
                        System.out.println("Add Album Thread ID: " + addAlbumThread.getId() + "\n");
                        //
                        break;
                    case "_AddSong":
                        // addSongHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread addSongThread = new Thread(() -> addSongHandler(tp.getScannerSocket(), tp.getSocket()));
                        addSongThread.start();
                        //
                        System.out.println("Add Song Thread ID: " + addSongThread.getId() + "\n");
                        //
                        break;
                    case "_AddPlaylist":
                        // addPlaylistHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread addPlaylistThread = new Thread(() -> addPlaylistHandler(tp.getScannerSocket(), tp.getSocket()));
                        addPlaylistThread.start();
                        //
                        System.out.println("Add Playlist Thread ID: " + addPlaylistThread.getId() + "\n");
                        //
                        break;
                    case "_SearchAlbum":
                        // searchAlbumHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread searchAlbumThread = new Thread(() -> searchAlbumHandler(tp.getScannerSocket(), tp.getSocket()));
                        searchAlbumThread.start();
                        //
                        System.out.println("Search Album Thread ID: " + searchAlbumThread.getId() + "\n");
                        //
                        break;
                    case "_SearchSinger":
                        // searchSingerHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread searchSingerThread = new Thread(() -> searchSingerHandler(tp.getScannerSocket(), tp.getSocket()));
                        searchSingerThread.start();
                        //
                        System.out.println("Search Singer Thread ID: " + searchSingerThread.getId() + "\n");
                        //
                        break;
                    case "_SearchPlaylist":
                        // searchPlaylistHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread searchPlaylistThread = new Thread(() -> searchPlaylistHandler(tp.getScannerSocket(), tp.getSocket()));
                        searchPlaylistThread.start();
                        //
                        System.out.println("Search Playlist Thread ID: " + searchPlaylistThread.getId() + "\n");
                        //
                        break;
                    case "_SearchSong":
                        // searchSongHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread searchSongThread = new Thread(() -> searchSongHandler(tp.getScannerSocket(), tp.getSocket()));
                        searchSongThread.start();
                        //
                        System.out.println("Search Song Thread ID: " + searchSongThread.getId() + "\n");
                        //
                        break;
                    case "_MyProfile":
                        // myProfileHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread myProfileThread = new Thread(() -> myProfileHandler(tp.getScannerSocket(), tp.getSocket()));
                        myProfileThread.start();
                        //
                        System.out.println("My Profile Thread ID: " + myProfileThread.getId() + "\n");
                        //                        
                        break;
                    case "_UserImage":
                        // userImageHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread userImageThread = new Thread(() -> userImageHandler(tp.getScannerSocket(), tp.getSocket()));
                        userImageThread.start();
                        //
                        System.out.println("User Image Thread ID: " + userImageThread.getId() + "\n");
                        //
                        break;
                    case "_GetList":
                        // getListHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread getListThread = new Thread(() -> getListHandler(tp.getScannerSocket(), tp.getSocket()));
                        getListThread.start();
                        //
                        System.out.println("Get List Thread ID: " + getListThread.getId() + "\n");
                        //
                        break;
                    case "_GetSong":
                        // getSongHandler(scannerSocket, socket);
                        //به دلیل برنامه نویسی موازی خط بالا کامنت شد
                        Thread getSongThread = new Thread(() -> getSongHandler(tp.getScannerSocket(), tp.getSocket()));
                        getSongThread.start();
                        //
                        System.out.println("Get List Thread ID: " + getSongThread.getId() + "\n");
                        //
                        break;
                    case "_LikeSinger":
                        Thread likeSingerThread = new Thread(() -> likeSingerHandler(tp.getScannerSocket(), tp.getSocket()));
                        likeSingerThread.start();
                        //
                        System.out.println("Get List Thread ID: " + likeSingerThread.getId() + "\n");
                        break;
                    case "_Exit":
                        System.out.println("Request: Exit\n");
                        break;
                }
                //
                if(serviceName.equals("_Exit"))
                    break;
            }
            socket.close();
            serverSocket.close();
        }
        catch (Exception ex)
        {
            System.out.println("Server Exception: " + ex.getMessage());
        }
    }
    
    //------------------------------------------------------------
    public static void main(String[] args) {
        SpotifyServer server = new SpotifyServer();
        server.distibuteJobs();
    }    
}
