package app.audio.Collections;

import java.util.ArrayList;
import java.util.List;

public class PodcastHelper {
    private String name;
    private List<String> episodes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<String> episodes) {
        this.episodes = episodes;
    }
}
