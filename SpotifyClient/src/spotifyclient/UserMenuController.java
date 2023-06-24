package spotifyclient;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserMenuController implements Initializable {

    @FXML
    private AnchorPane anchorPaneUserMenu;
    
    @FXML
    private AnchorPane anchorPaneUserMenuContent;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnAddAlbum;

    @FXML
    private Button btnAddSong;

    @FXML
    private Button btnAddPlaylist;

    @FXML
    private Button btnMyProfile;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnLogout;
    
    @FXML
    private Label lblUserID;
    
    @FXML
    private Label lblUserType;
    
    
    String type;                //login form will fill this field
    //--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    //--------------------------------------------------------------------------    
    @FXML
    private void btnHomeClicked(ActionEvent event){    
        String uID;
        try {
            uID = lblUserID.getText(); //شناسه كاربر
            //
            ArrangeMenuColor("btnHome");
            //
            Parent fxml = FXMLLoader.load(getClass().getResource("Home.fxml"));
            anchorPaneUserMenuContent.getChildren().removeAll();
            anchorPaneUserMenuContent.getChildren().setAll(fxml);
        } 
        catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }    
    
    //--------------------------------------------------------------------------    
    @FXML
    private void btnAddAlbumClicked(ActionEvent event){    
    
        String uID;
        try {
            uID = lblUserID.getText(); //شناسه كاربر
            //
            ArrangeMenuColor("btnAddAlbum");
            //load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddAlbum.fxml"));
            //ایجاد سازنده کنترلر و پاس کردن پارامتر
            AddAlbumController aac = new AddAlbumController(uID);
            loader.setController(aac);
            //
            Parent fxml = loader.load();
            //
            anchorPaneUserMenuContent.getChildren().removeAll();
            anchorPaneUserMenuContent.getChildren().setAll(fxml);
        } 
        catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------    
    @FXML
    private void btnAddSongClicked(ActionEvent event){
        
        String uID;
        try {
            uID = lblUserID.getText(); //شناسه کاربر
            //
            ArrangeMenuColor("btnAddSong");
            //load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddSong.fxml"));
            //ایجاد سازنده کنترلر و پاس کردن پارامتر
            AddSongController asc = new AddSongController(uID);
            loader.setController(asc);
            //
            Parent fxml = loader.load();
            //
            anchorPaneUserMenuContent.getChildren().removeAll();
            anchorPaneUserMenuContent.getChildren().setAll(fxml);
        } 
        catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());            
        }
    }

    //--------------------------------------------------------------------------    
    @FXML
    private void btnAddPlaylistClicked(ActionEvent event){
        
        String uID;
        try {
            uID = lblUserID.getText(); //شناسه کاربر
            //
            ArrangeMenuColor("btnAddPlaylist");
            
            
            //load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddPlaylist.fxml"));
            //ایجاد سازنده کنترلر و پاس کردن پارامتر
            AddPlaylistController apc = new AddPlaylistController(uID);
            loader.setController(apc);
            //
            Parent fxml = loader.load();
            //
            anchorPaneUserMenuContent.getChildren().removeAll();
            anchorPaneUserMenuContent.getChildren().setAll(fxml);
        } 
        catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());            
        }
    }

    //--------------------------------------------------------------------------    
    @FXML
    private void btnSearchClicked(ActionEvent event){
        
        try {
            ArrangeMenuColor("btnSearch");
            //
            Parent fxml = FXMLLoader.load(getClass().getResource("Search.fxml"));
            anchorPaneUserMenuContent.getChildren().removeAll();
            anchorPaneUserMenuContent.getChildren().setAll(fxml);
        } 
        catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }

    //--------------------------------------------------------------------------    
    @FXML
    private void btnMyProfileClicked(ActionEvent event){    
    
        String uID;
        try {
            uID = lblUserID.getText(); //شناسه كاربر
            //
            ArrangeMenuColor("btnMyProfile");
            //load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MyProfile.fxml"));
            //ایجاد سازنده کنترلر و پاس کردن پارامتر
            MyProfileController mpc = new MyProfileController(uID);
            loader.setController(mpc);
            //
            Parent fxml = loader.load();
            //
            anchorPaneUserMenuContent.getChildren().removeAll();
            anchorPaneUserMenuContent.getChildren().setAll(fxml);
        } 
        catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------    
    @FXML
    private void btnLogoutClicked(ActionEvent event) {
        //بستن استیج فعلی
        Stage stage = (Stage) anchorPaneUserMenu.getScene().getWindow();
        stage.close();
        //باز کردن یک کنترلر دیگر
        try {
            ArrangeMenuColor("btnLogout");
            
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } 
        catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }    

    //--------------------------------------------------------------------------    
    private void ArrangeMenuColor(String sw){
        btnHome.setStyle("-fx-background-color: #212121;-fx-text-fill:#1DB954;");
        btnAddAlbum.setStyle("-fx-background-color:  #212121;-fx-text-fill:#1DB954;");
        btnAddSong.setStyle("-fx-background-color:  #212121;-fx-text-fill:#1DB954;");
        btnAddPlaylist.setStyle("-fx-background-color:  #212121;-fx-text-fill:#1DB954;");
        btnMyProfile.setStyle("-fx-background-color:  #212121;-fx-text-fill:#1DB954;");
        btnSearch.setStyle("-fx-background-color: #212121;-fx-text-fill:#1DB954;");
        btnLogout.setStyle("-fx-background-color: #212121;-fx-text-fill:#1DB954;");
        switch(sw){
            case "btnHome":
                btnHome.setStyle("-fx-background-color: #1DB954;-fx-text-fill:#212121;");
                break;
            case "btnAddAlbum":
                btnAddAlbum.setStyle("-fx-background-color: #1DB954;-fx-text-fill:#212121;");
                break;
            case "btnAddSong":
                btnAddSong.setStyle("-fx-background-color: #1DB954;-fx-text-fill:#212121;");
                break;
            case "btnAddPlaylist":
                btnAddPlaylist.setStyle("-fx-background-color: #1DB954;-fx-text-fill:#212121;");
                break;                
            case "btnMyProfile":
                btnMyProfile.setStyle("-fx-background-color: #1DB954;-fx-text-fill:#212121;");
                break;
            case "btnSearch":
                btnSearch.setStyle("-fx-background-color: #1DB954;-fx-text-fill:#212121;");
                break;
            case "btnLogout":
                btnLogout.setStyle("-fx-background-color: #1DB954;-fx-text-fill:#212121;");
                break;
        }
    }
    
    //--------------------------------------------------------------------------    
    public void setLabelUserIDText(String value){
        lblUserID.setText(value);
    }
    
    //--------------------------------------------------------------------------    
    public void setLabelUserTypeText(String value){
        lblUserType.setText(value);
    }

    //--------------------------------------------------------------------------    
    //این روتین اگر کاربر خواننده نباشد دو آیتم از منو را غیر فعال می کند
    public void setupMenu(String type){
        if(type.equals("No Singer")){
            btnAddAlbum.setDisable(true);
            btnAddSong.setDisable(true);                    
        }
    }
        
}
