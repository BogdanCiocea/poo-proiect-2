package app.audio;

import lombok.Getter;

import java.util.ArrayList;

import static java.lang.Character.getName;

@Getter
public abstract class LibraryEntry {
    private final String name;
    private String artist;
    private String owner;

    public String getOwner() {
        return owner;
    }

    public String getArtist() { return artist; }

    public String getName() {
        return name;
    }

    public LibraryEntry(String name) {
        this.name = name;
    }

    public boolean matchesName(String name) {
        if (getName() != null)
            return getName().toLowerCase().startsWith(name.toLowerCase());
        return false;
    }
    public boolean matchesAlbum(String album) { return false; }
    public boolean matchesTags(ArrayList<String> tags) { return false; }
    public boolean matchesLyrics(String lyrics) { return false; }
    public boolean matchesGenre(String genre) { return false; }
    public boolean matchesArtist(String artist) {
        return getArtist().equalsIgnoreCase(artist);
    }
    public boolean matchesReleaseYear(String releaseYear) { return false; }
    public boolean matchesOwner(String owner) {
        return getOwner().equalsIgnoreCase(owner);
    }
    public boolean isVisibleToUser(String user) { return false; }
    public boolean matchesFollowers(String followers) { return false; }

    public boolean matchesDescription(String description) { return  false; }
}
