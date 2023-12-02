package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import fileio.input.SongInput;

import java.util.ArrayList;
import java.util.List;

public class Album {
    private List<Song> songs = new ArrayList<>();
    private String name;
    private Integer releaseYear;
    private String description = null;
    private String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public Album(String name, String owner, String description, Integer releaseYear, List<Song> songs) {
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.releaseYear = releaseYear;
        this.songs = songs;
    }
}
