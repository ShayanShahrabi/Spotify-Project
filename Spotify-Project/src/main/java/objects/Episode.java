package objects;

import java.util.List;

public class Episode {
    private int episodeID;
    private String title;
    private Img image;

    private String description;
    private int duration;

    public Episode(int episodeID, String title, String description, int duration, Img image) {
        this.episodeID = episodeID;
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.image = image;
    }

    public int getEpisodeID() {
        return episodeID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }
}