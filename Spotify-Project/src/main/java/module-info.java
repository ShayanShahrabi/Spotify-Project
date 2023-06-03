module com.example.spotifyproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.spotifyproject to javafx.fxml;
    exports com.example.spotifyproject;
}