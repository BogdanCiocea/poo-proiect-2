package app.audio.Helpers;

import java.util.ArrayList;
import java.util.List;

public class PodcastHelper {
    private String name;
    private List<String> episodes = new ArrayList<>();

    /**
     * Gets the name of the podcast
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the podcast
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Gets the episodes of the podcast
     * @return episodes
     */
    public List<String> getEpisodes() {
        return episodes;
    }
    /**
     * Sets the episodes of the podcast
     * @param episodes episodes
     */
    public void setEpisodes(final List<String> episodes) {
        this.episodes = episodes;
    }
}
