package objects;

import java.util.ArrayList;
import java.util.List;

public class Content {
    private int ID;
    private String title;
    private List<Artist> contributors;
    private int duration;
    private String description;
    private int likeCount;
    private Img image;

    public Content(int ID, String title, List<Artist> contributors, String description, Img image) {
        this.ID = ID;
        this.title = title;
        this.contributors = contributors;
        this.description = description;
        this.likeCount = 0;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getContributors() {
        return contributors;
    }

    public void addContributor(String contributor) {
        contributors.add(contributor);
    }

    public void removeContributor(String contributor) {
        contributors.remove(contributor);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void incrementLikeCount() {
        likeCount++;
    }

    public void decrementLikeCount() {
        if (likeCount > 0) {
            likeCount--;
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

