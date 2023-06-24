package spotifyserver;

public class Singer {
    private String userID;
    private String fullName;

    //------------------------------------------------------------
    public Singer(String userID, String fullName) {
        this.userID = userID;
        this.fullName = fullName;
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
    public String getFullName() {
        return fullName;
    }

    //----------------------------------
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //------------------------------------------------------------
    @Override
    public String toString() {
        return "Singer [full Name =" + fullName + "]";
    }
}
