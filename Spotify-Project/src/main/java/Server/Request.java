package Server;
import com.google.gson.Gson;
import objects.Artist;
import objects.Song;

public class Request {
    private String method;
    private String endpoint;
    private String body;

    public Request(String method, String endpoint, String body) {
        this.method = method;
        this.endpoint = endpoint;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public String getBody() {
        return body;
    }
}

//    public static void main(String[] args) {
//        // Create an Artist object
//        Artist artist = new Artist("john_doe", 1234, "John Doe");
//
//        // Create a Song object with the Artist object
//        Song song = new Song("My Song", "Pop", artist);
//
//        // Accessing Song information
//        System.out.println("Song Name: " + song.getName());
//        System.out.println("Genre: " + song.getGenre());
//
//        // Accessing Artist information
//        Artist artistInfo = song.getArtist();
//        System.out.println("Artist Name: " + artistInfo.getName());
//        System.out.println("Username: " + artistInfo.getName());
//
//        // Convert Song object to JSON string
//        Gson gson = new Gson();
//        String json = gson.toJson(song);
//        System.out.println("JSON: " + json);
//    }