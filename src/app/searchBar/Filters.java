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

    public String getDescription() {
        return description;
    }

    public String getAlbum() {
        return album;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getGenre() {
        return genre;
    }

    public String getReleaseYear() {
        return releaseYear;
    }

    public String getArtist() {
        return artist;
    }

    public String getOwner() {
        return owner;
    }

    public String getFollowers() {
        return followers;
    }

    private String releaseYear;
    private String artist;
    private String owner;
    private String followers;
    public Filters(FiltersInput filters) {
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

    public String getName() {
        return this.name;
    }
}
