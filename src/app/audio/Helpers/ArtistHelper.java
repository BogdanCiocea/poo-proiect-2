package app.audio.Helpers;

import app.audio.Collections.Album;
import app.audio.LibraryEntry;

import java.util.List;

public class ArtistHelper extends LibraryEntry {
    /**
     * Constructor for ArtistHelper.
     * @param name name
     * @param albums albums
     */
    public ArtistHelper(final String name, final List<Album> albums) {
        super(name);
        this.albums = albums;
        this.name = name;
    }

    private List<Album> albums;
    private String name;
    /**
     * Setter for albums.
     * @param albums albums
     */
    public void setAlbums(final List<Album> albums) {
        this.albums = albums;
    }

    /**
     * Setter for name.
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Getter for albums.
     * @return albums
     */
    public List<Album> getAlbums() {
        return albums;
    }
    /**
     * Getter for name.
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }
}
