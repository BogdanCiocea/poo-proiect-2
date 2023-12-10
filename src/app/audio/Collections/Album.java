package app.audio.Collections;

import app.audio.Files.Song;

import java.util.List;

public class Album {
    private List<Song> songs;
    private String name;
    private Integer releaseYear;
    private String description;
    private String owner;

    /**
     * Returns the total number of likes for all songs in the album.
     *
     * @return the total number of likes for all songs in the album.
     */
    public Integer getTotalLikes() {
        Integer likes = 0;
        for (Song song : songs) {
            likes += song.getLikes();
        }
        return likes;
    }

    /**
     * Returns owner of the album.
     * @return owner of the album.
     */
    public String getOwner() {
        return owner;
    }
    /**
     * Sets owner of the album.
     * @param owner owner of the album.
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     * Sets songs of the album.
     * @param songs songs of the album.
     */
    public void setSongs(final List<Song> songs) {
        this.songs = songs;
    }

    /**
     * Gets the name of the album.
     * @return the name of the album.
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the album.
     * @param name the name of the album.
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Gets the release year of the album.
     * @return the release year of the album.
     */
    public Integer getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the release year of the album.
     * @param releaseYear the release year of the album.
     */
    public void setReleaseYear(final Integer releaseYear) {
        this.releaseYear = releaseYear;
    }
    /**
     * Gets the description of the album.
     * @return the description of the album.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description of the album.
     * @param description the description of the album.
     */
    public void setDescription(final String description) {
        this.description = description;
    }
    /**
     * Gets the songs of the album.
     * @return the songs of the album.
     */
    public List<Song> getSongs() {
        return songs;
    }
    /**
     * Creates an album.
     * @param name the name of the album.
     * @param owner the owner of the album.
     * @param description the description of the album.
     * @param releaseYear the release year of the album.
     * @param songs the songs of the album.
     */
    public Album(final String name, final String owner, final String description,
                final Integer releaseYear, final List<Song> songs) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.releaseYear = releaseYear;
        this.songs = songs;
    }
}
