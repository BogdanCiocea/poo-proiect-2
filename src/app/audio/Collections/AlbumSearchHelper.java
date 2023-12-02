package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;

import java.util.List;

public class AlbumSearchHelper extends AudioCollection {
    private String name;
    private String owner;
    private String description;

    private List<Song> songs;

    public AlbumSearchHelper(String name, String owner, String description, List<Song> songs) {
        super(name, owner);
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.songs = songs;
    }

    public List<Song> getSongs() {
        return songs;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return songs.get(index);
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
