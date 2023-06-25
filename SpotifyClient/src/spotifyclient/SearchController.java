package spotifyclient;

import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URLEncoder;

public class SearchController implements Initializable {

    private MediaPlayer mediaPlayer;
    
    @FXML
    TextField tfSearch;

    @FXML
    ComboBox cbSearchGroup;
    
    @FXML
    GridPane gpReport;
    
    @FXML
    ImageView ivStop;
    
    //--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbSearchGroup.getItems().addAll("Album", "Singer", "Playlist", "Song");
        cbSearchGroup.getSelectionModel().select(0);
        //عکس استاپ موسیقی را نشان دهد
        String fileName = "\\Files\\" + "Stop" + ".jpg";
        fileName = "file:///" + System.getProperty("user.dir") + fileName;
        Image image = new Image(fileName);
        ivStop.setImage(image);
        ivStop.setVisible(false);
    }    

    //--------------------------------------------------------------------------    
    private void searchOnAlbum(String searchKey) {
        
        String cellInfo;
        int row;
        String returnValue, fileName;
        //جدول خروجی آماده شود
        //ابتدا محتویات پاک شود
        gpReport.getChildren().clear();
        gpReport.getRowConstraints().clear();        
        // Set the horizontal gap
        gpReport.setHgap(10.0);
        // Set the vertical gap
        gpReport.setVgap(10.0);
        // Set the width for the first column
         ColumnConstraints firstColumnConstraints = gpReport.getColumnConstraints().get(0);
         firstColumnConstraints.setPrefWidth(220);
         // Set the width for the second column
         ColumnConstraints secondColumnConstraints = gpReport.getColumnConstraints().get(1);
         secondColumnConstraints.setPrefWidth(480);
        try {
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            //خط زیر بسیار ضروری است زیرا زمان خواند فایل از سوکت دستور خواندن بلاکینک است و اگر خط زیر نباشد خواندن از سوکت در آخرین اجرای حلقه به 
            //دلیل اینکه چیزی برای خواند نیست متوقف می شود
            socket.setSoTimeout(200);
            // Get the input stream of the socket
            InputStream inputStream = socket.getInputStream();
            //
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_SearchAlbum");
            Util.write2Socket(socket, searchKey);
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            row = 0;
            while(!returnValue.equals("Finished")){
                //قبل و بعد از ارسال فایل تصویر پیغام بفرست تا درست کار کند
                Util.write2Socket(socket, "Get Image");                
                //عکس کاور آلبوم در خانه اول ذخیره گردد
                fileName = "Download\\" + "temp" + row + ".jpg";
                //فایل تصویر کاور آلبوم گرفته شود و در فولدر دانلود ذخیره گردد 
                Util.getFileFromPortUsingInputStream(fileName, inputStream);
                //حالا به سرور بگو فایل تصویر را گرفتم این لازم است زیرا اگر دوباره بلافاصله شروع به خواندن کنیم نمی دانم چرا اطلاعات خواند نمی شود
                Util.write2Socket(socket, "Image Received");
                // Create a new ImageView for each row
                Image image = new Image("file:///" + System.getProperty("user.dir") + "\\" + fileName);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                // Add padding to the first cell
                GridPane.setMargin(imageView, new Insets(10, 0, 0, 10)); // Adds 10 pixels of padding
                // Add the image to the first cell of the current row
                gpReport.add(imageView, 0, row);
                // اطلاعات آلبوم در سلول دوم از سطر جاری درج شود
                Text cellText = new Text();
                cellText.setFill(Paint.valueOf("1db954"));
                cellInfo = "";
                for(int i = 1; i < 6; i++)
                {
                    returnValue = Util.readFromSocket(scannerSocket);
                    //
                    cellInfo += returnValue + "\n";
                }
                cellText.setText(cellInfo);
                // Add the text to the second cell of the current row
                cellText.setWrappingWidth(470);
                cellText.setTextAlignment(TextAlignment.LEFT);
                gpReport.add(cellText, 1, row);
                // Set the height of the current row
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPrefHeight(220);
                rowConstraints.setMinHeight(220);
                rowConstraints.setMaxHeight(220);            
                gpReport.getRowConstraints().add(rowConstraints);    
                //
                returnValue = Util.readFromSocket(scannerSocket);
                //
                row++;
                //
                if(row > 100){
                    break;
                }
            }
            //
            if(row == 0){
                Util.showAlert(Alert.AlertType.INFORMATION, "Nothing Found.");
            }
            //
            inputStream.close();
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }    

    //--------------------------------------------------------------------------    
    private void searchOnSinger(String searchKey) {
        
        String cellInfo;
        int row;
        String returnValue, fileName;
        //جدول خروجی آماده شود
        //ابتدا محتویات پاک شود
        gpReport.getChildren().clear();
        gpReport.getRowConstraints().clear();        
        // Set the horizontal gap
        gpReport.setHgap(10.0);
        // Set the vertical gap
        gpReport.setVgap(10.0);
        // Set the width for the first column
         ColumnConstraints firstColumnConstraints = gpReport.getColumnConstraints().get(0);
         firstColumnConstraints.setPrefWidth(220);
         // Set the width for the second column
         ColumnConstraints secondColumnConstraints = gpReport.getColumnConstraints().get(1);
         secondColumnConstraints.setPrefWidth(480);
        try {
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            //خط زیر بسیار ضروری است زیرا زمان خواند فایل از سوکت دستور خواندن بلاکینک است و اگر خط زیر نباشد خواندن از سوکت در آخرین اجرای حلقه به 
            //دلیل اینکه چیزی برای خواند نیست متوقف می شود
            socket.setSoTimeout(200);
            // Get the input stream of the socket
            InputStream inputStream = socket.getInputStream();
            //
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_SearchSinger");
            Util.write2Socket(socket, searchKey);
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            row = 0;
            while(!returnValue.equals("Finished")){
                //قبل و بعد از ارسال فایل تصویر پیغام بفرست تا درست کار کند
                Util.write2Socket(socket, "Get Image");                
                //عکس خواننده در خانه اول ذخیره گردد
                fileName = "Download\\" + "temp" + row + ".jpg";
                //فایل تصویر کاور آلبوم گرفته شود و در فولدر دانلود ذخیره گردد 
                Util.getFileFromPortUsingInputStream(fileName, inputStream);
                //حالا به سرور بگو فایل تصویر را گرفتم این لازم است زیرا اگر دوباره بلافاصله شروع به خواندن کنیم نمی دانم چرا اطلاعات خواند نمی شود
                Util.write2Socket(socket, "Image Received");
                // Create a new ImageView for each row
                Image image = new Image("file:///" + System.getProperty("user.dir") + "\\" + fileName);
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(200);
                // Add padding to the first cell
                GridPane.setMargin(imageView, new Insets(10, 0, 0, 10)); // Adds 10 pixels of padding
                // Add the image to the first cell of the current row
                gpReport.add(imageView, 0, row);
                // اطلاعات خواننده در سلول دوم از سطر جاری درج شود
                Text cellText = new Text();
                cellText.setFill(Paint.valueOf("1db954"));
                cellInfo = "";
                for(int i = 1; i < 3; i++)
                {
                    returnValue = Util.readFromSocket(scannerSocket);
                    //
                    cellInfo += returnValue + "\n";
                }
                cellText.setText(cellInfo);
                // Add the text to the second cell of the current row
                cellText.setWrappingWidth(470);
                cellText.setTextAlignment(TextAlignment.LEFT);
                gpReport.add(cellText, 1, row);
                // Set the height of the current row
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPrefHeight(220);
                rowConstraints.setMinHeight(220);
                rowConstraints.setMaxHeight(220);            
                gpReport.getRowConstraints().add(rowConstraints);    
                //
                returnValue = Util.readFromSocket(scannerSocket);
                //
                row++;
                //
                if(row > 100){
                    break;
                }
            }
            //
            if(row == 0){
                Util.showAlert(Alert.AlertType.INFORMATION, "Nothing Found.");
            }
            //
            inputStream.close();
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }    

    //--------------------------------------------------------------------------    
    private void searchOnPlaylist(String searchKey) {
        String cellInfo;
        int row;
        String returnValue, fileName;
        //جدول خروجی آماده شود
        //ابتدا محتویات پاک شود
        gpReport.getChildren().clear();
        gpReport.getRowConstraints().clear();        
        // Set the horizontal gap
        gpReport.setHgap(10.0);
        // Set the vertical gap
        gpReport.setVgap(10.0);
        // Set the width for the first column
         ColumnConstraints firstColumnConstraints = gpReport.getColumnConstraints().get(0);
         firstColumnConstraints.setPrefWidth(100);
         // Set the width for the second column
         ColumnConstraints secondColumnConstraints = gpReport.getColumnConstraints().get(1);
         secondColumnConstraints.setPrefWidth(600);
        try {
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            //خط زیر بسیار ضروری است زیرا زمان خواند فایل از سوکت دستور خواندن بلاکینک است و اگر خط زیر نباشد خواندن از سوکت در آخرین اجرای حلقه به 
            //دلیل اینکه چیزی برای خواند نیست متوقف می شود
            socket.setSoTimeout(200);
            // Get the input stream of the socket
            InputStream inputStream = socket.getInputStream();
            //
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_SearchPlaylist");
            Util.write2Socket(socket, searchKey);
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            row = 0;
            while(!returnValue.equals("Finished")){
                //ستون اول را با عرض کم خالی می گذاریم
                // اطلاعات خواننده در سلول دوم از سطر جاری درج شود
                Text cellText = new Text();
                cellText.setFill(Paint.valueOf("1db954"));
                cellInfo = "";
                for(int i = 1; i < 5; i++)
                {
                    returnValue = Util.readFromSocket(scannerSocket);
                    //
                    cellInfo += returnValue + "\n";
                }
                cellText.setText(cellInfo);
                // Add the text to the second cell of the current row
                cellText.setWrappingWidth(580);
                cellText.setTextAlignment(TextAlignment.LEFT);
                gpReport.add(cellText, 1, row);
                // Set the height of the current row
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPrefHeight(130);
                rowConstraints.setMinHeight(130);
                rowConstraints.setMaxHeight(130);            
                gpReport.getRowConstraints().add(rowConstraints);    
                //
                returnValue = Util.readFromSocket(scannerSocket);
                //
                row++;
                //
                if(row > 100){
                    break;
                }
            }
            //
            if(row == 0){
                Util.showAlert(Alert.AlertType.INFORMATION, "Nothing Found.");
            }
            //
            inputStream.close();
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------    
    private void searchOnSong(String searchKey) {
        String cellInfo;
        int row;
        String returnValue;
        String userID = "", songTitle = "";
        //جدول خروجی آماده شود
        //ابتدا محتویات پاک شود
        gpReport.getChildren().clear();
        gpReport.getRowConstraints().clear();        
        // Set the horizontal gap
        gpReport.setHgap(10.0);
        // Set the vertical gap
        gpReport.setVgap(10.0);
        // Set the width for the first column
         ColumnConstraints firstColumnConstraints = gpReport.getColumnConstraints().get(0);
         firstColumnConstraints.setHalignment(HPos.CENTER);
         firstColumnConstraints.setPrefWidth(120);
         // Set the width for the second column
         ColumnConstraints secondColumnConstraints = gpReport.getColumnConstraints().get(1);
         secondColumnConstraints.setPrefWidth(580);
        try {
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            //خط زیر بسیار ضروری است زیرا زمان خواند فایل از سوکت دستور خواندن بلاکینک است و اگر خط زیر نباشد خواندن از سوکت در آخرین اجرای حلقه به 
            //دلیل اینکه چیزی برای خواند نیست متوقف می شود
            socket.setSoTimeout(200);
            // Get the input stream of the socket
            InputStream inputStream = socket.getInputStream();
            //
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_SearchSong");
            Util.write2Socket(socket, searchKey);
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            row = 0;
            while(!returnValue.equals("Finished")){
                //ستون اول را با عرض کم خالی می گذاریم
                // اطلاعات خواننده در سلول دوم از سطر جاری درج شود
                Text cellText = new Text();
                cellText.setFill(Paint.valueOf("1db954"));
                cellInfo = "";
                for(int i = 1; i < 8; i++)
                {
                    returnValue = Util.readFromSocket(scannerSocket);
                    //
                    if(i > 1){
                        cellInfo += returnValue + "\n";
                    }
                    if(i == 1){
                        userID = returnValue.substring(8);                    //returnValue is like: "UserID: Ali" 
                    }
                    else if(i == 2){
                        songTitle = returnValue.substring(6);                    //returnValue is like: "Song: Bahar"                         
                    }
                }
                cellText.setText(cellInfo);                
                // Add the text to the second cell of the current row
                cellText.setWrappingWidth(670);
                cellText.setTextAlignment(TextAlignment.LEFT);
                gpReport.add(cellText, 1, row);
                // Set the height of the current row
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPrefHeight(130);
                rowConstraints.setMinHeight(130);
                rowConstraints.setMaxHeight(130);    
                gpReport.getRowConstraints().add(rowConstraints);  
                //حالا سلول اول برای دانلود آماده شود
                String fileName = "\\Files\\" + "Play" + ".jpg";
                fileName = "file:///" + System.getProperty("user.dir") + fileName;
                Image image = new Image(fileName);
                ImageView imageView = new ImageView(image);
                PlaySongOnActionParameters hlSong = new PlaySongOnActionParameters(userID, songTitle);
                //
                imageView.setOnMouseClicked(event -> downloadSong(hlSong));
                //حالا تصویر پلی به سلول اضافه شود
                gpReport.add(imageView, 0, row);                
                //
                returnValue = Util.readFromSocket(scannerSocket);
                //
                row++;
                //
                if(row > 100){
                    break;
                }
            }
            //
            if(row == 0){
                Util.showAlert(Alert.AlertType.INFORMATION, "Nothing Found.");
            }
            //
            inputStream.close();
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------    
//    private void downloadSong(String userID, String songTitle) {
    private void downloadSong(PlaySongOnActionParameters hlSong) {    
        String fullName, role, phoneNumber, email, address, playlists, albums;
        String message, fileName;
        //
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Confirm?");
        DialogPane dialogPane = alert.getDialogPane();
        Optional <ButtonType> result = alert.showAndWait();
        if (!result.isPresent() || result.get() != ButtonType.OK){
            return;
        }
        //دانلود تایید شده است
        //فایل موسیقی از طریق سوکت دریافت شود
        try{
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            //خط زیر بسیار ضروری است زیرا زمان خواند فایل از سوکت دستور خواندن بلاکینک است و اگر خط زیر نباشد خواندن از سوکت در آخرین اجرای حلقه به 
            //دلیل اینکه چیزی برای خواند نیست متوقف می شود
            socket.setSoTimeout(200);
            
            // Get the input stream of the socket
            InputStream inputStream = socket.getInputStream();
            
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور ارسال شود
            Util.write2Socket(socket, "_GetSong");
            Util.write2Socket(socket, hlSong.getUserID());
            Util.write2Socket(socket, hlSong.getSongTitle());
            //از سرور سوال کن آیا فایل موسیقی وجود دارد پاسخ یس و نو است
            Util.write2Socket(socket, "Have MP3?");
            message  = Util.readFromSocket(scannerSocket);              //"Yes" or "No"
            if(message.equals("Yes")){
                Util.write2Socket(socket, "Please send the song file");
                //
                fileName = "Download\\" + "song-" + hlSong.getSongTitle() + ".Mp3";
                Util.getFileFromPortUsingInputStream(fileName, inputStream);
                //حالا به سرور بگو فایل تصویر را گرفتم این لازم است زیرا اگر دوباره بلافاصله شروع به خواندن کنیم نمی دانم چرا اطلاعات خواند نمی شود
                Util.write2Socket(socket, "File Received");
                //حالا فایل دانلود شده پلی شود
                fileName = "file:///" + System.getProperty("user.dir") + "\\" + fileName;
                fileName = fileName.replace("\\", "/");
                playMp3File(fileName);
            }
            else{
                Util.showAlert(Alert.AlertType.INFORMATION, "Song file not found!");
            }
            //
            socket.close();
        }
        catch(Exception ex){            
        }
    }    
    //--------------------------------------------------------------------------    
    @FXML
    private void btnGoClicked(ActionEvent event) {
        String searchGroup;
        
        if(tfSearch.getText().trim().equals("")){
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Search Text!");
            return;
        }
        searchGroup = cbSearchGroup.getSelectionModel().getSelectedItem().toString();
        
        switch(searchGroup)
        {
            case "Album":
                searchOnAlbum(tfSearch.getText().trim());
                break;
            case "Singer":
                searchOnSinger(tfSearch.getText().trim());
                break;
            case "Playlist":
                searchOnPlaylist(tfSearch.getText().trim());
                break;
            case "Song":
                searchOnSong(tfSearch.getText().trim());
                break;
        }
    }
    
    //--------------------------------------------------------------------------    
    public void playMp3File(String fileName){

        try{
            String encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            encodedFileName = encodedFileName.replace("+", "%20"); // Replace '+' with '%20' for spaces
            encodedFileName = encodedFileName.replace("file%3A%2F%2F", "file:///");            //اول رشته باید اینجوری باشه
            Media media = new Media(encodedFileName);
            //اول اگرموسیقی قبلی در حال پخش هست آنرا استپ کن
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            mediaPlayer = new MediaPlayer(media);
            //حالا پلی کن
            mediaPlayer.play();
            //تصویر استاپ را نشان بده
            ivStop.setVisible(true);
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
    }    
    
    //--------------------------------------------------------------------------    
    //در روتین زیر با کلیک کردن روی تصویر استاپ اجرای موسیقی متوقف می شود
    @FXML
    public void ivStopClicked(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        ivStop.setVisible(false);
    }
        
}

