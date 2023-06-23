module com.example.spotifyproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;


    opens com.example.spotifyproject to javafx.fxml;
    exports com.example.spotifyproject;
}