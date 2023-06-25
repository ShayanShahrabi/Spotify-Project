package spotifyclient;

import java.io.InputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class HomeController implements Initializable {

    @FXML
    private GridPane gpHome;

    //--------------------------------------------------------------------------    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillgvHome();
    }    
    
    //--------------------------------------------------------------------------    
    public void fillgvHome(){
        String cellInfo;
        int col, imagerow, counter;
        String message, fileName;
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
            Util.write2Socket(socket, "_Home");
            //حالا از پورت بخون آیا به درستی اطلاعات ذخیره شد؟
            message = Util.readFromSocket(scannerSocket);
            counter = 0;
            while(!message.equals("Finished")){
                col = counter % 4;
                if(counter < 4){
                    imagerow = 0;
                }
                else{
                    imagerow = 2;
                }
                //قبل و بعد از ارسال فایل تصویر پیغام بفرست تا درست کار کند
                //از سرور سوال کن آیا می خواهی عکس ارسال کنی پاسه یس و نو است
                Util.write2Socket(socket, "Have Photo?");
                message  = Util.readFromSocket(scannerSocket);
                if(message.equals("Yes")){
                    Util.write2Socket(socket, "Please send the image file");
                    //عکس کاور آلبوم در خانه اول ذخیره گردد
                    fileName = "Download\\" + "temp" + col + ".jpg";
                    //فایل تصویر کاور آلبوم گرفته شود و در فولدر دانلود ذخیره گردد 
                    Util.getFileFromPortUsingInputStream(fileName, inputStream);
                    //حالا به سرور بگو فایل تصویر را گرفتم این لازم است زیرا اگر دوباره بلافاصله شروع به خواندن کنیم نمی دانم چرا اطلاعات خواند نمی شود
                    Util.write2Socket(socket, "Image Received");
                    // حالا فایل تصویر را نشان بده
                    Image image = new Image("file:///" + System.getProperty("user.dir") + "\\" + fileName);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(200);
                    // Add the image to the first cell of the current col
                    gpHome.add(imageView, col, imagerow);
                }
                // اطلاعات آلبوم در سلول دوم از سطر جاری درج شود
                Text cellText = new Text();
                cellInfo = "";
                for(int i = 1; i < 5; i++)
                {
                    message = Util.readFromSocket(scannerSocket);
                    //
                    cellInfo += message + "\n";
                }
                cellText.setText(cellInfo);
                // Add the text to the second cell of the current col
                cellText.setWrappingWidth(200);
                cellText.setTextAlignment(TextAlignment.LEFT);
                gpHome.add(cellText, col, imagerow + 1);
                //
                message = Util.readFromSocket(scannerSocket);
                // Set the height of the current row
                RowConstraints rowConstraints = new RowConstraints();
                rowConstraints.setPrefHeight(220);
                rowConstraints.setMinHeight(220);
                rowConstraints.setMaxHeight(220);            
                gpHome.getRowConstraints().add(rowConstraints);    
                //
                counter++;
                //
                if(counter > 20){
                    break;
                }
            }
            //
            inputStream.close();
            socket.close();
        }
        catch(Exception ex){            
            Util.showAlert(Alert.AlertType.ERROR, ex.getMessage());
        }
    }
    
}
