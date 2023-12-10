package app.searchBar;

import fileio.input.FiltersInput;
import lombok.Data;

import java.util.ArrayList;

@Data
public class Filters {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;

    private String description;

    /**
     * Gets the description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the album
     * @return album
     */
    public String getAlbum() {
        return album;
    }
    /**
     * Gets the tags
     * @return tags
     */
    public ArrayList<String> getTags() {
        return tags;
    }
    /**
     * Gets the lyrics
     * @return lyrics
     */
    public String getLyrics() {
        return lyrics;
    }
    /**
     * Gets the genre
     * @return genre
     */
    public String getGenre() {
        return genre;
    }
    /**
     * Gets the release year
     * @return release year
     */
    public String getReleaseYear() {
        return releaseYear;
    }
    /**
     * Gets the artist
     * @return artist
     */
    public String getArtist() {
        return artist;
    }
    /**
     * Gets the owner
     * @return owner
     */
    public String getOwner() {
        return owner;
    }
    /**
     * Gets the followers
     * @return followers
     */
    public String getFollowers() {
        return followers;
    }

    private String releaseYear;
    private String artist;
    private String owner;
    private String followers;
    /**
     * Constructor
     * @param filters filters
     */
    public Filters(final FiltersInput filters) {
        this.name = filters.getName();
        this.album = filters.getAlbum();
        this.tags = filters.getTags();
        this.lyrics = filters.getLyrics();
        this.genre = filters.getGenre();
        this.releaseYear = filters.getReleaseYear();
        this.artist = filters.getArtist();
        this.owner = filters.getOwner();
        this.followers = filters.getFollowers();
        this.description = filters.getDescription();
    }
    /**
     * Gets the name
     * @return name
     */
    public String getName() {
        return this.name;
    }
}
