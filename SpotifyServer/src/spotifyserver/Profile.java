package spotifyserver;

public class Profile {
    
    private String userID;
    private String fullName;
    private String role;
    private String phoneNumber;
    private String email;
    private String address;
    private String playlists;
    private String albums;

    //------------------------------------------------------------
    public Profile(String userID, String fullName, String role, String phoneNumber,String email, String address, String playlists, String albums) {
        this.userID = userID;
        this.fullName = fullName;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.playlists = playlists;
        this.albums = albums;
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
    public String getRole() {
        return role;
    }

    //----------------------------------
    public void setRole(String role) {
        this.role = role;
    }

    //----------------------------------
    public String getPhoneNumber() {
        return phoneNumber;
    }

    //----------------------------------
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    //----------------------------------
    public String getEmail() {
        return email;
    }

    //----------------------------------
    public void setEmail(String email) {
        this.email = email;
    }

    //----------------------------------
    public String getAddress() {
        return address;
    }

    //----------------------------------
    public void setAddress(String address) {
        this.address = address;
    }
    
    //----------------------------------
    public String getPlaylists() {
        return playlists;
    }

    //----------------------------------
    public void setPlaylists(String playlists) {
        this.playlists = playlists;
    }
    
    //----------------------------------
    public String getAlbums() {
        return albums;
    }

    //----------------------------------
    public void setAlbums(String albums) {
        this.albums = albums;
    }
    
    
}
