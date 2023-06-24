package spotifyserver;

public class Album {
    private String userID;
    private String albumTitle;
    private String singer;
    private String genre;
    private String releaseDate;
    private int likes;

    //------------------------------------------------------------
    public Album(String userID, String albumTitle, String singer, String genre, String releaseDate, int likes) {
        this.userID = userID;
        this.albumTitle = albumTitle;
        this.singer = singer;
        this.genre = genre;
        this.releaseDate = releaseDate;
        this.likes = likes;
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
    public String getReleaseDate() {
        return releaseDate;
    }

    //----------------------------------
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    //----------------------------------
    public int getLikes() {
        return likes;
    }

    //----------------------------------
    public void setLikes(int likes) {
        this.likes = likes;
    }
    
    //------------------------------------------------------------
    @Override
    public String toString() {
        return "Album [albumTitle=" + albumTitle + ", singer=" + singer + ", genre=" + genre + ", releaseDate="
                + releaseDate + ", likes=" + likes + "]";
    }
}