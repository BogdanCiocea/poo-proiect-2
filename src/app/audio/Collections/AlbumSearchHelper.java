package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;

import java.util.List;

public class AlbumSearchHelper extends AudioCollection {
    private String name;
    private String owner;
    private String description;

    private List<Song> songs;
    /**
     * Constructor for AlbumSearchHelper.
     * @param name name
     * @param owner owner
     * @param description description
     * @param songs songs
     */
    public AlbumSearchHelper(final String name, final String owner,
                             final String description, final List<Song> songs) {
        super(name, owner);
        this.name = name;
        this.owner = owner;
        this.description = description;
        this.songs = songs;
    }
    /**
     * Getter for songs.
     * @return songs
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * Getter for name.
     * @return name
     */
    @Override
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
     * Getter for an owner.
     * @return owner
     */
    public String getOwner() {
        return owner;
    }
    /**
     * Getter for number of tracks.
     * @return number of tracks
     */
    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }
    /**
     * Getter for track by index.
     * @param index index
     * @return track by index
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }
    /**
     * Setter for owner.
     * @param owner owner
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }

    /**
     * Getter for description.
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setter for description.
     * @param description description
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}
