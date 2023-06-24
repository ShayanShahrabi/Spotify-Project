package spotifyserver;

public class Song {
    private String userID;
    private String songTitle;    
    private String albumTitle;
    private String singer;
    private String genre;
    private String duration;
    private String releaseDate;

    //------------------------------------------------------------
    public Song(String userID, String songTitle, String albumTitle, String singer, String genre, String duration, String releaseDate) {
        this.userID = userID;
        this.songTitle = songTitle;
        this.albumTitle = albumTitle;
        this.singer = singer;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
    }

    // Getters and setters 
    //----------------------------------
    public String getUserID() {
        return userID;
    }

    //----------------------------------
    public void setUserID(String userID) {
        this.userID = userID;
    }
        
    //----------------------------------
    public String getSongTitle() {
        return songTitle;
    }

    //----------------------------------
    public void setSongTitle(String songTitle) {
        this.songTitle = songTitle;
    }
    
    //----------------------------------
    public String getAlbumTitle() {
        return albumTitle;
    }

    //----------------------------------
    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    //----------------------------------
    public String getSinger() {
        return singer;
    }

    //----------------------------------
    public void setSinger(String singer) {
        this.singer = singer;
    }

    //----------------------------------
    public String getGenre() {
        return genre;
    }

    //----------------------------------
    public void setGenre(String genre) {
        this.genre = genre;
    }

    //----------------------------------
    public String getDuration() {
        return duration;
    }

    //----------------------------------
    public void setDuration(String duration) {
        this.duration = duration;
    }
    
    //----------------------------------
    public String getReleaseDate() {
        return releaseDate;
    }

    //----------------------------------
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

}
