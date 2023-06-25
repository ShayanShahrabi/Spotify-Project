package spotifyclient;

public class LikeSingerParameters {
    String userID;
    String singerID;
    
    //------------------------------------------------------------
    public LikeSingerParameters(String userID, String singerID) {
        this.userID = userID;
        this.singerID = singerID;
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
    public String getSingerID() {
        return singerID;
    }

    //----------------------------------
    public void setSingerID(String singerID) {
        this.singerID = singerID;
    }    
}
