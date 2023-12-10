package app.audio.Files;

import app.audio.LibraryEntry;
import lombok.Getter;

@Getter
public abstract class AudioFile extends LibraryEntry {
    private final Integer duration;
    /**
     * Getter for duration
     * @return duration
     */
    public Integer getDuration() {
        return duration;
    }
    /**
     * Constructor for AudioFile
     * @param name name
     * @param duration duration
     */
    public AudioFile(final String name, final Integer duration) {
        super(name);
        this.duration = duration;
    }
}
