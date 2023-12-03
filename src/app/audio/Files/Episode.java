package app.audio.Files;

import lombok.Getter;

@Getter
public final class Episode extends AudioFile {
    private final String description;

    public String getDescription() {
        return description;
    }

    public Episode(String name, Integer duration, String description) {
        super(name, duration);
        this.description = description;
    }
}
