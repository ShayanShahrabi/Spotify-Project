package objects;

import java.util.ArrayList;
import java.util.List;

public class Artist {
    private int userID;
    private String name;
    private String password;
    private String email;
    private String profilePicture;
    private String biography;
    private List<Album> albums;
    private String mainGenre;
    private List<String> socialLinks;

    public Artist(int userID, String name, String password, String email, String biography, String mainGenre, String genre) {
        this.userID = userID;
        this.name = name;
        this.password = password;
        this.email = email;
        this.biography = biography;
        this.albums = new ArrayList<>();
        this.mainGenre = mainGenre;
        this.socialLinks = new ArrayList<>();
    }

    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public String getBiography() {
        return biography;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public String getMainGenre() {
        return mainGenre;
    }

    public List<String> getSocialLinks() {
        return socialLinks;
    }

    public void addSocialLink(String link) {
        socialLinks.add(link);
    }

}

