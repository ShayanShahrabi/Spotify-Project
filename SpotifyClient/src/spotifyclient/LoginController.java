package spotifyclient;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    
   
    @FXML
    private TextField tfUserID;
    
    @FXML
    private PasswordField pfPassword;
    
    @FXML
    private AnchorPane anchorPaneLogin;

    //--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    //--------------------------------------------------------------------------    
    private boolean TestOK() {
        if(tfUserID.getText().trim().equals(""))
        {
            Util.showAlert(Alert.AlertType.ERROR, "Invalid User ID!");
            tfUserID.requestFocus();
            return false;
        }
        if(pfPassword.getText().trim().equals(""))
        {
            Util.showAlert(Alert.AlertType.ERROR, "Invalid Password!");
            pfPassword.requestFocus();
            return false;
        }
        return true;
    }
    //--------------------------------------------------------------------------    
    @FXML
    private void btnLoginClicked(ActionEvent event) {
        String type;
        FXMLLoader loader;
        String returnValue;
        if(!TestOK()){
            return;
        }
        //باز کردن یک کنترل دیگر
        try {

            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_Login");
            Util.write2Socket(socket, tfUserID.getText().trim());
            Util.write2Socket(socket, pfPassword.getText().trim());
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            if(returnValue.equals("Invalid User")){
                Util.showAlert(Alert.AlertType.INFORMATION, "Invalid user!");
            }
            else{
                type = returnValue;
                //بستن استیج فعلی
                Stage stage = (Stage) anchorPaneLogin.getScene().getWindow();
                stage.close();

                Parent root;
                
                //پاس کردن نام کاربری
                loader = new FXMLLoader(getClass().getResource("UserMenu.fxml"));
                root = loader.load();
                
                UserMenuController userMenuController = loader.getController();
                userMenuController.setLabelUserIDText(tfUserID.getText());
                userMenuController.setLabelUserTypeText(type);
                userMenuController.setupMenu(type);

                Scene scene = new Scene(root);
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Spotify");
                stage.show();
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
    private void btnExitClicked(ActionEvent event) {
        closeStage();
        System.exit(0);
    }
    
    //--------------------------------------------------------------------------    
    private void closeStage(){
        Stage stage = (Stage) anchorPaneLogin.getScene().getWindow();
        stage.close();
    }
    
    //--------------------------------------------------------------------------    
    @FXML
    private void btnSignUpClicked(ActionEvent event) {
        //بستن استیج فعلی
        Stage stage = (Stage) anchorPaneLogin.getScene().getWindow();
        stage.close();
        //باز کردن یک کنترل دیگر
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Signup.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Sign Up");
            stage.show();
        } catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
}
