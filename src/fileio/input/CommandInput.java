package fileio.input;

import app.audio.Files.Episode;
import app.audio.Files.Song;

import java.util.ArrayList;
import java.util.List;

public final class CommandInput {
    private String command;
    private String username;
    private Integer timestamp;
    private String type; // song / playlist / podcast
    private FiltersInput filters; // pentru search
    private Integer itemNumber; // pentru select
    private Integer repeatMode; // pentru repeat
    private Integer playlistId; // pentru add/remove song
    private String playlistName; // pentru create playlist
    private Integer seed; // pentru shuffle
    private String name;
    private Integer releaseYear;
    private String description;
    private ArrayList<SongInput> songs;
    private String date;
    private Integer price;
    private String nextPage;

    public String getNextPage() {
        return nextPage;
    }

    public Integer getPrice() {
        return price;
    }

    public ArrayList<SongInput> getSongs() {
        return songs;
    }
    public String getDate() {
        return date;
    }
    private ArrayList<EpisodeInput> episodes;

    public ArrayList<EpisodeInput> getEpisodes() {
        return episodes;
    }

    public String getDescription() {
        return description;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public String getName() {
        return name;
    }

    private Integer age;

    private String city;

    public String getCity() {
        return city;
    }

    public Integer getAge() {
        return age;
    }

    public CommandInput() {
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public FiltersInput getFilters() {
        return filters;
    }

    public void setFilters(FiltersInput filters) {
        this.filters = filters;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public Integer getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(Integer repeatMode) {
        this.repeatMode = repeatMode;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public Integer getSeed() {
        return seed;
    }

    public void setSeed(Integer seed) {
        this.seed = seed;
    }

    @Override
    public String toString() {
        return "CommandInput{" +
                "command='" + command + '\'' +
                ", username='" + username + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", filters=" + filters +
                ", itemNumber=" + itemNumber +
                ", repeatMode=" + repeatMode +
                ", playlistId=" + playlistId +
                ", playlistName='" + playlistName + '\'' +
                ", seed=" + seed +
                '}';
    }
}
