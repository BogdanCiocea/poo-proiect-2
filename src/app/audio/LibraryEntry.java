package app.audio;

import lombok.Getter;

import java.util.ArrayList;

import static java.lang.Character.getName;

@Getter
public abstract class LibraryEntry {
    private final String name;
    private String artist;
    private String owner;

    /**
     * Gets the owner of the library entry
     * @return owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the artist of the library entry
     * @return artist
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Gets the name of the library entry
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Constructor for LibraryEntry
     * @param name name
     */
    public LibraryEntry(final String name) {
        this.name = name;
    }

    /**
     * Matches the name of the library entry
     * @param name name
     * @return true if the name matches
     */
    public boolean matchesName(final String name) {
        return getName().toLowerCase().startsWith(name.toLowerCase());
    }

    /**
     * Matches the album of the library entry
     * @param album album
     * @return true if the album matches
     */
    public boolean matchesAlbum(final String album) {
        return false;
    }
    /**
     * Matches the tags of the library entry
     * @param tags tags
     * @return true if the tags match
     */
    public boolean matchesTags(final ArrayList<String> tags) {
        return false;
    }
    /**
     * Matches the lyrics of the library entry
     * @param lyrics lyrics
     * @return true if the lyrics match
     */
    public boolean matchesLyrics(final String lyrics) {
        return false;
    }
    /**
     * Matches the genre of the library entry
     * @param genre genre
     * @return true if the genre matches
     */
    public boolean matchesGenre(final String genre) {
        return false;
    }
    /**
     * Matches the artist of the library entry
     * @param artist artist
     * @return true if the artist matches
     */
    public boolean matchesArtist(final String artist) {
        return getArtist().equalsIgnoreCase(artist);
    }
    /**
     * Matches the release year of the library entry
     * @param releaseYear release year
     * @return true if the release year matches
     */
    public boolean matchesReleaseYear(final String releaseYear) {
        return false;
    }
    /**
     * Matches the owner of the library entry
     * @param owner owner
     * @return true if the owner matches
     */
    public boolean matchesOwner(final String owner) {
        return getOwner().equalsIgnoreCase(owner);
    }
    /**
     * Matches the visibility of the library entry
     * @param user user
     * @return true if the visibility matches
     */
    public boolean isVisibleToUser(final String user) {
        return false;
    }
    /**
     * Matches the followers of the library entry
     * @param followers followers
     * @return true if the followers match
     */
    public boolean matchesFollowers(final String followers) {
        return false;
    }
    /**
     * Matches the description of the library entry
     * @param description description
     * @return true if the description matches
     */
    public boolean matchesDescription(final String description) {
        return  false;
    }
}
