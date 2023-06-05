package Server;
import com.google.gson.Gson;
import src.main.java.Artist;
import src.main.java.Song;

public class Request {
    // Define the request object structure according to your needs
    public static void main(String[] args) {
        // Create an Artist object
        Artist artist = new Artist("john_doe", 1234, "John Doe");

        // Create a Song object with the Artist object
        Song song = new Song("My Song", "Pop", artist);

        // Accessing Song information
        System.out.println("Song Name: " + song.getName());
        System.out.println("Genre: " + song.getGenre());

        // Accessing Artist information
        Artist artistInfo = song.getArtist();
        System.out.println("Artist Name: " + artistInfo.getName());
        System.out.println("Username: " + artistInfo.getUsername());

        // Convert Song object to JSON string
        Gson gson = new Gson();
        String json = gson.toJson(song);
        System.out.println("JSON: " + json);
    }
}
