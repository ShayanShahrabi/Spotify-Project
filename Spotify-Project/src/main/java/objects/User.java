package objects;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userID;
    private String username;
    private String password;
    private String email;
    private String profilePicture;
    private List<Playlist> playlists;
    private List<Podcast> subscribedPodcasts;
    private List<Episode> downloadedEpisodes;

    // Constructor
    public User(int userID, String username, String password, String email) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.email = email;
        this.playlists = new ArrayList<>();
        this.subscribedPodcasts = new ArrayList<>();
        this.downloadedEpisodes = new ArrayList<>();
    }

    // Getters and Setters

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    public List<Podcast> getSubscribedPodcasts() {
        return subscribedPodcasts;
    }

    public void setSubscribedPodcasts(List<Podcast> subscribedPodcasts) {
        this.subscribedPodcasts = subscribedPodcasts;
    }

    public List<Episode> getDownloadedEpisodes() {
        return downloadedEpisodes;
    }

    public void setDownloadedEpisodes(List<Episode> downloadedEpisodes) {
        this.downloadedEpisodes = downloadedEpisodes;
    }

    // Additional methods for manipulating playlists, subscribed podcasts, downloaded episodes, etc.
    // ...
}

