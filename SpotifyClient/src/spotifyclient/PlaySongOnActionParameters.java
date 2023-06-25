package spotifyclient;

public class PlaySongOnActionParameters {
    String userID;
    String songTitle;
    
    //------------------------------------------------------------
    public PlaySongOnActionParameters(String userID, String songTitle) {
        this.userID = userID;
        this.songTitle = songTitle;
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
}
