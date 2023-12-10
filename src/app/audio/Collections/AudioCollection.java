package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioCollection extends LibraryEntry {
    private final String owner;
    /**
     * Getter for owner
     * @return owner
     */
    public String getOwner() {
        return owner;
    }
    /**
     * Constructor for AudioCollection
     * @param name name
     * @param owner owner
     */
    public AudioCollection(final String name, final String owner) {
        super(name);
        this.owner = owner;
    }
    /**
     * Getter for number of tracks
     * @return number of tracks
     */
    public abstract int getNumberOfTracks();
    /**
     * Getter for track by index
     * @param index index
     * @return track by index
     */
    public abstract AudioFile getTrackByIndex(int index);

    /**
     * Checks if the user matches the owner
     * @param user user
     * @return true if the user matches the owner
     */
    @Override
    public boolean matchesOwner(final String user) {
        return this.getOwner().equals(user);
    }
}
