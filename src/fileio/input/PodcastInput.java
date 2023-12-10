package fileio.input;

import java.util.ArrayList;

public final class PodcastInput {
    private String name;
    private String owner;
    private ArrayList<EpisodeInput> episodes;
    /**
     * Constructor for PodcastInput
     * @param name name
     * @param owner owner
     * @param episodes episodes
     */
    public PodcastInput(final String name, final String owner,
                        final ArrayList<EpisodeInput> episodes) {
        this.name = name;
        this.owner = owner;
        this.episodes = episodes;
    }
    /**
     * Constructor for PodcastInput
     */
    public PodcastInput() {
    }
    /**
     * Getter for name
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Setter for name
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Getter for owner
     * @return owner
     */
    public String getOwner() {
        return owner;
    }
    /**
     * Setter for an owner
     * @param owner owner
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    /**
     * Getter for episodes
     * @return episodes
     */
    public ArrayList<EpisodeInput> getEpisodes() {
        return episodes;
    }
    /**
     * Setter for episodes
     * @param episodes episodes
     */
    public void setEpisodes(final ArrayList<EpisodeInput> episodes) {
        this.episodes = episodes;
    }

    /**
     * toString method
     * @return string
     */
    @Override
    public String toString() {
        return "PodcastInput{" + "name='" + name + '\'' + ", owner='" + owner + '\''
                + ", episodes=" + episodes + '}';
    }
}
