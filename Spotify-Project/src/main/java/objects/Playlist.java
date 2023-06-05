package objects;

import java.util.ArrayList;
import java.util.List;

public class Playlist extends Content {
    private List<Song> songs;

    public Playlist(int ID, String title, List<String> contributors, String description, String image) {
        super(ID, title, contributors, description, image);
        this.songs = new ArrayList<>();
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




