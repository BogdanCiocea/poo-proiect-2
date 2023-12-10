package fileio.input;

import java.util.ArrayList;

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

    /**
     * Gets the next page
     * @return nextPage
     */
    public String getNextPage() {
        return nextPage;
    }

    /**
     * Gets the price
     * @return price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * Gets the songs
     * @return songs
     */
    public ArrayList<SongInput> getSongs() {
        return songs;
    }
    /**
     * Gets the date
     * @return date
     */
    public String getDate() {
        return date;
    }
    private ArrayList<EpisodeInput> episodes;
    /**
     * Gets the episodes
     * @return episodes
     */
    public ArrayList<EpisodeInput> getEpisodes() {
        return episodes;
    }
    /**
     * Gets the description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Gets the release year
     * @return releaseYear
     */
    public Integer getReleaseYear() {
        return releaseYear;
    }
    /**
     * Gets the name
     * @return name
     */
    public String getName() {
        return name;
    }
    private Integer age;

    private String city;
    /**
     * Gets the city
     * @return city
     */
    public String getCity() {
        return city;
    }
    /**
     * Gets the age
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Constructor
     */
    public CommandInput() {
    }
    /**
     * Gets the type
     * @return type
     */
    public String getType() {
        return type;
    }
    /**
     * Sets the type
     * @param type type
     */
    public void setType(final String type) {
        this.type = type;
    }

    /**
     * Gets the command
     * @return command
     */
    public String getCommand() {
        return command;
    }
    /**
     * Sets the command
     * @param command command
     */
    public void setCommand(final String command) {
        this.command = command;
    }
    /**
     * Gets the username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Sets the username
     * @param username username
     */
    public void setUsername(final String username) {
        this.username = username;
    }
    /**
     * Gets the timestamp
     * @return timestamp
     */
    public Integer getTimestamp() {
        return timestamp;
    }
    /**
     * Sets the timestamp
     * @param timestamp timestamp
     */
    public void setTimestamp(final Integer timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * Gets the filters
     * @return filters
     */
    public FiltersInput getFilters() {
        return filters;
    }
    /**
     * Sets the filters
     * @param filters filters
     */
    public void setFilters(final FiltersInput filters) {
        this.filters = filters;
    }
    /**
     * Gets the item number
     * @return itemNumber
     */
    public Integer getItemNumber() {
        return itemNumber;
    }
    /**
     * Sets the item number
     * @param itemNumber itemNumber
     */
    public void setItemNumber(final Integer itemNumber) {
        this.itemNumber = itemNumber;
    }
    /**
     * Gets the repeat mode
     * @return repeatMode
     */
    public Integer getRepeatMode() {
        return repeatMode;
    }
    /**
     * Sets the repeat mode
     * @param repeatMode repeatMode
     */
    public void setRepeatMode(final Integer repeatMode) {
        this.repeatMode = repeatMode;
    }
    /**
     * Gets the playlist id
     * @return playlistId
     */
    public Integer getPlaylistId() {
        return playlistId;
    }
    /**
     * Sets the playlist id
     * @param playlistId playlistId
     */
    public void setPlaylistId(final Integer playlistId) {
        this.playlistId = playlistId;
    }
    /**
     * Gets the playlist name
     * @return playlistName
     */
    public String getPlaylistName() {
        return playlistName;
    }
    /**
     * Sets the playlist name
     * @param playlistName playlistName
     */
    public void setPlaylistName(final String playlistName) {
        this.playlistName = playlistName;
    }
    /**
     * Gets the seed
     * @return seed
     */
    public Integer getSeed() {
        return seed;
    }
    /**
     * Sets the seed
     * @param seed seed
     */
    public void setSeed(final Integer seed) {
        this.seed = seed;
    }

    /**
     * toString method
     * @return string
     */
    @Override
    public String toString() {
        return "CommandInput{"
                + "command='" + command + '\''
                + ", username='" + username
                + '\'' + ", timestamp="
                + timestamp + ", type='"
                + type + '\'' + ", filters="
                + filters + ", itemNumber="
                + itemNumber + ", repeatMode="
                + repeatMode + ", playlistId="
                + playlistId + ", playlistName='"
                + playlistName + '\'' + ", seed="
                + seed + '}';
    }
}
