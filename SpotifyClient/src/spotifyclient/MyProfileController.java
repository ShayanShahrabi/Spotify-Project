package spotifyclient;

import java.io.File;
import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

public class MyProfileController implements Initializable {

    final String uID;                           //در واقع از این متغیر برای پاس شدن شناسه کاربر استفاده می کنیم    
    
    @FXML
    Button btnSave;
    
    @FXML
    TextField tfUserID;
    
    @FXML
    TextField tfFullName;
    
    @FXML
    TextField tfRole;
    
    @FXML
    TextField tfPhoneNumber;
    
    @FXML
    TextField tfEmail;
    
    @FXML
    TextField tfAddress;
    
    @FXML
    TextField tfFilePhotoPath;
    
    @FXML
    TextArea taPlaylist;
    
    @FXML
    TextArea taAlbums;
    
    @FXML
    ImageView ivPhoto;
    
    //--------------------------------------------------------------------------    
    public MyProfileController(String userID){
        this.uID = userID;
    }
    
    //--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tfUserID.setText(this.uID);
        fillForm();
        btnSave.setVisible(false);
    }   
    
    //--------------------------------------------------------------------------    
    @FXML
    public void btnBrowseClicked() {
        String fileName;
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files","*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        
        if (selectedFile != null) {
            if(selectedFile.length() > 1 * 1024 * 1024){
                Util.showAlert(Alert.AlertType.WARNING, "File Size Exceeds 1 MB!");
                tfFilePhotoPath.setText("");
            }
            else{
                try{
                    tfFilePhotoPath.setText(selectedFile.getAbsolutePath());
                    fileName = "file:///" + tfFilePhotoPath.getText();
                    Image image = new Image(fileName);
                    ivPhoto.setImage(image);
                    //
                    btnSave.setVisible(true);
                }
                catch (Exception ex){
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        }
        else{
            tfFilePhotoPath.setText("");
        }
    }
    
    //--------------------------------------------------------------------------    
    @FXML
    public void btnSaveClicked() {
        String message;
        try {

            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور روی پوت ارسال شود
            Util.write2Socket(socket, "_UserImage");
            Util.write2Socket(socket, tfUserID.getText().trim());
            //حالا یک خواندن انجام شود
            message = Util.readFromSocket(scannerSocket);               //The message is: "Send File"
            //حالا فایل تصویر کاربر ارسال شود
            Util.sendFile2Port(tfFilePhotoPath.getText(), socket);
            //
            Util.showAlert(Alert.AlertType.INFORMATION, "Image Saved.");
            //
            btnSave.setVisible(false);
            tfFilePhotoPath.setText("");
            //
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
    //--------------------------------------------------------------------------    
    private void fillForm(){
        String fullName, role, phoneNumber, email, address, playlists, albums;
        String message, fileName;
        try{
            Socket socket = new Socket(SpotifyClient.SERVER_ADDRESS, SpotifyClient.PORT_NUMBER);
            //خط زیر بسیار ضروری است زیرا زمان خواند فایل از سوکت دستور خواندن بلاکینک است و اگر خط زیر نباشد خواندن از سوکت در آخرین اجرای حلقه به 
            //دلیل اینکه چیزی برای خواند نیست متوقف می شود
            socket.setSoTimeout(200);
            
            // Get the input stream of the socket
            InputStream inputStream = socket.getInputStream();
            
            Scanner scannerSocket = new Scanner(socket.getInputStream());        
            //حالا اطلاعات برای سرور ارسال شود
            Util.write2Socket(socket, "_MyProfile");
            Util.write2Socket(socket, this.uID);
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            fullName = Util.readFromSocket(scannerSocket);
            role = Util.readFromSocket(scannerSocket);
            phoneNumber = Util.readFromSocket(scannerSocket);
            email = Util.readFromSocket(scannerSocket);
            address = Util.readFromSocket(scannerSocket);
            playlists = Util.readFromSocket(scannerSocket);
            albums = Util.readFromSocket(scannerSocket);
            //
            tfFullName.setText(fullName);
            tfRole.setText(role);
            tfPhoneNumber.setText(phoneNumber);
            tfEmail.setText(email);
            tfAddress.setText(address);
            taPlaylist.setText(playlists);
            taAlbums.setText(albums);
            //از سرور سوال کن آیا می خواهی عکس ارسال کنی پاسه یس و نو است
            Util.write2Socket(socket, "Have Photo?");
            message  = Util.readFromSocket(scannerSocket);
            if(message.equals("Yes")){
                Util.write2Socket(socket, "Please send the image file");
                //
                fileName = "Download\\" + "temp" + ".jpg";
                Util.getFileFromPortUsingInputStream(fileName, inputStream);
                //حالا به سرور بگو فایل تصویر را گرفتم این لازم است زیرا اگر دوباره بلافاصله شروع به خواندن کنیم نمی دانم چرا اطلاعات خواند نمی شود
                Util.write2Socket(socket, "Image Received");
                //حالا فایل دریافت شده نشان دهده شود
                fileName = "file:///" + System.getProperty("user.dir") + "\\" + fileName;
                Image image = new Image(fileName);
                ivPhoto.setImage(image);
            }
            //
            socket.close();
        }
        catch(Exception ex){            
        }
    }
}
