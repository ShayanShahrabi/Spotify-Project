package spotifyclient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Formatter;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Util {

    //------------------------------------------------------------
    public static String readFromSocket(Scanner scannerSocket){
        String result = "";
        try{
            if(scannerSocket.hasNext()){
                result = scannerSocket.nextLine();
            }        
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }     
        return result;
    }
    
    //------------------------------------------------------------
    //After each message "\n" will be inserted
    public static void write2Socket(Socket socket, String message){
        try{
            Formatter formatterSocket = new Formatter(socket.getOutputStream());
            formatterSocket.format(message + "\n");
            formatterSocket.flush();
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
    //------------------------------------------------------------
    public static void getFileFromPort(String fileName, Socket socket){
        //خواندن اطلاعات از کنسول
        try{
            //حالا با یک پروتکلی باید فایل را از سوکت بخوانیم
            try {
                // Get the input stream of the socket
                InputStream inputStream = socket.getInputStream();
                // Create a file to save the received data
                File receivedFile = new File(fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                // Read the data from the socket's input stream and write it to the file
                byte[] buffer = new byte[204800];         //Instead of 4096
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    bufferedOutputStream.write(buffer, 0, bytesRead);
                }
                // Close the resources
                bufferedOutputStream.close();
                inputStream.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "\n");
            }            
        }
        catch (Exception ex){
            System.out.println("server connection failed!");
        }
    }    

    //------------------------------------------------------------
    public static void getFileFromPortUsingInputStream(String fileName, InputStream inputStream){
        //خواندن اطلاعات از کنسول
        try{
            //حالا با یک پروتکلی باید فایل را از سوکت بخوانیم
            try {
                // Create a file to save the received data
                File receivedFile = new File(fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(receivedFile);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

                // Read the data from the socket's input stream and write it to the file
                byte[] buffer = new byte[204800];         //Instead of 4096
                int bytesRead;
                //این ترای کچ بسیار لازم است زیر خواندن از سوکت در حلقه زیر بلاکینگ است و به دلیل اینکه بیرون از روتین برای سوکت تایم اوت ست کرده ایم از حلقه خارج می شویم
                //وگرنه در حلقه بار آخر که چیزی برای خواندن از سوکت نبود برنامه همینجوری متوقف می شد و منتظر خواند از بلاک بود
                try{
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        bufferedOutputStream.write(buffer, 0, bytesRead);
                    }
                }
                catch(Exception ex){                    
                }
                // Close the resources
                bufferedOutputStream.close();
                fileOutputStream.close();
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "\n");
            }            
        }
        catch (Exception ex){
            System.out.println("server connection failed!");
        }
    }    

    //------------------------------------------------------------
    //از این روتین برای ارسال فایل روی پورت استفاده می شود
    public static void sendFile2Port(String fileAbsolutePath, Socket socket){
        
        try {
            
            File fileToSend = new File(fileAbsolutePath);
            
            FileInputStream fileInputStream = new FileInputStream(fileToSend);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);

            // Get the output stream of the socket
            OutputStream outputStream = socket.getOutputStream();

            // Write the file to the socket's output stream
            byte[] buffer = new byte[204800];         //Instead of 4096
            int bytesRead;
            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            // Close the resources
            bufferedInputStream.close();
            outputStream.close();
            //
            System.out.println("File sent successfully.\n");
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
        
    //--------------------------------------------------------------------------    
    public static void showAlert(Alert.AlertType at, String Message){
        Alert alert = new Alert(at);
        alert.setHeaderText(null);
        alert.setContentText(Message);
        //آیکون تنظیم شود
        Image image;
        String imagePath = "Mhr.png";
        File file = new File(imagePath);
        String absolutePath = file.getAbsolutePath();
        image = new Image("file:///" + absolutePath);
        
        ImageView icon = new ImageView(image);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.setGraphic(icon);        
        
        alert.showAndWait();
        return;        
    }
    
    
    
}
