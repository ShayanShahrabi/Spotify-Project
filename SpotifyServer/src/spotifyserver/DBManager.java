package spotifyserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    String connectionString = "jdbc:sqlite:Spotify.sqli";

    //--------------------------------------------------------------------------
    //برای درج و اصلاح و حذف از جدول می توان از روتین زیر استفاده کرد
    public String runSQLCommand(String SQLInsert) throws SQLException{

        String result = "";
        
        Connection conn = null;    
        
        try {
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            // 
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQLInsert);
            //
            result = "OK";
            //
            conn.close();
        } 
        catch (Exception e) {
            result = "Error";
        }        
        return result;
    }

    //--------------------------------------------------------------------------
    //برای اجرای دسته ای از دستورات اس کیو ال از این روتین استفاده می شود
    //این روتین یا همه یا هیچ است
    public String runSQLCommandList(List SQLCommandList) throws SQLException{

        String result;
        
        Connection conn;    
        
        try {
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            // 
            Statement stmt = conn.createStatement();
            // Start the transaction
            conn.setAutoCommit(false);
            for(int i = 0; i < SQLCommandList.size(); i++){
                stmt.executeUpdate(SQLCommandList.get(i).toString());
            }
            //
            conn.commit();
            //
            result = "OK";
            //
            conn.close();
        } 
        catch (Exception e) {
            result = "Error";
        }        
        return result;
    }
    
    //--------------------------------------------------------------------------    
    //This routin will return 'Found' or 'Not Foung'
    public String ifRecordExist(String SQLSelect){
        
        String returnValue = "Not Found";
        
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        
        try {
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                returnValue = "Found";
            }            
            rs.close();
            pstmt.close();
            conn.close();
        } 
        catch (Exception e) {
            returnValue = "Not Found";
        }        
        return returnValue;
    }

    //--------------------------------------------------------------------------    
    //Already the password is hashed
    //This routin will retun 'Singer' or 'No Singer' or 'Invalid User'
    public String getUserType(String userID, String password){
        
        String returnValue, SQLSelect;
        
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        
        try {
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            SQLSelect = "Select type From Accounts Where userID = '" + userID + "' And Pass = '" + password + "'";
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                returnValue = rs.getString("type").trim();
            }
            else{
                returnValue = "Invalid User";
            }
            rs.close();
            pstmt.close();
            conn.close();
        } 
        catch (Exception e) {
            returnValue = "Error in getUserType!";
        }        
        return returnValue;
    }
    
    //--------------------------------------------------------------------------
    public void getList(String SQLSelect, String fieldName, List list){
        
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        try {
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(fieldName));
            }            
            rs.close();
            pstmt.close();
            conn.close();
        } 
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }        
        return;
    }
    
    //--------------------------------------------------------------------------
    public void albumSearch(String searchKey, List <Album> albumList){
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        String SQLSelect, userID, albumTitle, singer, genre, releaseDate;
        int likes;
        try {        
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            SQLSelect = "Select * From Album Left join Accounts on Album.userID = Accounts.userID Where albumTitle like '%" + searchKey + "%' Order By albumTitle";
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userID = rs.getString("userID");
                albumTitle = rs.getString("albumTitle");
                singer = rs.getString("fullName");
                genre = rs.getString("genre");
                releaseDate = rs.getString("releasedDate");
                likes = rs.getInt("popularity");
                //به لیست اضافه شود
                Album album = new Album(userID, albumTitle, singer, genre, releaseDate, likes);
                albumList.add(album);                
            }
            rs.close();
            pstmt.close();
            conn.close();
        } 
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }        
    }
    
    //--------------------------------------------------------------------------
    public void singerSearch(String searchKey, List <Singer> singerList){
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        String SQLSelect, userID, fullName;
        try {        
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            SQLSelect = "Select userID, fullName From Accounts Where type = 'Singer' And fullName like '%" + searchKey + "%' Order By fullName";
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userID = rs.getString("userID");
                fullName = rs.getString("fullName");
                //به لیست اضافه شود
                Singer singer = new Singer(userID, fullName);
                singerList.add(singer);                
            }
            rs.close();
            pstmt.close();
            conn.close();
        } 
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }        
    }

    //--------------------------------------------------------------------------
    public void playlistSearch(String searchKey, List <Playlist> playlistList){
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        String SQLSelect, userID, playlistTitle, fullName, description;
        try {        
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            SQLSelect = "Select * From Playlist left join Accounts on Playlist.userID = Accounts.userID Where playlistTitle like '%" + searchKey + "%' Order By fullName";
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userID = rs.getString("userID");
                playlistTitle = rs.getString("playlistTitle");
                fullName = rs.getString("fullName");
                description = rs.getString("description");
                //به لیست اضافه شود
                Playlist playlist = new Playlist(userID, playlistTitle, fullName, description);
                playlistList.add(playlist);                
            }
            rs.close();
            pstmt.close();
            conn.close();
        } 
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }        
    }
    
    //--------------------------------------------------------------------------
    public void songSearch(String searchKey, List <Song> songList){
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        String SQLSelect, userID, songTitle, albumTitle, fullName, genre, duration, releaseDate;
        try {        
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            SQLSelect = "Select * From Song left join Accounts on Song.userID = Accounts.userID Where songTitle like '%" + searchKey + "%' Order By fullName";
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userID = rs.getString("userID");
                songTitle = rs.getString("songTitle");
                albumTitle = rs.getString("albumTitle");
                fullName = rs.getString("fullName");
                genre = rs.getString("genre");
                duration = rs.getString("duration");
                releaseDate = rs.getString("releasedDate");
                //به لیست اضافه شود
                Song song = new Song(userID, songTitle, albumTitle, fullName, genre, duration, releaseDate);
                songList.add(song);                
            }
            rs.close();
            pstmt.close();
            conn.close();
        } 
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }        
    }
    
    //--------------------------------------------------------------------------
    public void getProfile(Profile profile){
        String userID = profile.getUserID();
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        String SQLSelect, playlists, albums;
        List <String> listPlaylist = new ArrayList<>();
        List <String> listAlbum = new ArrayList<>();
        try {        
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            SQLSelect = "Select * From Accounts Where userID = '" + userID + "'";
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                profile.setFullName(rs.getString("fullName"));
                profile.setRole(rs.getString("type"));
                profile.setPhoneNumber(rs.getString("PhoneNumber"));
                profile.setEmail(rs.getString("email"));
                profile.setAddress(rs.getString("address"));
            }
            rs.close();
            //لیست پلی لیست این کاربر پر شود
            SQLSelect = "Select playlistTitle || ' / ' || fullName || ' / ' || songTitle As song from PlaylistSong left join Accounts On playlistSong.userID = Accounts.userID Where PlaylistSong.userID = '" + userID + "'";
            getList(SQLSelect,"song", listPlaylist);
            playlists = "";
            for(String song : listPlaylist){
                if(!playlists.equals("")){
                    playlists += ", ";
                }
                playlists += song;
            }
            profile.setPlaylists(playlists + ".");
            //لیست آلبوم این کاربر پر شود
            SQLSelect = "Select albumTitle || ' / ' || songTitle As song from Song Where userID = '" + userID + "'";
            getList(SQLSelect,"song", listAlbum);
            albums = "";
            for(String song : listAlbum){
                if(!albums.equals("")){
                    albums += ", ";
                }
                albums += song;
            }
            profile.setAlbums(albums + ".");
            pstmt.close();
            conn.close();
        } 
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }        
    }
    
    //--------------------------------------------------------------------------
    public void get8RecentAlbums(List <Album> albumList){
        Connection conn = null;    
        PreparedStatement pstmt = null;
        ResultSet rs = null;  
        String SQLSelect, userID, albumTitle, singer, genre, releaseDate;
        int likes;
        try {        
            // Connect to the SQLite database
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(connectionString);
            SQLSelect = "Select * From Album Left join Accounts on Album.userID = Accounts.userID Order By releasedDate Desc Limit 8";
            pstmt = conn.prepareStatement(SQLSelect);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                userID = rs.getString("userID");
                albumTitle = rs.getString("albumTitle");
                singer = rs.getString("fullName");
                genre = rs.getString("genre");
                releaseDate = rs.getString("releasedDate");
                likes = rs.getInt("popularity");
                //به لیست اضافه شود
                Album album = new Album(userID, albumTitle, singer, genre, releaseDate, likes);
                albumList.add(album);                
            }
            rs.close();
            pstmt.close();
            conn.close();
        } 
        catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }        
    }    
}

    
