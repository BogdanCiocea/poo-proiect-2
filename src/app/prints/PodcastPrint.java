package app.prints;

import java.util.List;

public class PodcastPrint {
    private String name;
    private List<String> episodes;
    /**
     * Constructor for PodcastPrint.
     * @param name name
     * @param episodes episodes
     */
    public PodcastPrint(final String name, final List<String> episodes) {
        this.name = name;
        this.episodes = episodes;
    }
    /**
     * Getter for name.
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Setter for name.
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Getter for episodes.
     * @return episodes
     */
    public List<String> getEpisodes() {
        return episodes;
    }
    /**
     * Setter for episodes.
     * @param episodes episodes
     */
    public void setEpisodes(final List<String> episodes) {
        this.episodes = episodes;
    }
    /**
     * toString method for PodcastPrint.
     * @return String
     */
    @Override
    public String toString() {
        return name + ":\n\t" + episodes + "\n";
    }
}
