package Server;

import com.google.gson.Gson;
import src.main.java.Artist;
import src.main.java.Song;

public class Response {
    // Define the response object structure according to your needs
    public static void main(String[] args) {
        // JSON string representing a Song object
        String json = "{\"name\":\"My Song\",\"genre\":\"Pop\",\"artist\":{\"username\":\"john_doe\",\"id\":1234,\"name\":\"John Doe\"}}";

        // Create Gson object
        Gson gson = new Gson();

        // Convert JSON string to Song object
        Song song = gson.fromJson(json, Song.class);

        // Access the deserialized Song object
        System.out.println("Song Name: " + song.getName());
        System.out.println("Genre: " + song.getGenre());

        Artist artist = song.getArtist();
        System.out.println("Artist Name: " + artist.getName());
        System.out.println("Username: " + artist.getUsername());
    }
}

