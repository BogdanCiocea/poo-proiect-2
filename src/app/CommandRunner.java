package app;

import app.announcement.Announcement;
import app.audio.Collections.Album;
import app.audio.Collections.AlbumHelper;
import app.audio.Collections.ArtistHelper;
import app.audio.Collections.Playlist;
import app.audio.Collections.PodcastHelper;
import app.audio.Collections.HostHelper;
import app.audio.Collections.Podcast;
import app.audio.Collections.PlaylistOutput;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.audio.LibraryEntry;
import app.event.Event;
import app.merch.Merch;
import app.player.PlayerStats;
import app.prints.PodcastPrint;
import app.searchBar.Filters;
import app.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandRunner {
    private static ObjectMapper objectMapper = new ObjectMapper();
    public static final int MAX_SIZE = 5;
    public CommandRunner() {
    }
    /**
     * Gets the object mapper
     * @return the object mapper
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    /**
     * Searches for songs, albums, artists, podcasts, hosts
     * @param commandInput the command input
     * @return the result of the search
     */
    public static ObjectNode search(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());

        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = new ArrayList<>();
        String message;
        if (user != null && user.getOnlineStatus()) {
            results = user.search(filters, type);
            message = "Search returned " + results.size() + " results";
        } else {
            message = commandInput.getUsername() + " is offline.";
        }
        if (!results.isEmpty()) {
            if (type.equals("artist")) {
                user.setCurrentPage("Artist");
            } else if (type.equals("host")) {
                user.setCurrentPage("Host");
            }
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    /**
     * Switches the connection status of a user
     * @param commandInput the command input
     * @return the result of the switch
     */
    public static ObjectNode switchConnectionStatus(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;

        if (adminInstance.getUser(commandInput.getUsername()) != null) {
            assert user != null;
            message = user.switchConnectionStatus();
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Adds a user
     * @param commandInput the command input
     * @return the result of the add
     */
    public static ObjectNode addUser(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User newUser = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (adminInstance.getHosts().isEmpty()) {
            adminInstance.getHostHelpers().clear();
        }
        if (newUser != null) {
            message = "The username " + newUser.getUsername() + " is already taken.";
        } else {
            newUser = new User(commandInput.getUsername(), commandInput.getAge(),
                    commandInput.getCity());
            newUser.setType(commandInput.getType());
            if (newUser.getType().equals("artist")) {
                //newUser.switchConnectionStatus();
                newUser.setOnlineStatus(false);
                //newUser.getPlayer().setPaused(true);
                adminInstance.getArtists().add(newUser);
                ArtistHelper artistHelper = new ArtistHelper(newUser.getUsername(),
                        newUser.getAlbums());
                adminInstance.getArtistHelpers().add(artistHelper);
            } else if (newUser.getType().equals("host")) {
                if (adminInstance.getHosts().isEmpty()) {
                    adminInstance.getHostHelpers().clear();
                }
                newUser.setOnlineStatus(false);
                //newUser.getPlayer().setPaused(true);
                adminInstance.getHosts().add(newUser);
                HostHelper hostHelper = new HostHelper(newUser.getUsername(),
                        newUser.getPodcasts());
                adminInstance.getHostHelpers().add(hostHelper);
            }
            adminInstance.getUsers().add(newUser);
            message = "The username " + newUser.getUsername() + " has been added successfully.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Gets all the users
     * @param commandInput the command input
     * @return the result of the get
     */
    public static ObjectNode getAllUsers(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        List<User> allUsers = adminInstance.getUsers();
        List<User> normalUsers = new ArrayList<>();
        List<User> artists = new ArrayList<>();
        List<User> hosts = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getType() == null || (!user.getType().equals("artist")
                    && !user.getType().equals("host"))) {
                normalUsers.add(user);
            } else if (user.getType() != null && user.getType().equals("artist")) {
                artists.add(user);
            } else {
                hosts.add(user);
            }
        }
        List<String> sortedUsers = new ArrayList<>();
        for (User user : normalUsers) {
            sortedUsers.add(user.getUsername());
        }
        for (User user : artists) {
            sortedUsers.add(user.getUsername());
        }
        for (User user : hosts) {
            sortedUsers.add(user.getUsername());
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(sortedUsers));
        return objectNode;
    }

    /**
     * Removes a podcast
     * @param commandInput the command input
     * @return the result of the remove
     */
    public static ObjectNode removePodcast(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.removePodcast(commandInput);
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Adds an event
     * @param commandInput the command input
     * @return the result of the add
     */
    public static ObjectNode addEvent(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        String message;
        User user = adminInstance.getUser(commandInput.getUsername());
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.addEvent(commandInput);
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Removes an event
     * @param commandInput the command input
     * @return the result of the remove
     */
    public static ObjectNode removeEvent(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        String message;
        User user = adminInstance.getUser(commandInput.getUsername());

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.removeEvent(commandInput.getName());
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Deletes a user
     * @param commandInput the command input
     * @return the result of the delete
     */
    public static ObjectNode deleteUser(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.deleteUser();
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Removes an album
     * @param commandInput the command input
     * @return the result of the remove
     */
    public static ObjectNode removeALbum(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User artist = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (artist == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = artist.removeAlbum(commandInput.getUsername(), commandInput.getName());
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    /**
     * Selects the result of a search
     * @param commandInput the command input
     * @return the result of the select
     */
    public static ObjectNode select(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user != null && user.getOnlineStatus()) {
            message = user.select(commandInput.getItemNumber());
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Gets the online users
     * @param commandInput the command input
     * @return the result of the get
     */
    public static ObjectNode getOnlineUsers(final CommandInput commandInput) {
        List<String> result = new ArrayList<>();
        Admin adminInstance = Admin.getInstance();
        for (User user : adminInstance.getUsers()) {
            if (user.getOnlineStatus()) {
                result.add(user.getUsername());
            }
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(result));

        return objectNode;
    }

    /**
     * Loads a source
     * @param commandInput the command input
     * @return the result of the load
     */
    public static ObjectNode load(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user != null && user.getOnlineStatus()) {
            message = user.load();
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Plays or pauses a source
     * @param commandInput the command input
     * @return the result of the play/pause
     */
    public static ObjectNode playPause(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Repeats a source
     * @param commandInput the command input
     * @return the result of the repeat
     */
    public static ObjectNode repeat(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user != null && user.getOnlineStatus()) {
            message = user.repeat();
        } else {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shuffles a source
     * @param commandInput the command input
     * @return the result of the shuffle
     */
    public static ObjectNode shuffle(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        Integer seed = commandInput.getSeed();
        assert user != null;
        String message = user.shuffle(seed);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Forwards a source
     * @param commandInput the command input
     * @return the result of the forward
     */
    public static ObjectNode forward(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Backwards a source
     * @param commandInput the command input
     * @return the result of the backward
     */
    public static ObjectNode backward(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Likes a song
     * @param commandInput the command input
     * @return the result of the like
     */
    public static ObjectNode like(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user != null && user.getOnlineStatus()) {
            message = user.like();
        } else {
            message = commandInput.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Skips to the next track
     * @param commandInput the command input
     * @return the result of the skip
     */
    public static ObjectNode next(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Skips to the previous track
     * @param commandInput the command input
     * @return the result of the skip
     */
    public static ObjectNode prev(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Creates a playlist
     * @param commandInput the command input
     * @return the result of the creation
     */
    public static ObjectNode createPlaylist(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        if (user != null && user.getOnlineStatus()) {
            String message = user.createPlaylist(commandInput.getPlaylistName(),
                    commandInput.getTimestamp());

            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("command", commandInput.getCommand());
            objectNode.put("user", commandInput.getUsername());
            objectNode.put("timestamp", commandInput.getTimestamp());
            objectNode.put("message", message);

            return objectNode;
        }
        String message = commandInput.getUsername() + " is offline.";
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds an album
     * @param commandInput the command input
     * @return the result of the add
     */
    public static ObjectNode addAlbum(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.addAlbum(commandInput);
        }
        adminInstance.setSongsNew(adminInstance.getSongs());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Changes the page of a user
     * @param commandInput the command input
     * @return the result of the change
     */
    public static ObjectNode changePage(final CommandInput commandInput) {
        // Get the singleton instance of Admin
        Admin adminInstance = Admin.getInstance();

        // Retrieve the user based on the username provided in the command input
        User user = adminInstance.getUser(commandInput.getUsername());

        // Ensure the user object is not null
        assert user != null;

        // Initialize a message indicating the user accessed the new page successfully
        String message = user.getUsername() + " accessed " + commandInput.getNextPage() + " successfully.";

        // Check if the user is trying to access the 'Artist' page but is not an artist
        if (commandInput.getNextPage().equals("Artist") && !user.getType().equals("artist")) {
            // Update the message to indicate the user is trying to access a non-existent page
            message = user.getUsername() + " is trying to access a non-existent page.";
        }

        // Check if the user is trying to access the 'Host' page but is not a host
        if (commandInput.getNextPage().equals("Host") && !user.getType().equals("host")) {
            // Update the message to indicate the user is trying to access a non-existent page
            message = user.getUsername() + " is trying to access a non-existent page.";
        }

        // Check if the user is offline
        if (!user.getOnlineStatus()) {
            // Update the message to indicate the user is offline
            message = user.getUsername() + " is offline.";
        } else {
            // If the user is online, update their current page
            user.setCurrentPage(commandInput.getNextPage());
        }

        // Create an ObjectNode to structure the response data
        ObjectNode objectNode = objectMapper.createObjectNode();

        // Populate the response with the command information
        objectNode.put("command", commandInput.getCommand());

        // Populate the response with the username
        objectNode.put("user", commandInput.getUsername());

        // Populate the response with the timestamp
        objectNode.put("timestamp", commandInput.getTimestamp());

        // Populate the response with the message
        objectNode.put("message", message);

        // Return the structured response
        return objectNode;
    }


    /**
     * Prints the current page of a user
     * @param commandInput the command input
     * @return the result of the print
     */
    public static ObjectNode printCurrentPage(final CommandInput commandInput) {
        // Get the singleton instance of Admin
        Admin adminInstance = Admin.getInstance();

        // Retrieve the user based on the username provided in the command input
        User user = adminInstance.getUser(commandInput.getUsername());

        // Initialize a placeholder message, likely for debugging or as a default
        String message = "mama ta";

        // Ensure the user object is not null
        assert user != null;

        // Retrieve and create a list of songs liked by the user
        List<Song> likedSongs = new ArrayList<>(user.getLikedSongs());

        // Check the online status of the user
        boolean dewIt = user.getOnlineStatus();

        // If the user is online
        if (dewIt) {
            // Sort liked songs by the number of likes
            likedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
            // Get the top liked songs limited by MAX_SIZE
            List<Song> topLikedSongs = likedSongs.stream().limit(MAX_SIZE).toList();
            // List of followed playlists
            List<Playlist> followedPlaylists = new ArrayList<>(user.getFollowedPlaylists());
            // Sort playlists by the number of followers
            followedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers).reversed());

            // Get top followed playlists limited by MAX_SIZE
            List<Playlist> topFollowedPlaylists = followedPlaylists.stream().limit(MAX_SIZE)
                    .collect(Collectors.toList());
            // Initialize list for top songs names
            List<String> topSongs = new ArrayList<>();
            // Loop through top liked songs
            for (Song song : topLikedSongs) {
                // Add song names to the list
                topSongs.add(song.getName());
            }
            // List for liked songs with details
            List<String> likedPageSongs = new ArrayList<>();
            // Loop through top liked songs
            for (Song song : topLikedSongs) {
                // Add song details to the list
                likedPageSongs.add(song.getName() + " - " + song.getArtist());
            }
            // List for followed playlists with details
            List<String> likedPagePlaylists = new ArrayList<>();
            // Loop through top followed playlists
            for (Playlist playlist : topFollowedPlaylists) {
                // Add playlist details to the list
                likedPagePlaylists.add(playlist.getName() + " - " + playlist.getOwner());
            }
            // Check again if user is online
            if (!user.getOnlineStatus()) {
                // Update message for offline status
                message = commandInput.getUsername() + " is offline.";
                // If the current page is Home
            } else if (user.getCurrentPage().equals("Home")) {
                // Format the top songs and playlists for display
                String formattedTopSongs = topSongs.stream()
                        .collect(Collectors.joining(", "));
                String formattedTopPlaylists = topFollowedPlaylists.stream()
                        .map(LibraryEntry::getName).collect(Collectors.joining(", "));

                // Construct the message for the Home page
                message = "Liked songs:\n\t[" + formattedTopSongs + "]\n\nFollowed playlists:\n\t["
                        + formattedTopPlaylists + "]";
                // If the current page is LikedContent
            } else if (user.getCurrentPage().equals("LikedContent")) {
                // Process and display liked songs and playlists
                List<Song> likedS0ngs = user.getLikedSongs();
                List<String> likedSongsString = new ArrayList<>();
                for (Song song : likedS0ngs) {
                    likedSongsString.add(song.getName() + " - " + song.getArtist());
                }
                message = "Liked songs:\n\t" + likedSongsString + "\n\nFollowed playlists:\n\t"
                        + likedPagePlaylists;
                // If on Artist page
            } else if (user.getCurrentPage().equals("Artist") && user.getSearchBar()
                    .getLastSelected() != null) {
                // Get the selected artist's details
                User artist = adminInstance.getUser(user.getSearchBar().getLastSelected()
                        .getName());
                List<String> albums = new ArrayList<>();
                // If artist exists
                if (artist != null) {
                    // Collect album names
                    for (Album album : artist.getAlbums()) {
                        albums.add(album.getName());
                    }
                    // Collect merchandise details
                    List<String> merches = new ArrayList<>();
                    for (Merch merch1 : artist.getMerches()) {
                        merches.add(merch1.getName() + " - " + merch1.getPrice()
                                + ":\n\t" + merch1.getDescription());
                    }
                    // Collect event details
                    List<String> artistEvents = new ArrayList<>();
                    for (Event event : artist.getEvents()) {
                        artistEvents.add(event.getName() + " - " + event.getDate()
                                + ":\n\t" + event.getDescription());
                    }
                    // Construct the message for the Artist page
                    message = "Albums:\n\t" + albums + "\n\nMerch:\n\t" + merches
                            + "\n\nEvents:\n\t" + artistEvents;
                } else {
                    // Use last page message if artist not found
                    message = user.getLastPageMessage();
                }
                // If on Host page
            } else if (user.getCurrentPage().equals("Host")) {
                User host; // Variable to store the host user
                // Determine the host user based on the search bar's last selection or the
                // last user selected
                if (user.getSearchBar().getLastSelected() != null) {
                    host = adminInstance.getUser(user.getSearchBar().getLastSelected().getName());
                } else {
                    host = adminInstance.getUser(user.getLastUserSelected());
                }
                // If host user is found
                if (host != null) {
                    // Prepare a list for podcast details
                    List<String> episodes;
                    List<PodcastPrint> podcastPrints = new ArrayList<>();
                    // Loop through podcasts
                    for (Podcast podcast : host.getPodcasts()) {
                        episodes = new ArrayList<>();
                        // Loop through episodes
                        for (Episode episode : podcast.getEpisodes()) {
                            // Add episode details
                            episodes.add(episode.getName() + " - " + episode.getDescription());
                        }
                        // Add podcast details
                        podcastPrints.add(new PodcastPrint(podcast.getName(), episodes));
                    }
                    // Prepare a list for announcements
                    List<String> announcements = new ArrayList<>();
                    // Loop through announcements
                    for (Announcement announcement : host.getAnnouncements()) {
                        // Add announcement details
                        announcements.add(announcement.getName() + ":\n\t"
                                + announcement.getDescription() + "\n");
                    }
                    // Construct the message for the Host page
                    message = "Podcasts:\n\t" + podcastPrints + "\n\nAnnouncements:\n\t"
                            + announcements;

                    // Update the last user selected with the host's username
                    user.setLastUserSelected(host.getUsername());
                    // If no host is found, use the last page message
                } else if (user.getLastPageMessage() != null) {
                    // Use last page message
                    message = user.getLastPageMessage();
                } else {
                    // Default message if no host and no last page message
                    message = "Liked songs:\n\t[]\n\nFollowed playlists:\n\t[]";
                }
            }
            // Update the last page message of the user
            user.setLastPageMessage(message);
            } else {
                // If the user is offline, set the message to indicate offline status
                message = user.getUsername() + " is offline.";
            }
        // If the initial placeholder message is still unchanged,
        // replace it with the last page message
        if (message.equals("mama ta")) {
            message = user.getLastPageMessage();
        }
        // Create an ObjectNode to structure the response data
        ObjectNode objectNode = objectMapper.createObjectNode();
        // Populate the response with user details and message
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        // Return the structured response
        return objectNode;

    }

    /**
     * Adds an announcement
     * @param commandInput the command input
     * @return the result of the adding
     */
    public static ObjectNode addAnnouncement(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.addAnnouncement(commandInput.getName(), commandInput.getDescription());
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Removes an announcement
     * @param commandInput the command input
     * @return the result of the remove
     */
    public static ObjectNode removeAnnouncement(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.removeAnnouncement(commandInput.getName());
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds a merch
     * @param commandInput the command input
     * @return the result of the add
     */
    public static ObjectNode addMerch(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.addMerch(commandInput);
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Adds a podcast
     * @param commandInput the command input
     * @return the result of the add
     */
    public static ObjectNode addPodcast(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else {
            message = user.addPodcast(commandInput);
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shows all the podcasts
     * @param commandInput the command input
     * @return the result of the show
     */
    public static ObjectNode showPodcasts(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        ArrayList<PodcastHelper> podcastHelpers = new ArrayList<>();

        assert user != null;
        for (Podcast podcast : user.getPodcasts()) {
            PodcastHelper podcastHelper = new PodcastHelper();

            for (Episode episode : podcast.getEpisodes()) {
                podcastHelper.getEpisodes().add(episode.getName());
            }

            podcastHelper.setName(podcast.getName());

            podcastHelpers.add(podcastHelper);
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(podcastHelpers));

        return objectNode;
    }

    /**
     * Shows all the albums
     * @param commandInput the command input
     * @return the result of the show
     */
    public static ObjectNode showAlbums(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        assert user != null;
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayList<AlbumHelper> albumHelpers = new ArrayList<>();
        for (Album album1 : user.getAlbums()) {
            AlbumHelper albumHelper = new AlbumHelper();
            for (Song songInput : album1.getSongs()) {
                albumHelper.getSongs().add(songInput.getName());
            }
            albumHelper.setName(album1.getName());

            albumHelpers.add(albumHelper);
        }
        objectNode.put("result", objectMapper.valueToTree(albumHelpers));
        return objectNode;
    }

    /**
     * Adds or removes a song in a playlist
     * @param commandInput the command input
     * @return the result of the add/remove
     */
    public static ObjectNode addRemoveInPlaylist(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        String message;
        if (user != null && user.getOnlineStatus()) {
            message = user.addRemoveInPlaylist(commandInput.getPlaylistId());
        } else  {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Switches the visibility of a playlist
     * @param commandInput
     * @return
     */
    public static ObjectNode switchVisibility(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shows the playlists
     * @param commandInput the command input
     * @return the result of the show
     */
    public static ObjectNode showPlaylists(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Follows a playlist
     * @param commandInput the command input
     * @return the result of the follow
     */
    public static ObjectNode follow(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    /**
     * Shows the status of a user
     * @param commandInput the command input
     * @return the result of the show
     */
    public static ObjectNode status(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user != null) {
            PlayerStats stats = user.getPlayerStats();
            objectNode.put("stats", objectMapper.valueToTree(stats));
        }
        return objectNode;
    }

    /**
     * Shows a user's liked songs
     * @param commandInput the command input
     * @return the result of the show
     */
    public static ObjectNode showLikedSongs(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets the preferred genre of a user
     * @param commandInput the command input
     * @return the result of the get
     */
    public static ObjectNode getPreferredGenre(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        User user = adminInstance.getUser(commandInput.getUsername());
        assert user != null;
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    /**
     * Gets the top 5 songs
     * @param commandInput the command input
     * @return the result of the get
     */
    public static ObjectNode getTop5Songs(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        List<String> songs = adminInstance.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    /**
     * Gets the top 5 playlists
     * @param commandInput the command input
     * @return the result of the get
     */
    public static ObjectNode getTop5Playlists(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        List<String> playlists = adminInstance.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    /**
     * Gets the top 5 albums
     * @param commandInput the command input
     * @return the result of the get
     */
    public static ObjectNode getTop5Albums(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        List<String> albums = adminInstance.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    /**
     * Gets the top 5 artists
     * @param commandInput the command input
     * @return the result of the get
     */
    public static ObjectNode getTop5Artists(final CommandInput commandInput) {
        Admin adminInstance = Admin.getInstance();
        List<String> artists = adminInstance.getTop5Artists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(artists));

        return objectNode;
    }
}
