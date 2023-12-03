package app.user;

import app.Admin;
import app.CommandRunner;
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
import fileio.input.*;
import fileio.output.AlbumResult;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class User extends AudioCollection{
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
    private List<Announcement> announcements = new ArrayList<>();
    private String lastPageMessage;
    private String lastUserSelected;

    public String getLastUserSelected() {
        return lastUserSelected;
    }

    public void setLastUserSelected(String lastUserSelected) {
        this.lastUserSelected = lastUserSelected;
    }

    public String getLastPageMessage() {
        return lastPageMessage;
    }

    public void setLastPageMessage(String lastPageMessage) {
        this.lastPageMessage = lastPageMessage;
    }

    public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public List<Podcast> getPodcasts() {
        return podcasts;
    }

    public void setPodcasts(List<Podcast> podcasts) {
        this.podcasts = podcasts;
    }

    public List<Merch> getMerches() {
        return merches;
    }

    public void setMerches(List<Merch> merches) {
        this.merches = merches;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    private final ArrayList<Album> albums = new ArrayList<>();

    public Album getAlbum(String albumName) {
            for (Album album : albums) {
                if (album.getName().equals(albumName))
                    return album;
            }
        return null;
    }

    public ArrayList<Album> getAlbums() {
        return albums;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private boolean onlineStatus = true;

    public boolean getOnlineStatus() {
        return onlineStatus;
    }

    public String switchConnectionStatus() {
        onlineStatus = !onlineStatus;
        player.pause();
        return username + " has changed status successfully.";
    }

    public String getUsername() {
        return username;
    }

    public int getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public ArrayList<Song> getLikedSongs() {
        return likedSongs;
    }

    public ArrayList<Playlist> getFollowedPlaylists() {
        return followedPlaylists;
    }

    public Player getPlayer() {
        return player;
    }

    public SearchBar getSearchBar() {
        return searchBar;
    }

    public boolean isLastSearched() {
        return lastSearched;
    }
    public User(String username, int age, String city) {
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

    public ArrayList<String> search(Filters filters, String type) {
        searchBar.clearSelection();
        player.stop();

        lastSearched = true;

        ArrayList<String> results = new ArrayList<>();
        List<LibraryEntry> libraryEntries = searchBar.search(filters, type);

        for (LibraryEntry libraryEntry : libraryEntries) {
            results.add(libraryEntry.getName());
        }

        if (type.equals("artist") || type.equals("host")) {
            Set<String> uniqueResults = new LinkedHashSet<>(results);
            results = new ArrayList<>(uniqueResults);
        }

        return results;
    }

    public String select(int itemNumber) {
        if (!lastSearched)
            return "Please conduct a search before making a selection.";

        lastSearched = false;

        LibraryEntry selected = searchBar.select(itemNumber);

        if (selected == null)
            return "The selected ID is too high.";
        for (User user : Admin.getUsers()) {
            if (user.getUsername().equals(selected.getName()) && (user.getType().equals("artist") || user.getType().equals("host"))) {
                return "Successfully selected %s's page.".formatted(selected.getName());
            }
        }
        return "Successfully selected %s.".formatted(selected.getName());
    }

    public String load() {
        if (searchBar.getLastSelected() == null)
            return "Please select a source before attempting to load.";

        if (!searchBar.getLastSearchType().equals("song") && ((AudioCollection)searchBar.getLastSelected()).getNumberOfTracks() == 0) {
            return "You can't load an empty audio collection!";
        }

        player.setSource(searchBar.getLastSelected(), searchBar.getLastSearchType());
        searchBar.clearSelection();

        player.pause();

        return "Playback loaded successfully.";
    }

    public String playPause() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before attempting to pause or resume playback.";

        player.pause();

        if (player.getPaused())
            return "Playback paused successfully.";
        else
            return "Playback resumed successfully.";
    }


    public String repeat() {
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before setting the repeat status.";

        Enums.RepeatMode repeatMode = player.repeat();
        String repeatStatus = "";

        switch(repeatMode) {
            case NO_REPEAT -> repeatStatus = "no repeat";
            case REPEAT_ONCE -> repeatStatus = "repeat once";
            case REPEAT_ALL -> repeatStatus = "repeat all";
            case REPEAT_INFINITE -> repeatStatus = "repeat infinite";
            case REPEAT_CURRENT_SONG -> repeatStatus = "repeat current song";
        }

        return "Repeat mode changed to %s.".formatted(repeatStatus);
    }

    public String shuffle(Integer seed) {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before using the shuffle function.";

        if (!player.getType().equals("playlist"))
            return "The loaded source is not a playlist or an album.";

        player.shuffle(seed);

        if (player.getShuffle())
            return "Shuffle function activated successfully.";
        return "Shuffle function deactivated successfully.";
    }

    public String forward() {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";
        if (player.getCurrentAudioFile() == null)
            return "Please load a source before attempting to forward.";

        if (!player.getType().equals("podcast"))
            return "The loaded source is not a podcast.";

        player.skipNext();

        return "Skipped forward successfully.";
    }

    public String addAnnouncement(String name, String description) {
        String message = this.getUsername() + " has successfully added new announcement.";

        if (this.getType() == null || !this.getType().equals("host"))
            return this.getUsername() + " is not a host.";

        for (Announcement announcement : this.getAnnouncements())
            if (announcement.getName().equals(name))
                return this.getUsername() + "  has already added an announcement with this name.";

        this.getAnnouncements().add(new Announcement(name, description));

        return message;
    }

    public String removeAnnouncement(String name) {
        if (this.getType() != null && !this.getType().equals("host"))
            return this.getUsername() + " is not a host.";

        for (Announcement announcement : this.getAnnouncements())
            if (announcement.getName().equals(name)) {
                this.getAnnouncements().remove(announcement);
                return this.getUsername() + " has successfully deleted the announcement.";
            }

        return this.getUsername() + " has no announcement with the given name.";
    }

    public String backward() {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";
        if (player.getCurrentAudioFile() == null)
            return "Please select a source before rewinding.";

        if (!player.getType().equals("podcast"))
            return "The loaded source is not a podcast.";

        player.skipPrev();

        return "Rewound successfully.";
    }

    public String like() {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before liking or unliking.";

        if (!player.getType().equals("song") && !player.getType().equals("playlist"))
            return "Loaded source is not a song.";

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

    public String next() {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before skipping to the next track.";

        player.next();

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before skipping to the next track.";

        return "Skipped to next track successfully. The current track is %s.".formatted(player.getCurrentAudioFile().getName());
    }

    public String prev() {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before returning to the previous track.";

        player.prev();

        return "Returned to previous track successfully. The current track is %s.".formatted(player.getCurrentAudioFile().getName());
    }

    public String createPlaylist(String name, int timestamp) {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";

        if (playlists.stream().anyMatch(playlist -> playlist.getName().equals(name)))
            return "A playlist with the same name already exists.";

        playlists.add(new Playlist(name, username, timestamp));

        return "Playlist created successfully.";
    }

    public String addRemoveInPlaylist(int Id) {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";

        if (player.getCurrentAudioFile() == null)
            return "Please load a source before adding to or removing from the playlist.";

        if (player.getType().equals("podcast"))
            return "The loaded source is not a song.";

        if (Id > playlists.size())
            return "The specified playlist does not exist.";

        Playlist playlist = playlists.get(Id - 1);

        if (playlist.containsSong((Song)player.getCurrentAudioFile())) {
            playlist.removeSong((Song)player.getCurrentAudioFile());
            return "Successfully removed from playlist.";
        }

        playlist.addSong((Song)player.getCurrentAudioFile());
        return "Successfully added to playlist.";
    }

    public String switchPlaylistVisibility(Integer playlistId) {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";

        if (playlistId > playlists.size())
            return "The specified playlist ID is too high.";

        Playlist playlist = playlists.get(playlistId - 1);
        playlist.switchVisibility();

        if (playlist.getVisibility() == Enums.Visibility.PUBLIC) {
            return "Visibility status updated successfully to public.";
        }

        return "Visibility status updated successfully to private.";
    }

    public ArrayList<PlaylistOutput> showPlaylists() {
        ArrayList<PlaylistOutput> playlistOutputs = new ArrayList<>();
        for (Playlist playlist : playlists) {
            playlistOutputs.add(new PlaylistOutput(playlist));
        }

        return playlistOutputs;
    }

    public String follow() {
        if (!this.getOnlineStatus())
            return this.getUsername() + " is offline.";

        LibraryEntry selection = searchBar.getLastSelected();
        String type = searchBar.getLastSearchType();

        if (selection == null)
            return "Please select a source before following or unfollowing.";

        if (!type.equals("playlist"))
            return "The selected source is not a playlist.";

        Playlist playlist = (Playlist)selection;

        if (playlist.getOwner().equals(username))
            return "You cannot follow or unfollow your own playlist.";

        if (followedPlaylists.contains(playlist)) {
            followedPlaylists.remove(playlist);
            playlist.decreaseFollowers();

            return "Playlist unfollowed successfully.";
        }

        followedPlaylists.add(playlist);
        playlist.increaseFollowers();


        return "Playlist followed successfully.";
    }

    public PlayerStats getPlayerStats() {
        return player.getStats();
    }

    public ArrayList<String> showPreferredSongs() {
        ArrayList<String> results = new ArrayList<>();
        for (AudioFile audioFile : likedSongs) {
            results.add(audioFile.getName());
        }

        return results;
    }

    public Integer getNumberOfAlbumLikes() {
        if (!this.getType().equals("artist"))
            return -1;

        Integer numberOfLikes = 0;
        for (Album album : this.getAlbums())
            for (Song song : album.getSongs())
                numberOfLikes += song.getLikes();

        return numberOfLikes;
    }

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

    public String addAlbum(CommandInput commandInput) {
        if (!this.getType().equals("artist"))
            return this.getUsername() + " is not an artist.";

        if (this.getAlbum(commandInput.getName()) != null)
            return commandInput.getUsername() + " has another album with the same name.";

        HashSet<String> uniqueSongs = new HashSet<>();
        for (SongInput songInput : commandInput.getSongs()) {
            if (!uniqueSongs.add(songInput.getName())) {
                return commandInput.getUsername() + " has the same song at least twice in this album.";
            }
        }

        Album album = new Album(commandInput.getName(), commandInput.getUsername(), commandInput.getDescription(), commandInput.getReleaseYear(), Admin.addSongs(commandInput.getSongs()));

        this.getAlbums().add(album);

        ObjectMapper objectMapper = new ObjectMapper();
        LibraryInput library;
        try {
            library = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + "library/library.json"), LibraryInput.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (SongInput songInput : commandInput.getSongs()) {
            library.getSongs().add(songInput);
        }

        if (Admin.getArtist(album.getOwner()) != null)
            Admin.getArtist(album.getOwner()).getAlbums().add(album);

        AlbumSearchHelper albumSearchHelper = new AlbumSearchHelper(album.getName(), album.getOwner(), album.getDescription(), album.getSongs());
        Admin.getAlbumSearchHelpers().add(albumSearchHelper);

        Admin.setSongList(commandInput.getSongs());
        Admin.getAlbums().add(album);

        Set<Song> newSongs = new LinkedHashSet<>(Admin.getSongs());
        for (Song song : newSongs)
            if (!Admin.getSongs().contains(song))
                Admin.getSongs().add(song);

        return commandInput.getUsername() + " has added new album successfully.";
    }


    public String addEvent(CommandInput commandInput) {
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
        if (date.getYear() < 1900 || date.getYear() > 2023 || date.getMonthValue() == 2 && date.getDayOfMonth() > 28) {
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

    public String removeEvent(String eventName) {
        if (this.getType() != null && !this.getType().equals("artist"))
            return this.getUsername() + " is not an artist.";

        int ok = 0;
        for (Event event : this.getEvents())
            if (event.getName().equals(eventName)) {
                ok = 1;
                break;
            }

        if (ok == 0)
            return this.getUsername() + " doesn't have an event with the given name.";

        this.getEvents().removeIf(event -> event.getName().equals(eventName));

        return this.getUsername() + " deleted the event successfully.";
    }

    public String deleteUser() {
        String message = getUsername() + " was successfully deleted.";

        if (this.getType() != null && this.getType().equals("host")) {
            for (User user : Admin.getUsers()) {
                if (user.getCurrentPage().equals("Host"))
                    return getUsername() + " can't be deleted.";
                for (Podcast podcast : this.getPodcasts())
                    for (Episode episode : podcast.getEpisodes()) {
                        if (user.getPlayer().getSource() != null && (user.getPlayer().getCurrentAudioFile().getName().equals(episode.getName())))
                            return getUsername() + " can't be deleted.";
                    }
            }
            Admin.getHosts().remove(this);
            for (Podcast podcast : Admin.getPodcasts())
                if (podcast.getOwner().equals(this.getUsername()))
                    Admin.getPodcastsList().remove(podcast);
        }

        if (this.getType() != null && this.getType().equals("artist")) {
            for (User user : Admin.getUsers()) {
                if (user.getCurrentPage().equals("Artist"))
                    return getUsername() + " can't be deleted.";
                for (Album album : this.getAlbums())
                    for (Song song : album.getSongs())
                        if (user.getPlayer().getCurrentAudioFile() != null && user.getPlayer().getCurrentAudioFile().getName().equals(song.getName()))
                            return getUsername() + " can't be deleted.";
            }

            if (this.getType() != null && this.getType().equals("artist")) {
                List<Song> songsToRemove = new ArrayList<>();
                for (Song song : Admin.getSongs()) {
                    if (song.getArtist().equals(this.getUsername())) {
                        Admin.getSongsList().remove(song);
                        songsToRemove.add(song);
                    }
                }
                for (User user : Admin.getUsers()) {
                    for (Song song : songsToRemove)
                        user.getLikedSongs().remove(song);
                }
                Admin.getSongsList().removeAll(songsToRemove);
                Admin.getArtists().remove(this);
            }
        }

        if (this.getType() != null && this.getType().equals("user")) {
            for (User user : Admin.getUsers()) {
                for (Playlist playlist : this.getPlaylists()) {
                    for (Song song : playlist.getSongs()) {
                        if (user.getPlayer().getCurrentAudioFile() != null && user.getPlayer().getCurrentAudioFile().getName().equals(song.getName()))
                            return this.getUsername() + " can't be deleted.";
                    }
                }
            }
        }

        for (User user : Admin.getUsers()) {
            for (Playlist playlist : user.getPlaylists()) {
                if (this.getFollowedPlaylists().contains(playlist))
                    playlist.decreaseFollowers();
            }
        }

        for (User user : Admin.getUsers()) {
            for (Playlist playlist : this.getPlaylists())
                user.getFollowedPlaylists().remove(playlist);
        }
        Admin.getPlaylists().removeAll(this.getPlaylists());

        return message;
    }

    public String removeAlbum(String username, String albumName) {
        User artist = Admin.getUser(username);
        assert artist != null;
        if (!artist.getType().equals("artist"))
            return artist.getUsername() + " is not an artist.";

        int ok = 0;
        for (Album album : artist.getAlbums())
            if (album.getName().equals(albumName)) {
                ok = 1;
                break;
            }

        if (ok == 0)
            return artist.getUsername() + " doesn't have an album with the given name.";

        for (User user : Admin.getUsers()) {
            for (Album album : artist.getAlbums()) {
                for (Song song : album.getSongs())
                    if (user.getPlayer().getCurrentAudioFile() != null && song.getName().equals(user.getPlayer().getCurrentAudioFile().getName()))
                        return artist.getUsername() + " can't delete this album.";
            }
        }

        return artist.getUsername() + " deleted the album successfully.";
    }

    public String addMerch(CommandInput commandInput) {
        String message = null;
        if (!this.getType().equals("artist"))
            return commandInput.getUsername() + " is not an artist.";
        if (this.getMerches() != null) {
            for (Merch merch : this.getMerches()) {
                if (merch.getName().equals(commandInput.getName())) {
                    return commandInput.getUsername() + " has merchandise with the same name.";
                }
            }
        }
        if (commandInput.getPrice() < 0)
            return  "Price for merchandise can not be negative.";
        message = this.getUsername() + " has added new merchandise successfully.";
        Merch merch = new Merch(commandInput.getName(), commandInput.getDescription(), commandInput.getPrice());
        this.getMerches().add(merch);
        return message;
    }

    public String addPodcast(CommandInput commandInput) {
        String message = this.getUsername() + " has added new podcast successfully.";
        int ok = 1;
        if (this.getType() == null || !this.getType().equals("host")) {
            message = this.getUsername() + " is not a host.";
            ok = 0;
        }
        for (Podcast podcast : this.getPodcasts())
            if (podcast.getName().equals(commandInput.getName())) {
                ok = 0;
                message = this.getUsername() + " has another podcast with the same name.";
                break;
            }

        List<Episode> newEpisodes = commandInput.getEpisodes().stream()
                .map(e -> new Episode(e.getName(), e.getDuration(), e.getDescription()))
                .toList();

        for (Podcast podcast : this.getPodcasts()) {
            if (podcast.getEpisodes().equals(newEpisodes)) {
                return this.getUsername() + " has the same episode in this podcast.";
            }
        }

        Podcast newPodcast = new Podcast(commandInput.getName(), commandInput.getUsername(), newEpisodes);
//        if (!Admin.getPodcasts().contains(newPodcast))
        if (ok == 1 && !Admin.getPodcasts().contains(newPodcast))
            Admin.getAdminPodcasts().add(newPodcast);

        if (ok == 1)
            podcasts.add(newPodcast);
        ok = 1;
        return message;
    }

    public String removePodcast(CommandInput commandInput) {
        String message = this.getUsername() + " deleted the podcast successfully.";
        if (!this.getType().equals("host"))
            message = this.getUsername() + " is not a host.";
        int ok = 0;
        for (Podcast podcast : this.getPodcasts()) {
            if (podcast.getName().equals(commandInput.getName())) {
                ok = 1;
            }
        }

        if (ok == 0) {
            return this.getUsername() + " doesn't have a podcast with the given name.";
        }

        for (User user : Admin.getUsers()) {
            if (user.getPlayer().getSource() != null && user.getPlayer().getSource().getAudioCollection() != null && user.getPlayer().getSource().getAudioCollection().getName().equals(commandInput.getName()) && !user.getPlayer().getPaused()) {
                return commandInput.getUsername() + " can't delete this podcast.";
            }
        }

        for (Podcast podcast : Admin.getPodcasts()) {
            if (podcast.getName().equals(commandInput.getName()))
                Admin.getPodcastsList().remove(podcast);
        }
        this.getPodcasts().removeIf(podcast -> podcast.getName().equals(commandInput.getName()));

        return message;
    }

    public void simulateTime(int time) {
        player.simulatePlayer(time);
    }

    @Override
    public int getNumberOfTracks() {
        return 0;
    }

    @Override
    public AudioFile getTrackByIndex(int index) {
        return null;
    }
}
