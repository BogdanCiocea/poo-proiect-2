package app.audio.Helpers;

import java.util.ArrayList;
import java.util.List;

public class AlbumHelper {
    private String name;
    private List<String> songs = new ArrayList<>();

    /**
     * Gets the name of the album
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the album
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Gets the songs of the album
     * @return songs
     */
    public List<String> getSongs() {
        return songs;
    }
    /**
     * Sets the songs of the album
     * @param songs songs
     */
    public void setSongs(final List<String> songs) {
        this.songs = songs;
    }
}
