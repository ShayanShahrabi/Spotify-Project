package objects;
import java.io.File;
import java.sql.*;

public class Song {
    private int songID;
    private String title;
    private String genre;
    private String album;
    private Artist artist;
    private int duration;
    private Date releaseYear;
    private int likeCount;

    // Constructor
    public Song(int songID, String title, String genre, String album, Artist artist, int duration, Date releaseYear, int likeCount) {
        this.songID = songID;
        this.title = title;
        this.genre = genre;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.releaseYear = releaseYear;
        this.likeCount = likeCount;
    }

    // Getters for the variables
    public int getSongID() {
        return songID;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getAlbum() {
        return album;
    }

    public Artist getArtist() {
        return artist;
    }

    public int getDuration() {
        return duration;
    }

    public Date getReleaseYear() {
        return releaseYear;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getFilePath(Song song) {
        String basePath = "src" + File.separator + "main" + File.separator + "java" + File.separator + "Server" +
                File.separator + "resources";
        String genre = song.getGenre();
        String artistName = song.getArtist().getName();

        genre = genre.replaceAll("[^a-zA-Z0-9]", "");
        artistName = artistName.replaceAll("[^a-zA-Z0-9]", "");

        return basePath + File.separator + genre + File.separator + artistName;
    }

}

