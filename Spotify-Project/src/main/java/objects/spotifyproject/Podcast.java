package objects.spotifyproject;

import java.util.ArrayList;
import java.util.List;

public class Podcast extends Content {
    private List<Episode> episodes;

    public Podcast(int ID, String title, List<Artist> contributors, String description, String image) {
        super(ID, title, contributors, description, image);
        this.episodes = new ArrayList<>();
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void addEpisode(Episode episode) {
        episodes.add(episode);
        setDuration(getDuration() + episode.getDuration());
    }

    public void removeEpisode(Episode episode) {
        episodes.remove(episode);
        setDuration(getDuration() - episode.getDuration());
    }
}

