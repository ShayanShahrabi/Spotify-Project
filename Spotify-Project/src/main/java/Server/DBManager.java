package Server;

import objects.Artist;
import objects.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/your_database";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "something";

    // Artist-related methods

    public void addArtist(Artist artist) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO artists (user_id, name, password, email, profile_picture, biography, main_genre) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, artist.getUserID());
            statement.setString(2, artist.getName());
            statement.setString(3, artist.getPassword());
            statement.setString(4, artist.getEmail());
            statement.setString(5, artist.getProfilePicture());
            statement.setString(6, artist.getBiography());
            statement.setString(7, artist.getMainGenre());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Artist getArtist(int userID) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM artists WHERE user_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int retrievedUserID = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String password = resultSet.getString("password");
                String email = resultSet.getString("email");
                String profilePicture = resultSet.getString("profile_picture");
                String biography = resultSet.getString("biography");
                String mainGenre = resultSet.getString("main_genre");
                // Create and return the Artist object
                return new Artist(retrievedUserID, name, password, email, profilePicture, biography, mainGenre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Artist not found
    }

    // Add similar methods for updateArtist(), deleteArtist(), getAllArtists(), etc.

    // Song-related methods

    public void addSong(Song song) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO songs (song_id, title, genre, album, artist_id, duration, release_year, like_count) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, song.getSongID());
            statement.setString(2, song.getTitle());
            statement.setString(3, song.getGenre());
            statement.setString(4, song.getAlbum());
            statement.setInt(5, song.getArtist().getUserID());
            statement.setInt(6, song.getDuration());
            statement.setDate(7, new java.sql.Date(song.getReleaseYear().getTime()));
            statement.setInt(8, song.getLikeCount());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Song getSong(int songID) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT * FROM songs WHERE song_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, songID);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int retrievedSongID = resultSet.getInt("song_id");
                String title = resultSet.getString("title");
                String genre = resultSet.getString("genre");
                String album = resultSet.getString("album");
                int artistID = resultSet.getInt("artist_id");
                int duration = resultSet.getInt("duration");
                Date releaseYear = resultSet.getDate("release_year");
                int likeCount = resultSet.getInt("like_count");
                // Retrieve the associated Artist object
                Artist artist = getArtist(artistID);
                // Create and return the Song object
                return new Song(retrievedSongID, title, genre, album, artist, duration, releaseYear, likeCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Song not found
    }

    // Add similar methods for updateSong(), deleteSong(), getAllSongs()

    // Implement methods for other entities (Content, Album, Playlist, Episode, Podcast) following the same pattern

    // Methods for handling user connections, follow relationships

    public void addUserConnection(int userID1, int userID2) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "INSERT INTO user_connections (user1_id, user2_id) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID1);
            statement.setInt(2, userID2);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getFollowers(int userID) {
        List<Integer> followers = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String query = "SELECT user1_id FROM user_connections WHERE user2_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int followerID = resultSet.getInt("user1_id");
                followers.add(followerID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return followers;
    }

    // Add similar methods for getUserConnections(), removeUserConnection(), etc.
}
