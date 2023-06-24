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

public class AddAlbumController implements Initializable {

    final String uID;
    
    @FXML
    TextField tfArtist;

    @FXML
    TextField tfAlbumTitle;

    @FXML
    ComboBox cbGenre;

    @FXML
    TextField tfReleasedDate;
    
    @FXML
    TextField tfFileAbsolutePath;
       
    //--------------------------------------------------------------------------    
    public AddAlbumController(String userID){
        this.uID = userID;
    }    
    
    //--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbGenre.getItems().addAll("Classical", "Country music", "Folk music", "Heavy metal", "Hip-hop", "Jazz", "Pop", "Rock");
        cbGenre.getSelectionModel().select(0);
        //
        tfArtist.setText(this.uID);
    }    

    //--------------------------------------------------------------------------    
    boolean testOK() {
        
        if(tfAlbumTitle.getText().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Album Title!");
            tfAlbumTitle.requestFocus();
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
            Util.showAlert(Alert.AlertType.WARNING, "Album Cover Is Not Uploaded!");
            return false;
        }        
        return true;
    }    
    
    //--------------------------------------------------------------------------    
    @FXML
    public void btnBrowseClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            if(selectedFile.length() > 2 * 1024 * 1024){
                Util.showAlert(Alert.AlertType.WARNING, "File Size Exceeds 2MB!");
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
            Util.write2Socket(socket, "_AddAlbum");
            Util.write2Socket(socket, this.uID);
            Util.write2Socket(socket, tfAlbumTitle.getText().trim());
            Util.write2Socket(socket, cbGenre.getValue().toString().trim());
            Util.write2Socket(socket, tfReleasedDate.getText().trim());            
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            if(returnValue.equals("Error")){
                Util.showAlert(Alert.AlertType.WARNING, "Dupplicated Album Title!");
            }
            else{
                //حالا فایل روکش آلبوم ارسال شود
                Util.sendFile2Port(tfFileAbsolutePath.getText(), socket);
                //
                Util.showAlert(Alert.AlertType.INFORMATION, "Album added.");
                tfAlbumTitle.setText("");
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