package fileio.input;

import java.util.ArrayList;

public class FiltersInput {
    private String name;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private String releaseYear; // pentru search song/episode -> releaseYear
    private String artist;
    private String owner; // pentru search playlist si podcast
    private String followers; // pentru search playlist -> followers
    private String description;
    /**
     * Gets the description of the library entry
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description of the library entry
     * @param description description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Constructor for FiltersInput
     */
    public FiltersInput() {
    }

    /**
     * Gets the name of the library entry
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the library entry
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Gets the album of the library entry
     * @return album
     */
    public String getAlbum() {
        return album;
    }
    /**
     * Sets the album of the library entry
     * @param album album
     */
    public void setAlbum(final String album) {
        this.album = album;
    }
    /**
     * Gets the tags of the library entry
     * @return tags
     */
    public ArrayList<String> getTags() {
        return tags;
    }
    /**
     * Sets the tags of the library entry
     * @param tags tags
     */
    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }
    /**
     * Gets the lyrics of the library entry
     * @return lyrics
     */
    public String getLyrics() {
        return lyrics;
    }
    /**
     * Sets the lyrics of the library entry
     * @param lyrics lyrics
     */
    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }
    /**
     * Gets the genre of the library entry
     * @return genre
     */
    public String getGenre() {
        return genre;
    }
    /**
     * Sets the genre of the library entry
     * @param genre genre
     */
    public void setGenre(final String genre) {
        this.genre = genre;
    }
    /**
     * Gets the release year of the library entry
     * @return releaseYear
     */
    public String getReleaseYear() {
        return releaseYear;
    }
    /**
     * Sets the release year of the library entry
     * @param releaseYear releaseYear
     */
    public void setReleaseYear(final String releaseYear) {
        this.releaseYear = releaseYear;
    }
    /**
     * Gets the artist of the library entry
     * @return artist
     */
    public String getArtist() {
        return artist;
    }
    /**
     * Sets the artist of the library entry
     * @param artist artist
     */
    public void setArtist(final String artist) {
        this.artist = artist;
    }
    /**
     * Gets the owner of the library entry
     * @return owner
     */
    public String getOwner() {
        return owner;
    }
    /**
     * Sets the owner of the library entry
     * @param owner owner
     */
    public void setOwner(final String owner) {
        this.owner = owner;
    }
    /**
     * Gets the followers of the library entry
     * @return followers
     */
    public String getFollowers() {
        return followers;
    }
    /**
     * Sets the followers of the library entry
     * @param followers followers
     */
    public void setFollowers(final String followers) {
        this.followers = followers;
    }

    /**
     * Overriding toString method
     * @return string
     */
    @Override
    public String toString() {
        return "FilterInput{"
                + ", name='" + name + '\''
                + ", album='" + album + '\''
                + ", tags=" + tags
                + ", lyrics='" + lyrics + '\''
                + ", genre='" + genre + '\''
                + ", releaseYear='" + releaseYear + '\''
                + ", artist='" + artist + '\''
                + ", owner='" + owner + '\''
                + ", followers='" + followers + '\''
                + '}';
    }
}
