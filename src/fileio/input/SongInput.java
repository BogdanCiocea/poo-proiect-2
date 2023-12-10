package fileio.input;

import java.util.ArrayList;

public final class SongInput {
    private String name;
    private Integer duration;
    private String album;
    private ArrayList<String> tags;
    private String lyrics;
    private String genre;
    private Integer releaseYear;
    private String artist;

    /**
     * Constructor for SongInput
     */
    public SongInput() {
    }

    /**
     * Gets the name of the song
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the song
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Gets the duration of the song
     * @return duration
     */
    public Integer getDuration() {
        return duration;
    }
    /**
     * Sets the duration of the song
     * @param duration duration
     */
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }
    /**
     * Gets the album of the song
     * @return album
     */
    public String getAlbum() {
        return album;
    }
    /**
     * Sets the album of the song
     * @param album album
     */
    public void setAlbum(final String album) {
        this.album = album;
    }
    /**
     * Gets the tags of the song
     * @return tags
     */
    public ArrayList<String> getTags() {
        return tags;
    }
    /**
     * Sets the tags of the song
     * @param tags tags
     */
    public void setTags(final ArrayList<String> tags) {
        this.tags = tags;
    }
    /**
     * Gets the lyrics of the song
     * @return lyrics
     */
    public String getLyrics() {
        return lyrics;
    }
    /**
     * Sets the lyrics of the song
     * @param lyrics lyrics
     */
    public void setLyrics(final String lyrics) {
        this.lyrics = lyrics;
    }
    /**
     * Gets the genre of the song
     * @return genre
     */
    public String getGenre() {
        return genre;
    }
    /**
     * Sets the genre of the song
     * @param genre genre
     */
    public void setGenre(final String genre) {
        this.genre = genre;
    }
    /**
     * Gets the release year of the song
     * @return release year
     */
    public int getReleaseYear() {
        return releaseYear;
    }
    /**
     * Sets the release year of the song
     * @param releaseYear release year
     */
    public void setReleaseYear(final int releaseYear) {
        this.releaseYear = releaseYear;
    }
    /**
     * Gets the artist of the song
     * @return artist
     */
    public String getArtist() {
        return artist;
    }
    /**
     * Sets the artist of the song
     * @param artist artist
     */
    public void setArtist(final String artist) {
        this.artist = artist;
    }

    /**
     * toString method
     * @return string
     */
    @Override
    public String toString() {
        return "SongInput{"
                + "name='" + name + '\'' + ", duration=" + duration + ", album='" + album + '\''
                + ", tags=" + tags + ", lyrics='" + lyrics + '\'' + ", genre='" + genre + '\''
                + ", releaseYear='" + releaseYear + '\'' + ", artist='" + artist + '\'' + '}';
    }
}
