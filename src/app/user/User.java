package app.user;

import app.Admin;
import app.announcement.Announcement;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.event.Event;
import app.merch.Merch;
import app.player.Player;
import app.player.PlayerStats;
import app.searchBar.Filters;
import app.searchBar.SearchBar;
import app.utils.Enums;
import checker.CheckerConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import fileio.input.CommandInput;
import fileio.input.LibraryInput;
import fileio.input.SongInput;
import lombok.Getter;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class User extends AudioCollection {
    @Getter
    private String username;
    @Getter
    private int age;
    @Getter
    private String city;
    @Getter
    private ArrayList<Playlist> playlists;
    @Getter
    private ArrayList<Song> likedSongs;
    @Getter
    private ArrayList<Playlist> followedPlaylists;
    private final Player player;
    private final SearchBar searchBar;
    private boolean lastSearched;
    private List<Event> events = new ArrayList<>();
    private String currentPage = "Home";
    private List<Merch> merches = new ArrayList<>();
    private List<Podcast> podcasts = new ArrayList<>();
    private final List<Announcement> announcements = new ArrayList<>();
    private String lastPageMessage;
    private String lastUserSelected;
    private boolean onlineStatus = true;

    public static final int MIN_YEAR = 1900;
    public static final int MAX_YEAR = 2023;
    public static final int MAX_DAY = 28;

    /**
     * Sets the online status of the user
     * @param onlineStatus onlineStatus
     */
    public void setOnlineStatus(final boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    /**
     * Gets the last user selected
     * @return lastUserSelected
     */
    public String getLastUserSelected() {
        return lastUserSelected;
    }
    /**
     * Sets the last user selected
     * @param lastUserSelected lastUserSelected
     */
    public void setLastUserSelected(final String lastUserSelected) {
        this.lastUserSelected = lastUserSelected;
    }

    /**
     * Gets the last page message
     * @return lastPageMessage
     */
    public String getLastPageMessage() {
        return lastPageMessage;
    }

    /**
     * Sets the last page message
     * @param lastPageMessage lastPageMessage
     */
    public void setLastPageMessage(final String lastPageMessage) {
        this.lastPageMessage = lastPageMessage;
    }

    /**
     * Gets the announcements
     * @return announcements
     */
    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    /**
     * Gets the podcasts
     * @return podcasts
     */
    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    /**
     * Gets the merches
     * @return merches
     */
    public List<Merch> getMerches() {
        return merches;
    }

    /**
     * Sets the merches
     * @param merches merches
     */
    public void setMerches(final List<Merch> merches) {
        this.merches = merches;
    }

    /**
     * Gets the events
     * @return events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Sets the events
     * @param events events
     */
    public void setEvents(final List<Event> events) {
        this.events = events;
    }

    /**
     * Gets the current page
     * @return currentPage
     */
    public String getCurrentPage() {
        return currentPage;
    }

    /**
     * Sets the current page
     * @param currentPage currentPage
     */
    public void setCurrentPage(final String currentPage) {
        this.currentPage = currentPage;
    }

    private final ArrayList<Album> albums = new ArrayList<>();

    /**
     * Gets the album with the given name
     * @param albumName albumName
     * @return album
     */
    public Album getAlbum(final String albumName) {
        for (Album album : this.getAlbums()) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }

    /**
     * Gets the albums
     * @return albums
     */
    public ArrayList<Album> getAlbums() {
        return albums;
    }

    private String type;
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
     * Gets the online status
     * @return onlineStatus
     */
    public boolean getOnlineStatus() {
        return onlineStatus;
    }

    /**
     * Switches the connection status
     * @return message
     */
    public String switchConnectionStatus() {
        if (this.getType() != null && (this.getType().equals("artist")
                || this.getType().equals("host"))) {
            return this.getUsername() + " is not a normal user.";
        }
        onlineStatus = !onlineStatus;
        return username + " has changed status successfully.";
    }
    /**
     * Gets the username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Gets the age
     * @return age
     */
    public int getAge() {
        return age;
    }
    /**
     * Gets the city
     * @return city
     */
    public String getCity() {
        return city;
    }
    /**
     * Gets the playlists
     * @return playlists
     */
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
    /**
     * Gets the liked songs
     * @return likedSongs
     */
    public ArrayList<Song> getLikedSongs() {
        return likedSongs;
    }
    /**
     * Gets the followed playlists
     * @return followedPlaylists
     */
    public ArrayList<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }
    /**
     * Gets the player
     * @return player
     */
    public Player getPlayer() {
        return player;
    }
    /**
     * Gets the searchBar
     * @return searchBar
     */
    public SearchBar getSearchBar() {
        return searchBar;
    }

    /**
     * Constructor for the user
     * @param username username
     * @param age age
     * @param city city
     */
    public User(final String username, final int age, final String city) {
        super(null, null);
        this.username = username;
        this.age = age;
        this.city = city;
        playlists = new ArrayList<>();
        likedSongs = new ArrayList<>();
        followedPlaylists = new ArrayList<>();
        player = new Player();
        searchBar = new SearchBar(username);
        lastSearched = false;
    }

    /**
     * Searches for a given type of source
     * @param filters filters
     * @param type type
     * @return results
     */
    public ArrayList<String> search(final Filters filters, final String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;

        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }

        return results;
    }
    /**
     * Selects a source
     * @param itemNumber itemNumber
     * @return message
     */
    public String select(final int itemNumber) {
        if (!lastSearched) {
            return "Please conduct a search before making a selection.";
        }
        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null) {
            return "The selected ID is too high.";
        }
        Admin adminInstance = Admin.getInstance();
        for (User user : adminInstance.getUsers()) {
            if (user.getUsername().equals(selected.getName())
                    && (user.getType().equals("artist") || user.getType().equals("host"))) {
                return "Successfully selected %s's page.".formatted(selected.getName());
            }
        }
        return "Successfully selected %s.".formatted(selected.getName());
    }

    /**
     * Loads a source
     * @return message
     */
    public String load() {
        if (searchBar.getLastSelected() == null) {
            return "Please select a source before attempting to load.";
        }
        if (!searchBar.getLastSearchType().equals("song")
                && ((AudioCollection) searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    /**
     * Plays or pauses the current audio file
     * @return message
     */
    public String playPause() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to pause or resume playback.";
        }
        player.pause();

        if (player.getPaused()) {
            return "Playback paused successfully.";
        } else {
            return "Playback resumed successfully.";
        }
    }

    /**
     * Repeats the current audio file
     * @return message
     */
    public String repeat() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before setting the repeat status.";
        }
        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch (repeatMode) {
            case NO_REPEAT:
                repeatStatus = "no repeat";
                break;
            case REPEAT_ONCE:
                repeatStatus = "repeat once";
                break;
            case REPEAT_ALL:
                repeatStatus = "repeat all";
                break;
            case REPEAT_INFINITE:
                repeatStatus = "repeat infinite";
                break;
            case REPEAT_CURRENT_SONG:
                repeatStatus = "repeat current song";
                break;
            default:
                repeatStatus = ""; // or some default value
                break;
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    /**
     * Shuffles the current playlist or album
     * @param seed seed
     * @return message
     */
    public String shuffle(final Integer seed) {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before using the shuffle function.";
        }
        if (!player.getType().equals("playlist") && !player.getType().equals("album")) {
            return "The loaded source is not a playlist or an album.";
        }
        player.shuffle(seed);

        if (player.getShuffle()) {
            return "Shuffle function activated successfully.";
        }
        return "Shuffle function deactivated successfully.";
    }

    /**
     * Skips forward to the next track
     * @return message
     */
    public String forward() {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before attempting to forward.";
        }
        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }
        player.skipNext();

        return "Skipped forward successfully.";
    }

    /**
     * Adds a new announcement
     * @param name name
     * @param description description
     * @return message
     */
    public String addAnnouncement(final String name, final String description) {
        String message = this.getUsername() + " has successfully added new announcement.";

        if (this.getType() == null || !this.getType().equals("host")) {
            return this.getUsername() + " is not a host.";
        }
        for (Announcement announcement : this.getAnnouncements()) {
            if (announcement.getName().equals(name)) {
                return this.getUsername() + "  has already added an announcement with this name.";
            }
        }
        this.getAnnouncements().add(new Announcement(name, description));

        return message;
    }

    /**
     * Removes an announcement
     * @param name name
     * @return message
     */
    public String removeAnnouncement(final String name) {
        if (this.getType() != null && !this.getType().equals("host")) {
            return this.getUsername() + " is not a host.";
        }
        for (Announcement announcement : this.getAnnouncements()) {
            if (announcement.getName().equals(name)) {
                this.getAnnouncements().remove(announcement);
                return this.getUsername() + " has successfully deleted the announcement.";
            }
        }
        return this.getUsername() + " has no announcement with the given name.";
    }

    /**
     * Skips backward to the previous track
     * @return message
     */
    public String backward() {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please select a source before rewinding.";
        }
        if (!player.getType().equals("podcast")) {
            return "The loaded source is not a podcast.";
        }
        player.skipPrev();

        return "Rewound successfully.";
    }

    /**
     * Likes or unlikes the current song
     * @return message
     */
    public String like() {
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before liking or unliking.";
        }

        if (!player.getType().equals("song") && !player.getType().equals("playlist")
                && !player.getType().equals("album")) {
            return "Loaded source is not a song.";
        }

        Song song = (Song) player.getCurrentAudioFile();

        if (likedSongs.contains(song)) {
            likedSongs.remove(song);
            song.dislike();

            return "Unlike registered successfully.";
        }

        likedSongs.add(song);
        song.like();
        return "Like registered successfully.";
    }

    /**
     * Goes to the next track
     * @return message
     */
    public String next() {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }
        player.next();

        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before skipping to the next track.";
        }
        return ("Skipped to next track successfully. "
                + "The current track is %s.").formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Goes to the previous track
     * @return message
     */
    public String prev() {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before returning to the previous track.";
        }
        player.prev();

        return ("Returned to previous track successfully. "
                + "The current track is %s.").formatted(player.getCurrentAudioFile().getName());
    }

    /**
     * Creates a new playlist
     * @param name name
     * @param timestamp timestamp
     * @return message
     */
    public String createPlaylist(final String name, final int timestamp) {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name))) {
            return "A playlist with the same name already exists.";
        }

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    /**
     * Adds or removes a song from a playlist
     * @param id id
     * @return message
     */
    public String addRemoveInPlaylist(final int id) {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        if (player.getCurrentAudioFile() == null) {
            return "Please load a source before adding to or removing from the playlist.";
        }
        if (player.getType().equals("podcast")) {
            return "The loaded source is not a song.";
        }
        if (id > playlists.size()) {
            return "The specified playlist does not exist.";
        }
        Playlist playlist = playlists.get(id - 1);

        if (playlist.containsSong((Song) player.getCurrentAudioFile())) {
            playlist.removeSong((Song) player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song) player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    /**
     * Switches the visibility of a playlist
     * @param playlistId playlistId
     * @return message
     */
    public String switchPlaylistVisibility(final Integer playlistId) {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        if (playlistId > playlists.size()) {
            return "The specified playlist ID is too high.";
        }
        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    /**
     * Shows the playlists
     * @return playlistOutputs
     */
    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    /**
     * Follow or unfollow a playlist
     * @return message
     */
    public String follow() {
        if (!this.getOnlineStatus()) {
            return this.getUsername() + " is offline.";
        }
        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null) {
            return "Please select a source before following or unfollowing.";
        }
        if (!type.equals("playlist")) {
            return "The selected source is not a playlist.";
        }
        Playlist playlist = (Playlist) selection;

        if (playlist.getOwner().equals(username)) {
            return "You cannot follow or unfollow your own playlist.";
        }
        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    /**
     * Gets the player stats
     * @return playerStats
     */
    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    /**
     * Shows the liked songs
     * @return results
     */
    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    /**
     * Gets the number of album likes
     * @return numberOfLikes
     */
    public Integer getNumberOfAlbumLikes() {
        if (!this.getType().equals("artist")) {
            return -1;
        }

        Integer numberOfLikes = 0;
        for (Album album : this.getAlbums()) {
            for (Song song : album.getSongs()) {
                numberOfLikes += song.getLikes();
            }
        }

        return numberOfLikes;
    }

    /**
     * Gets the preferred genre
     * @return preferredGenre
     */
    public String getPreferredGenre() {
        String[] genres = {"pop", "rock", "rap"};
        int[] counts = new int[genres.length];
        int mostLikedIndex = -1;
        int mostLikedCount = 0;

        for (Song song : likedSongs) {
            for (int i = 0; i < genres.length; i++) {
                if (song.getGenre().equals(genres[i])) {
                    counts[i]++;
                    if (counts[i] > mostLikedCount) {
                        mostLikedCount = counts[i];
                        mostLikedIndex = i;
                    }
                    break;
                }
            }
        }

        String preferredGenre = mostLikedIndex != -1 ? genres[mostLikedIndex] : "unknown";
        return "This user's preferred genre is %s.".formatted(preferredGenre);
    }

    /**
     * Adds an album
     * @param commandInput commandInput
     * @return message
     */
    public String addAlbum(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        if (!this.getType().equals("artist")) {
            return this.getUsername() + " is not an artist.";
        }
        if (this.getAlbum(commandInput.getName()) != null) {
            return commandInput.getUsername() + " has another album with the same name.";
        }
        HashSet<String> uniqueSongs = new HashSet<>();
        for (SongInput songInput : commandInput.getSongs()) {
            if (!uniqueSongs.add(songInput.getName())) {
                return commandInput.getUsername()
                        + " has the same song at least twice in this album.";
            }
        }

        Album album = new Album(commandInput.getName(), commandInput.getUsername(),
                commandInput.getDescription(), commandInput.getReleaseYear(),
                adminInstance.addSongs(commandInput.getSongs()));

        this.getAlbums().add(album);

        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library;
        try {
            library = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH
                    + "library/library.json"), LibraryInput.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (SongInput songInput : commandInput.getSongs()) {
            library.getSongs().add(songInput);
        }

        if (adminInstance.getArtist(album.getOwner()) != null) {
            Objects.requireNonNull(adminInstance.getArtist(album.getOwner())).getAlbums().add(album);
        }
        AlbumSearchHelper albumSearchHelper = new AlbumSearchHelper(album.getName(),
                album.getOwner(), album.getDescription(), album.getSongs());
        adminInstance.getAlbumSearchHelpers().add(albumSearchHelper);

        adminInstance.setSongList(commandInput.getSongs());
        adminInstance.getAlbums().add(album);

        Set<Song> newSongs = new LinkedHashSet<>(adminInstance.getSongs());
        for (Song song : newSongs) {
            if (!adminInstance.getSongs().contains(song)) {
                adminInstance.getSongs().add(song);
            }
        }

        return commandInput.getUsername() + " has added new album successfully.";
    }

    /**
     * Adds an event
     * @param commandInput commandInput
     * @return message
     */
    public String addEvent(final CommandInput commandInput) {
        if (this.getType() == null || !getType().equals("artist")) {
            return commandInput.getUsername() + " is not an artist.";
        }

        String dateString = commandInput.getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate date;
        try {
            date = LocalDate.parse(dateString, formatter);
        } catch (DateTimeParseException e) {
            return "Event for " + commandInput.getUsername() + " does not have a valid date.";
        }

        // Check for invalid date conditions
        if (date.getYear() < MIN_YEAR || date.getYear() > MAX_YEAR
                || date.getMonthValue() == 2 && date.getDayOfMonth() > MAX_DAY) {
            return "Event for " + commandInput.getUsername() + " does not have a valid date.";
        }

        // Check for event name uniqueness
        for (Event existingEvent : this.getEvents()) {
            if (existingEvent.getName().equals(commandInput.getName())) {
                return commandInput.getUsername() + " has another event with the same name.";
            }
        }

        Event event = new Event(commandInput.getName(), commandInput.getDescription(), dateString);
        this.getEvents().add(event);

        return commandInput.getUsername() + " has added new event successfully.";
    }

    /**
     * Removes an event
     * @param eventName eventName
     * @return message
     */
    public String removeEvent(final String eventName) {
        if (this.getType() != null && !this.getType().equals("artist")) {
            return this.getUsername() + " is not an artist.";
        }

        int ok = 0;
        for (Event event : this.getEvents()) {
            if (event.getName().equals(eventName)) {
                ok = 1;
                break;
            }
        }

        if (ok == 0) {
            return this.getUsername() + " doesn't have an event with the given name.";
        }
        this.getEvents().removeIf(event -> event.getName().equals(eventName));

        return this.getUsername() + " deleted the event successfully.";
    }

    /**
     * Delete a user
     * @return message
     */
    public String deleteUser() {
        String message = getUsername() + " was successfully deleted.";
        Admin adminInstance = Admin.getInstance();
        if (this.getType() != null && this.getType().equals("host")) {
            for (User user : adminInstance.getUsers()) {
                if (user.getCurrentPage().equals("Host")) {
                    return getUsername() + " can't be deleted.";
                }
                for (Podcast podcast : this.getPodcasts()) {
                    for (Episode episode : podcast.getEpisodes()) {
                        if (user.getPlayer().getSource() != null
                                && (user.getPlayer().getCurrentAudioFile()
                                .getName().equals(episode.getName()))) {
                            return getUsername() + " can't be deleted.";
                        }
                    }
                }
            }

            for (Podcast podcast : adminInstance.getPodcasts()) {
                if (podcast.getOwner().equals(this.getUsername())) {
                    adminInstance.getPodcastsList().remove(podcast);
                }
            }
            adminInstance.getHosts().remove(this);
        } else if (this.getType() != null && this.getType().equals("artist")) {
            for (User user : adminInstance.getUsers()) {
                if (user.getCurrentPage().equals("Artist")) {
                    return getUsername() + " can't be deleted.";
                }
                for (Album album : this.getAlbums()) {
                    for (Song song : album.getSongs()) {
                        if (user.getPlayer().getCurrentAudioFile() != null
                                && user.getPlayer().getCurrentAudioFile()
                                .getName().equals(song.getName())) {
                            return getUsername() + " can't be deleted.";
                        }
                    }
                }
            }

            List<Song> songsToRemove = new ArrayList<>();
            for (Song song : adminInstance.getSongs()) {
                if (song.getArtist().equals(this.getUsername())) {
                    adminInstance.getSongsList().remove(song);
                    songsToRemove.add(song);
                }
            }
            for (User user : adminInstance.getUsers()) {
                user.getLikedSongs().removeAll(songsToRemove);
            }
            adminInstance.getAlbums().removeAll(this.getAlbums());
            adminInstance.getArtists().remove(this);
            for (ArtistHelper artistHelper : adminInstance.getArtistHelpers()) {
                if (artistHelper.getName().equals(this.getUsername())) {
                    adminInstance.getArtistHelpers().remove(artistHelper);
                    break;
                }
            }
            for (AlbumSearchHelper albumSearchHelper : adminInstance.getAlbumSearchHelpers()) {
                if (albumSearchHelper.getOwner().equals(this.getUsername())) {
                    adminInstance.getAlbumSearchHelpers().remove(albumSearchHelper);
                    break;
                }
            }
        } else if (this.getType() == null || this.getType().equals("user")) {
            for (User user : adminInstance.getUsers()) {
                for (Playlist playlist : this.getPlaylists()) {
                    for (Song song : playlist.getSongs()) {
                        if (user.getPlayer().getCurrentAudioFile() != null
                                && user.getPlayer().getCurrentAudioFile()
                                .getName().equals(song.getName())) {
                            return this.getUsername() + " can't be deleted.";
                        }
                    }
                }
            }

            for (Song song : adminInstance.getSongs()) {
                if (this.getLikedSongs().contains(song)) {
                    song.dislike();
                    this.getLikedSongs().remove(song);
                }
            }

            for (User user : adminInstance.getUsers()) {
                for (Playlist playlist : user.getPlaylists()) {
                    if (this.getFollowedPlaylists().contains(playlist)) {
                        playlist.decreaseFollowers();
                    }
                }
            }

            for (User user : adminInstance.getUsers()) {
                for (Playlist playlist : this.getPlaylists()) {
                    user.getFollowedPlaylists().remove(playlist);
                }
            }
            adminInstance.getPlaylists().removeAll(this.getPlaylists());
            adminInstance.getUsers().remove(this);
        }

        return message;
    }

    /**
     * Gets the playlist with the given name
     * @param playlistName playlistName
     * @return playlist
     */
    public Playlist getPlaylist(final String playlistName) {
        for (Playlist playlist : this.getPlaylists()) {
            if (playlist.getName().equals(playlistName)) {
                return playlist;
            }
        }
        return null;
    }

    /**
     * Removes an album
     * @param username username
     * @param albumName albumName
     * @return message
     */
    public String removeAlbum(final String username, final String albumName) {
        Admin adminInstance = Admin.getInstance();
        User artist = adminInstance.getUser(username);
        assert artist != null;
        if (!artist.getType().equals("artist")) {
            return artist.getUsername() + " is not an artist.";
        }
        int ok = 0;
        for (Album album : artist.getAlbums()) {
            if (album.getName().equals(albumName)) {
                ok = 1;
                break;
            }
        }

        if (ok == 0) {
            return artist.getUsername() + " doesn't have an album with the given name.";
        }
        for (User user : adminInstance.getUsers()) {
            for (Album album : artist.getAlbums()) {
                for (Song song : album.getSongs()) {
                    if (user.getPlayer().getCurrentAudioFile() != null
                            && song.getName().equals(user.getPlayer()
                            .getCurrentAudioFile().getName())) {
                        return artist.getUsername() + " can't delete this album.";
                    }
                }
            }
        }

        for (User user : adminInstance.getUsers()) {
            for (Playlist playlist : user.getPlaylists()) {
                for (Song song : artist.getAlbum(albumName).getSongs()) {
                    for (Song song1 : playlist.getSongs()) {
                        if (song1.getName().equals(song.getName())) {
                            return artist.getUsername() + " can't delete this album.";
                        }
                    }
                }
            }
        }

        for (User user : adminInstance.getUsers()) {
            user.getLikedSongs().removeIf(song1 -> song1.matchesAlbum(albumName));
        }
        for (Song song : adminInstance.getSongs()) {
            if (song.getAlbum().equals(albumName)) {
                adminInstance.getSongsList().remove(song);
            }
        }
        adminInstance.getAlbums().remove(artist.getAlbum(albumName));
        artist.getAlbums().remove(artist.getAlbum(albumName));

        return artist.getUsername() + " deleted the album successfully.";
    }

    /**
     * Adds a merch item
     * @param commandInput commandInput
     * @return message
     */
    public String addMerch(final CommandInput commandInput) {
        String message;
        if (!this.getType().equals("artist")) {
            return commandInput.getUsername() + " is not an artist.";
        }
        if (this.getMerches() != null) {
            for (Merch merch : this.getMerches()) {
                if (merch.getName().equals(commandInput.getName())) {
                    return commandInput.getUsername() + " has merchandise with the same name.";
                }
            }
        }
        if (commandInput.getPrice() < 0) {
            return "Price for merchandise can not be negative.";
        }
        message = this.getUsername() + " has added new merchandise successfully.";
        Merch merch = new Merch(commandInput.getName(),
                commandInput.getDescription(), commandInput.getPrice());
        this.getMerches().add(merch);
        return message;
    }

    /**
     * Adds a podcast
     * @param commandInput commandInput
     * @return message
     */
    public String addPodcast(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        String message = this.getUsername() + " has added new podcast successfully.";
        int ok = 1;
        if (this.getType() == null || !this.getType().equals("host")) {
            message = this.getUsername() + " is not a host.";
            ok = 0;
        }
        for (Podcast podcast : this.getPodcasts()) {
            if (podcast.getName().equals(commandInput.getName())) {
                ok = 0;
                message = this.getUsername() + " has another podcast with the same name.";
                break;
            }
        }

        List<Episode> newEpisodes = commandInput.getEpisodes().stream()
                .map(e -> new Episode(e.getName(), e.getDuration(), e.getDescription()))
                .toList();

        for (Podcast podcast : this.getPodcasts()) {
            if (podcast.getEpisodes().equals(newEpisodes)) {
                return this.getUsername() + " has the same episode in this podcast.";
            }
        }

        Podcast newPodcast = new Podcast(commandInput.getName(),
                commandInput.getUsername(), newEpisodes);
//        if (!Admin.getPodcasts().contains(newPodcast))
        if (ok == 1 && !adminInstance.getPodcasts().contains(newPodcast)) {
            adminInstance.getAdminPodcasts().add(newPodcast);
        }

        if (ok == 1) {
            podcasts.add(newPodcast);
        }
        return message;
    }

    /**
     * Removes a podcast
     * @param commandInput commandInput
     * @return message
     */
    public String removePodcast(final CommandInput commandInput) {
        String message = this.getUsername() + " deleted the podcast successfully.";
        if (!this.getType().equals("host")) {
            message = this.getUsername() + " is not a host.";
        }
        int ok = 0;
        for (Podcast podcast : this.getPodcasts()) {
            if (podcast.getName().equals(commandInput.getName())) {
                ok = 1;
                break;
            }
        }

        if (ok == 0) {
            return this.getUsername() + " doesn't have a podcast with the given name.";
        }
        Admin adminInstance = Admin.getInstance();
        for (User user : adminInstance.getUsers()) {
            if (user.getPlayer().getSource() != null
                    && user.getPlayer().getSource().getAudioCollection() != null
                    && user.getPlayer().getSource().getAudioCollection().getName()
                    .equals(commandInput.getName()) && !user.getPlayer().getPaused()) {
                return commandInput.getUsername() + " can't delete this podcast.";
            }
        }

        for (Podcast podcast : adminInstance.getPodcasts()) {
            if (podcast.getName().equals(commandInput.getName())) {
                adminInstance.getPodcastsList().remove(podcast);
            }
        }
        this.getPodcasts().removeIf(podcast -> podcast.getName().equals(commandInput.getName()));

        return message;
    }

    /**
     * Simulates time of a player
     * @param time time
     */
    public void simulateTime(final int time) {
        if (this.onlineStatus)
            player.simulatePlayer(time);
    }
    /**
     * Gets the number of songs
     * @return numberOfSongs
     */
    @Override
    public int getNumberOfTracks() {
        return 0;
    }
    /**
     * Gets the track by index
     * @param index index
     * @return track
     */
    @Override
    public AudioFile getTrackByIndex(final int index) {
        return null;
    }
}
