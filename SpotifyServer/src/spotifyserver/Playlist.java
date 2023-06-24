package spotifyserver;

public class Playlist {
    private String userID;
    private String playlistTitle;    
    private String fullName;
    private String description;

    //------------------------------------------------------------
    public Playlist(String userID, String playlistTitle, String fullName, String description) {
        this.userID = userID;
        this.playlistTitle = playlistTitle;
        this.fullName = fullName;
        this.description = description;
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
    public String getPlaylistTitle() {
        return playlistTitle;
    }

    //----------------------------------
    public void setPlaylistTitle(String playlistTitle) {
        this.playlistTitle = playlistTitle;
    }
    
    //----------------------------------
    public String getFullName() {
        return fullName;
    }

    //----------------------------------
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //----------------------------------
    public String getDescription() {
        return description;
    }

    //----------------------------------
    public void setDescription(String description) {
        this.description = description;
    }

}
