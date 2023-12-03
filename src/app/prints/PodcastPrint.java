package app.prints;

import java.util.List;

public class PodcastPrint {
    private String name;
    private List<String> episodes;

    public PodcastPrint(String name, List<String> episodes) {
        this.name = name;
        this.episodes = episodes;
    }

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

    @Override
    public String toString() {
        return name + ":\n\t" + episodes + "\n";
    }
}
