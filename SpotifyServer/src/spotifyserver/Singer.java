package spotifyserver;

public class Singer {
    private String userID;
    private String fullName;
    private Integer likes;

    //------------------------------------------------------------
    public Singer(String userID, String fullName, Integer likes) {
        this.userID = userID;
        this.fullName = fullName;
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
    public String getFullName() {
        return fullName;
    }

    //----------------------------------
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    //----------------------------------
    public Integer getLikes() {
        return likes;
    }

    //----------------------------------
    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    //------------------------------------------------------------
    @Override
    public String toString() {
        return "Singer [full Name =" + fullName + "]";
    }
}
