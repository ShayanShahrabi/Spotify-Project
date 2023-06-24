package spotifyclient;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class AddPlaylistController implements Initializable {

    final String uID;                           //در واقع از این متغیر برای پاس شدن شناسه کاربر استفاده می کنیم
    
    @FXML
    TextField tfCreator;
    
    @FXML
    TextField tfPlaylistTitle;    

    @FXML
    TextField tfDescription;

    @FXML
    ComboBox cbArtist;
    
    @FXML
    ComboBox cbSong;    

    @FXML
    ListView lvSong;
    
    //--------------------------------------------------------------------------    
    public AddPlaylistController(String userID){
        this.uID = userID;
    }
    
    //--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tfCreator.setText(this.uID);
        fillArtist();        
        fillSong();
    }    
    
    //--------------------------------------------------------------------------    
    private void fillArtist() {
        String SQLSelect, message;
        String value, key;
        int index;
        //ارسال اطلاعات روی پورت
        SQLSelect = "Select Distinct Song.userID || ' / ' || fullName As item From Song left join Accounts on Song.userID = Accounts.userID Order By fullName";
        try {
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_GetList");
            Util.write2Socket(socket, SQLSelect);
            Util.write2Socket(socket, "item");
            //حالا یکی یکی آهنگ های این خواننده را از پورت بخوان
            message = Util.readFromSocket(scannerSocket);
            while(!message.equals("Finished")){
                index = message.indexOf(" / ");
                if(index != -1){
                    key = message.substring(0, index);
                    value = message.substring(index + 3);
                    cbArtist.getItems().add(new KeyValue(value, key));
                }
                message = Util.readFromSocket(scannerSocket);
            }
            //اولین آیتم در صورت وجود انتخاب شود
            if(!cbArtist.getItems().isEmpty()){
                cbArtist.getSelectionModel().select(0);
            }                
            //            
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------    
    @FXML
    private void fillSong() {
        String SQLSelect, returnValue, userID;
        
        cbSong.getItems().clear();
        
        KeyValue artist = (KeyValue) cbArtist.getValue();
        if(artist == null){
            return;
        }
        userID = artist.getKey();
        
        //ارسال اطلاعات روی پورت
        SQLSelect = "Select songTitle From Song Where userID = '" + userID + "' Order By SongTitle";
        try {

            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_GetList");
            Util.write2Socket(socket, SQLSelect);
            Util.write2Socket(socket, "songTitle");
            //حالا یکی یکی آهنگ های این خواننده را از پورت بخوان
            cbSong.getItems().clear();
            returnValue = Util.readFromSocket(scannerSocket);
            while(!returnValue.equals("Finished")){
                cbSong.getItems().add(returnValue);
                returnValue = Util.readFromSocket(scannerSocket);
            }
            //اولین آیتم در صورت وجود انتخاب شود
            if(!cbSong.getItems().isEmpty()){
                cbSong.getSelectionModel().select(0);
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
        
        if(tfPlaylistTitle.getText().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Playlist Title!");
            tfPlaylistTitle.requestFocus();
            return false;
        }
        if(tfDescription.getText().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Description!");
            tfDescription.requestFocus();
            return false;
        }
        if(lvSong.getItems().size() < 1)
        {
            Util.showAlert(Alert.AlertType.WARNING, "No Song is added!");
            cbArtist.requestFocus();
            return false;
        }
        return true;
    }    

    //--------------------------------------------------------------------------    
    @FXML
    public void btnAddClicked() {
        String songTitle, item2Add, item, key;
        boolean found;
        int i;
        KeyValue artist = (KeyValue) cbArtist.getValue();
        if(artist == null){
            return;
        }
        songTitle = cbSong.getValue().toString();
        if(songTitle == null){
            return;
        }
        item2Add = artist.getValue() + " / " + songTitle;
        key = artist.getKey() + " / " + songTitle;
        //چک شود آیا آیتم قبلا در لیست هست یا خیر
        i = 0;
        found = false;
        while(!found && i < lvSong.getItems().size())
        {
            item = lvSong.getItems().get(i).toString();
            if(item.equals(item2Add)){
                found = true;
            }            
            i++;
        }
        if(found){
            Util.showAlert(Alert.AlertType.WARNING, "The sond has been inserted already!");
        }
        else{
            lvSong.getItems().add( new KeyValue(item2Add, key));           //KeyValue is a class defined at the buttom            
        }
    }    
    
    //--------------------------------------------------------------------------    
    @FXML
    public void btnRemoveClicked() {

        int index;
        index = lvSong.getSelectionModel().getSelectedIndex();
        if(index != -1){
            lvSong.getItems().remove(index);
        }
        else{
            Util.showAlert(Alert.AlertType.WARNING, "You should select an item from below list.");
        }
    }        
    
    //--------------------------------------------------------------------------    
    @FXML
    public void btnSaveClicked() {
        String returnValue;
        KeyValue item;
        if(!testOK()){
            return;
        }
        //ارسال اطلاعات روی پورت
        try {
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_AddPlaylist");
            Util.write2Socket(socket, this.uID);
            Util.write2Socket(socket, tfPlaylistTitle.getText().trim());
            Util.write2Socket(socket, tfDescription.getText().trim());           
            //حالا لیست آهنگها ارسال شود
            for(int i = 0; i < lvSong.getItems().size(); i++){
                item = (KeyValue) lvSong.getItems().get(i);
                Util.write2Socket(socket, item.getKey());                //for example Ali / Spring which Ali is userID and Spring is the song title
            }
            //اعلام پایان ارسال اطلاعات
            Util.write2Socket(socket, "Finished");
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            if(returnValue.equals("Error")){
                Util.showAlert(Alert.AlertType.WARNING, "Dupplicated Playlist Title!");
            }
            else{
                Util.showAlert(Alert.AlertType.INFORMATION, "Playlist added.");
                tfPlaylistTitle.setText("");
                tfDescription.setText("");
                lvSong.getItems().clear();
            }           
            //
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }  
    }        
 
//--------------------------------------------------------------------------
//تعریف کلاس برای درج در کامبوباکس و لیست ویو
private static class KeyValue {
        private final String value;
        private final String key;

        public KeyValue(String fullName, String key) {
            this.value = fullName;
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return value;
        }
    }    
}
