package objects;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends Content {
    private List<Song> songs;

    public Playlist(int ID, String title, List<String> contributors, int duration, String description, int likeCount, List<Song> songs) {
        super(ID, title, contributors, duration, description, likeCount);
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void addSong(Song song) {
        songs.add(song);
        setDuration(getDuration() + song.getDuration());
    }

    public void removeSong(Song song) {
        songs.remove(song);
        setDuration(getDuration() - song.getDuration());
    }
}




