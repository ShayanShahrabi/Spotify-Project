
package spotifyclient;

import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SignupController implements Initializable {

    @FXML
    TextField tfUserID;
    @FXML
    TextField tfFullName;
    @FXML
    PasswordField pfPassword;
    @FXML
    PasswordField pfPasswordConfirm;
    @FXML
    RadioButton rbSinger;
    @FXML
    RadioButton rbNoSinger;
    @FXML
    TextField tfEmail;
    @FXML
    TextField tfAddress;
    @FXML
    TextField tfPhoneNumber;
    
    @FXML
    private AnchorPane anchorPaneSignUp;
//--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfAddress.requestFocus();
    }    

//--------------------------------------------------------------------------    
    @FXML
    private void btnLoginClicked(ActionEvent event) {        
        //بستن استیج فعلی
        Stage stage = (Stage) anchorPaneSignUp.getScene().getWindow();
        stage.close();
        //باز کردن یک کنترل دیگر
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (Exception ex) {
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
//--------------------------------------------------------------------------    
    @FXML
    private void btnSignUpClicked(ActionEvent event) throws SQLException {
        String returnValue, type;
        if(!TestOK())
            return;
        //        
        try{
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور ارسال شود
            Util.write2Socket(socket, "_CreateAccount");
            Util.write2Socket(socket, tfUserID.getText().trim());
            Util.write2Socket(socket, pfPassword.getText().trim());
            if(rbSinger.isSelected()){
                type = "Singer";
            }
            else{
                type = "No Singer";
            }
            Util.write2Socket(socket, type);
            Util.write2Socket(socket, tfFullName.getText().trim());            
            Util.write2Socket(socket, tfEmail.getText().trim());
            Util.write2Socket(socket, tfAddress.getText().trim());
            Util.write2Socket(socket, tfPhoneNumber.getText().trim());
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            returnValue = Util.readFromSocket(scannerSocket);
            if(returnValue.equals("Error")){
                Util.showAlert(Alert.AlertType.WARNING, "Duplicated ID!");
            }
            else{
                Util.showAlert(Alert.AlertType.INFORMATION, "Account created!");
                //برگرد به صفحه لاگین
                btnLoginClicked(event);
            }
            //
            socket.close();
        }
        catch(Exception ex){            
        }
    }
//--------------------------------------------------------------------------    
    private boolean TestOK() {
        if(tfUserID.getText().trim().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid ID!");

            tfUserID.requestFocus();
            return false;
        }
        if(tfFullName.getText().trim().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Name!");

            tfFullName.requestFocus();
            return false;
        }        
        if(pfPassword.getText().trim().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Password!");

            pfPassword.requestFocus();
            return false;
        }
        if(!pfPassword.getText().trim().equals(pfPasswordConfirm.getText()))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Passwords do no match!");

            pfPassword.requestFocus();
            return false;
        }
        if(tfPhoneNumber.getText().trim().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Phone Number!");

            tfPhoneNumber.requestFocus();
            return false;
        }        
        //بررسی فرمت ایمیل
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tfEmail.getText());
        if(!matcher.matches())
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid email format!");

            tfEmail.requestFocus();
            return false;
        }
        //
        if(tfAddress.getText().trim().equals(""))
        {
            Util.showAlert(Alert.AlertType.WARNING, "Invalid Address!");

            tfAddress.requestFocus();
            return false;
        }        
        return true;
    }
    
}
