package objects;

import java.util.ArrayList;
import java.util.List;

public class Podcast extends Content {
    private List<Episode> episodes;

    public Podcast(int ID, String title, List<String> contributors, int duration, String description, int likeCount, List<Episode> episodes) {
        super(ID, title, contributors, duration, description, likeCount);
        this.episodes = episodes;
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

