package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.audio.LibraryEntry;

import java.util.List;

public class ArtistHelper extends LibraryEntry {

    public ArtistHelper(String name, List<Album> albums) {
        super(name);
        this.albums = albums;
        this.name = name;
    }

    private List<Album> albums;
    private String name;
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    @Override
    public String getName() {
        return name;
    }
}
