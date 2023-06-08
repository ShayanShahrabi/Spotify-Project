package objects;

import java.util.List;

public class Content {
    private int ID;
    private String title;
    private List<String> contributors;
    private int duration;
    private String description;
    private int likeCount;

    public Content(int ID, String title, List<String> contributors, int duration, String description, int likeCount) {
        this.ID = ID;
        this.title = title;
        this.contributors = contributors;
        this.duration = duration;
        this.description = description;
        this.likeCount = likeCount;
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

}

