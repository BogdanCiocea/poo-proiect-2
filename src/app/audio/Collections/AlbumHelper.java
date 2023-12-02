package app.audio.Collections;

import java.util.ArrayList;
import java.util.List;

public class AlbumHelper {
    private String name;
    private List<String> songs = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getSongs() {
        return songs;
    }

    public void setSongs(List<String> songs) {
        this.songs = songs;
    }
}
