package spotifyclient;

import java.io.File;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SpotifyClient extends Application {

    public static final int PORT_NUMBER = 4444;
    public static final String SERVER_ADDRESS = "localhost";
    
    //--------------------------------------------------------------------------
    @Override
    public void start(Stage stage) throws Exception {
        try{
            Image image;
            String imagePath = "Mhr.png";
            File file = new File(imagePath);
            String absolutePath = file.getAbsolutePath();
            image = new Image("file:///" + absolutePath);
            stage.getIcons().add(image);               

            Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle("Login");
            stage.setResizable(false);

            stage.show();       
        }
        catch (Exception ex){
            System.out.println("Error: " + ex.getMessage());
        }
    }

    //--------------------------------------------------------------------------    
    public static void main(String[] args) {
        launch(args);
    }
    
}
