package objects.spotifyproject;

import java.util.ArrayList;
import java.util.List;

public class Album extends Content {
    private List<Song> songs;

    public Album(int ID, String title, List<Artist> contributors, String description, String image) {
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
