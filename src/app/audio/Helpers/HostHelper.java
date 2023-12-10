package app.audio.Helpers;

import app.audio.Collections.Podcast;
import app.audio.LibraryEntry;

import java.util.List;

public class HostHelper extends LibraryEntry {
    private String name;
    private List<Podcast> podcasts;
    //private String announcements;
    /**
     * Constructor for HostHelper
     * @param name name
     * @param podcasts podcasts
     */
    public HostHelper(final String name, final List<Podcast> podcasts) {
        super(name);
        this.name = name;
        this.podcasts = podcasts;
    }
    /**
     * Getter for podcasts
     * @return podcasts
     */
    public List<Podcast> getPodcasts() {
        return podcasts;
    }
    /**
     * Getter for name
     * @return name
     */
    @Override
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
     * Setter for podcasts
     * @param podcasts podcasts
     */
    public void setPodcasts(final List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }
}
