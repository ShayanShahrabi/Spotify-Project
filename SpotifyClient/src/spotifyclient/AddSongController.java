package spotifyclient;

import java.io.File;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class AddSongController implements Initializable {

    final String uID;                           //در واقع از این متغیر برای پاس شدن شناسه کاربر استفاده می کنیم
    
    @FXML
    TextField tfArtist;
    
    @FXML
    TextField tfSongTitle;    

    @FXML
    ComboBox cbAlbum;

    @FXML
    ComboBox cbGenre;
    
    @FXML
    TextField tfDuration;    

    @FXML
    TextField tfReleasedDate;
    
    @FXML
    TextField tfFileAbsolutePath;
  
    //--------------------------------------------------------------------------    
    public AddSongController(String userID){
        this.uID = userID;
    }
    
    //--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbGenre.getItems().addAll("Classical", "Country music", "Folk music", "Heavy metal", "Hip-hop", "Jazz", "Pop", "Rock");
        cbGenre.getSelectionModel().select(0);
        //
        tfArtist.setText(this.uID);
        //
        fillAlbum();        
    }    
    
    //--------------------------------------------------------------------------    
    @FXML
    private void fillAlbum() {
        String SQLSelect, returnValue;
        cbAlbum.getItems().clear();
        //ارسال اطلاعات روی پورت
        SQLSelect = "Select albumTitle From Album Where userID = '" + this.uID + "' Order By albumTitle";
        try {
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_GetList");
            Util.write2Socket(socket, SQLSelect);
            Util.write2Socket(socket, "albumTitle");
            //حالا یکی یکی آلبوم های این خواننده را از پورت بخوان
            returnValue = Util.readFromSocket(scannerSocket);
            while(!returnValue.equals("Finished")){
                cbAlbum.getItems().add(returnValue);
                returnValue = Util.readFromSocket(scannerSocket);
            }
            //اولین آیتم در صورت وجود انتخاب شود
            if(!cbAlbum.getItems().isEmpty()){
                cbAlbum.getSelectionModel().select(0);
            }                
            //
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------    
    boolean testOK() {
        
        if(tfArtist.getText().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Artist!");
            return false;
        }
        if(tfSongTitle.getText().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Song Title!");
            tfSongTitle.requestFocus();
            return false;
        }
        if(cbAlbum.getValue() == null)
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Album Title!");
            cbAlbum.requestFocus();
            return false;
        }
        if(tfDuration.getText().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Duration!");
            tfDuration.requestFocus();
            return false;
        }
        if(tfReleasedDate.getText().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Released Date!");
            tfReleasedDate.requestFocus();
            return false;
        }
        if(tfFileAbsolutePath == null || tfFileAbsolutePath.getText().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Song Is Not Uploaded!");
            return false;
        }        
        return true;
    }    
    
    //--------------------------------------------------------------------------    
    @FXML
    public void btnBrowseClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.MP3"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            if(selectedFile.length() > 100 * 1024 * 1024){
                Util.showAlert(Alert.AlertType.WARNING, "File Size Exceeds 5MB!");
                tfFileAbsolutePath.setText("");
            }
            else{
                tfFileAbsolutePath.setText(selectedFile.getAbsolutePath());
            }
        }
        else{
            tfFileAbsolutePath.setText("");
        }
    }
    
    //--------------------------------------------------------------------------    
    @FXML
    public void btnSaveClicked() {
        String returnValue;
        if(!testOK()){
            return;
        }
        //ارسال اطلاعات روی پورت
        try {

            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_AddSong");
            Util.write2Socket(socket, this.uID);
            Util.write2Socket(socket, tfSongTitle.getText().trim());
            Util.write2Socket(socket, cbAlbum.getValue().toString().trim());           
            Util.write2Socket(socket, cbGenre.getValue().toString().trim());
            Util.write2Socket(socket, tfDuration.getText().trim());
            Util.write2Socket(socket, tfReleasedDate.getText().trim());            
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            if(returnValue.equals("Error")){
                Util.showAlert(Alert.AlertType.WARNING, "Dupplicated Song Title!");
            }
            else{
                //حالا فایل موسیقی ارسال شود
                Util.sendFile2Port(tfFileAbsolutePath.getText(), socket);
                //
                Util.showAlert(Alert.AlertType.INFORMATION, "Song added.");
                tfSongTitle.setText("");
                tfDuration.setText("");
                tfReleasedDate.setText("");
                tfFileAbsolutePath.setText("");
            }           
            //
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }        
}
