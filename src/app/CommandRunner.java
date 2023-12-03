package app;

import app.announcement.Announcement;
import app.audio.Collections.*;
import app.audio.Files.AudioFile;
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
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.output.AlbumResult;

import java.util.*;
import java.util.stream.Collectors;

public class CommandRunner {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode search(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        Filters filters = new Filters(commandInput.getFilters());
        String type = commandInput.getType();

        ArrayList<String> results = new ArrayList<>();
        String message;
        if (user != null && user.getOnlineStatus()) {
            results = user.search(filters, type);
            if (type.equals("artist")) {
                user.setCurrentPage("Artist");
                Set<String> uniqueResults = new LinkedHashSet<>(results);
                results = new ArrayList<>(uniqueResults);
            } else if (type.equals("host")) {
                user.setCurrentPage("Host");
                Set<String> uniqueResults = new LinkedHashSet<>(results);
                results = new ArrayList<>(uniqueResults);
            }
            message = "Search returned " + results.size() + " results";
        } else {
            message = commandInput.getUsername() + " is offline.";
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        objectNode.put("results", objectMapper.valueToTree(results));

        return objectNode;
    }

    public static ObjectNode switchConnectionStatus(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (Admin.getUser(commandInput.getUsername()) != null)
            message = user.switchConnectionStatus();
        else
            message = "The username " + commandInput.getUsername() + " doesn't exist.";

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    public static ObjectNode addUser(CommandInput commandInput) {
        User newUser = Admin.getUser(commandInput.getUsername());
        String message;
        if (Admin.getHosts().isEmpty())
            Admin.getHostHelpers().clear();
        if (newUser != null) {
            message = "The username " + newUser.getUsername() + " is already taken.";
        } else {
            newUser = new User(commandInput.getUsername(), commandInput.getAge(), commandInput.getCity());
            newUser.setType(commandInput.getType());
            if (newUser.getType().equals("artist")) {
                newUser.switchConnectionStatus();
                Admin.getArtists().add(newUser);
                ArtistHelper artistHelper = new ArtistHelper(newUser.getUsername(), newUser.getAlbums());
                Admin.getArtistHelpers().add(artistHelper);
            } else if (newUser.getType().equals("host")) {
                if (Admin.getHosts().isEmpty())
                    Admin.getHostHelpers().clear();
                Admin.getHosts().add(newUser);
                HostHelper hostHelper = new HostHelper(newUser.getUsername(), newUser.getPodcasts());
                Admin.getHostHelpers().add(hostHelper);
            }
            Admin.getUsers().add(newUser);
            message = "The username " + newUser.getUsername() + " has been added successfully.";
        }
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    public static ObjectNode getAllUsers(CommandInput commandInput) {
        List<User> allUsers = Admin.getUsers();
        List<User> normalUsers = new ArrayList<>();
        List<User> artists = new ArrayList<>();
        List<User> hosts = new ArrayList<>();
        for (User user : allUsers) {
            if (user.getType() == null || (!user.getType().equals("artist") && !user.getType().equals("host")))
                normalUsers.add(user);
            else if (user.getType() != null && user.getType().equals("artist"))
                artists.add(user);
            else
                hosts.add(user);
        }
        List<String> sortedUsers = new ArrayList<>();
        for (User user : normalUsers)
            sortedUsers.add(user.getUsername());
        for (User user : artists)
            sortedUsers.add(user.getUsername());
        for (User user : hosts)
            sortedUsers.add(user.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(sortedUsers));
        return objectNode;
    }

    public static ObjectNode removePodcast(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null)
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        else
            message = user.removePodcast(commandInput);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    public static ObjectNode addEvent(CommandInput commandInput) {
        String message;
        User user = Admin.getUser(commandInput.getUsername());
        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else
            message = user.addEvent(commandInput);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    public static ObjectNode removeEvent(CommandInput commandInput) {
        String message;
        User user = Admin.getUser(commandInput.getUsername());

        if (user == null)
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        else
            message = user.removeEvent(commandInput.getName());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode deleteUser(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else
            message = user.deleteUser();

        if (message.equals(commandInput.getUsername() + " was successfully deleted.")) {
            assert user != null;
            if (user.getType() != null && user.getType().equals("artist")) {
                for (Album album : user.getAlbums()) {
                    Admin.getSongs().removeAll(album.getSongs());
                }
                    Admin.getUsers().remove(user);
            }
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }

    public static ObjectNode removeALbum(CommandInput commandInput) {
        User artist = Admin.getUser(commandInput.getUsername());
        String message;
        if (artist == null)
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        else
            message = artist.removeAlbum(commandInput.getUsername(), commandInput.getName());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);
        return objectNode;
    }
    public static ObjectNode select(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
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

    public static ObjectNode getOnlineUsers(CommandInput commandInput) {
        List<String> result = new ArrayList<>();

        for (User user : Admin.getUsers()) {
            if (user.getOnlineStatus())
                result.add(user.getUsername());
        }

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(result));

        return objectNode;
    }

    public static ObjectNode load(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
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

    public static ObjectNode playPause(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.playPause();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode repeat(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
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

    public static ObjectNode shuffle(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
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

    public static ObjectNode forward(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.forward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode backward(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.backward();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode like(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
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

    public static ObjectNode next(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.next();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode prev(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.prev();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode createPlaylist(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        if (user != null && user.getOnlineStatus()) {
            String message = user.createPlaylist(commandInput.getPlaylistName(), commandInput.getTimestamp());

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

    public static ObjectNode addAlbum(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null)
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        else
            message = user.addAlbum(commandInput);

        Set<Song> correctSongList = new LinkedHashSet<>(Admin.getSongs());
        List<Song> newListSong = new ArrayList<>(correctSongList);
        Admin.setSongsNew(newListSong);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode changePage(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        String message = user.getUsername() + " accessed " + commandInput.getNextPage() + " successfully.";;

        if (commandInput.getNextPage().equals("Artist") && !user.getType().equals("artist"))
            message = user.getUsername() + "is trying to access a non-existent page.";

        if (commandInput.getNextPage().equals("Host") && !user.getType().equals("host"))
            message = user.getUsername() + "is trying to access a non-existent page.";

        if (!user.getOnlineStatus())
            message = user.getUsername() + " is offline.";
        else
            user.setCurrentPage(commandInput.getNextPage());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode printCurrentPage(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message = "mama ta";
        assert user != null;
        List<Song> likedSongs = new ArrayList<>(user.getLikedSongs());
        boolean dewIt = true;
        if (!user.getOnlineStatus())
            dewIt = false;
        if (dewIt) {
            likedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
            List<Song> topLikedSongs = likedSongs.stream().limit(5).toList();
            List<Playlist> followedPlaylists = new ArrayList<>(user.getFollowedPlaylists());
            followedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers).reversed());
            List<Playlist> topFollowedPlaylists = followedPlaylists.stream().limit(5).collect(Collectors.toList());

            List<String> topSongs = new ArrayList<>();
            for (Song song : topLikedSongs)
                topSongs.add(song.getName());

            List<String> likedPageSongs = new ArrayList<>();
            for (Song song : topLikedSongs) {
                likedPageSongs.add(song.getName() + " - " + song.getArtist());
            }
            List<String> likedPagePlaylists = new ArrayList<>();
            for (Playlist playlist : topFollowedPlaylists) {
                likedPagePlaylists.add(playlist.getName() + " - " + playlist.getOwner());
            }

            List<String> albumPage = new ArrayList<>();
            List<String> merch = new ArrayList<>();
            List<String> events = new ArrayList<>();

            if (!user.getOnlineStatus())
                message = commandInput.getUsername() + " is offline.";
            else if (user.getCurrentPage().equals("Home")) {
                String formattedTopSongs = topSongs.stream().collect(Collectors.joining(", "));
                String formattedTopPlaylists = topFollowedPlaylists.stream()
                        .map(LibraryEntry::getName)
                        .collect(Collectors.joining(", "));

                message = "Liked songs:\n\t[" + formattedTopSongs + "]\n\nFollowed playlists:\n\t[" + formattedTopPlaylists + "]";
            } else if (user.getCurrentPage().equals("LikedContent")) {
                message = "Liked songs:\n\t" + likedPageSongs + "\n\nFollowed playlists:\n\t" + likedPagePlaylists;
            } else if (user.getCurrentPage().equals("Artist") && user.getSearchBar().getLastSelected() != null) {
                User artist = Admin.getUser(user.getSearchBar().getLastSelected().getName());
                List<String> albums = new ArrayList<>();
                if (artist != null) {
                    for (Album album : artist.getAlbums())
                        albums.add(album.getName());

                    List<String> merches = new ArrayList<>();
                    for (Merch merch1 : artist.getMerches())
                        merches.add(merch1.getName() + " - " + merch1.getPrice() + ":\n\t" + merch1.getDescription());

                    List<String> artistEvents = new ArrayList<>();
                    for (Event event : artist.getEvents())
                        artistEvents.add(event.getName() + " - " + event.getDate() + ":\n\t" + event.getDescription());
                    message = "Albums:\n\t" + albums + "\n\nMerch:\n\t" + merches + "\n\nEvents:\n\t" + artistEvents;
                } else
                    message = user.getLastPageMessage();
            } else if (user.getCurrentPage().equals("Host")) {
                List<String> podcasts = new ArrayList<>();
                User host = null;
                if (user.getSearchBar().getLastSelected() != null) {
                    host = Admin.getUser(user.getSearchBar().getLastSelected().getName());
                } else
                    host = Admin.getUser(user.getLastUserSelected());
                if (host != null) {
                    List<String> episodes = new ArrayList<>();
                    List<PodcastPrint> podcastPrints = new ArrayList<>();
                    for (Podcast podcast : host.getPodcasts()) {
                        episodes = new ArrayList<>();
                        for (Episode episode : podcast.getEpisodes())
                            episodes.add(episode.getName() + " - " + episode.getDescription());
                        podcastPrints.add(new PodcastPrint(podcast.getName(), episodes));
                    }

                    List<String> announcements = new ArrayList<>();
                    for (Announcement announcement : host.getAnnouncements())
                        announcements.add(announcement.getName() + ":\n\t" + announcement.getDescription() + "\n");
                    message = "Podcasts:\n\t" + podcastPrints + "\n\nAnnouncements:\n\t" + announcements;

                    user.setLastUserSelected(host.getUsername());
                } else if (user.getLastPageMessage() != null)
                    message = user.getLastPageMessage();
                else
                    message = "Liked songs:\n\t[]\n\nFollowed playlists:\n\t[]";
            }
            user.setLastPageMessage(message);
        } else
            message = user.getUsername() + " is offline.";

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addAnnouncement(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else
            message = user.addAnnouncement(commandInput.getName(), commandInput.getDescription());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode removeAnnouncement(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;

        if (user == null) {
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        } else
            message = user.removeAnnouncement(commandInput.getName());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addMerch(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null)
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        else
            message = user.addMerch(commandInput);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode addPodcast(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        String message;
        if (user == null)
            message = "The username " + commandInput.getUsername() + " doesn't exist.";
        else
            message = user.addPodcast(commandInput);

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode showPodcasts(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ArrayList<PodcastHelper> podcastHelpers = new ArrayList<>();

        assert user != null;
        for (Podcast podcast : user.getPodcasts()) {
            PodcastHelper podcastHelper = new PodcastHelper();

            for (Episode episode : podcast.getEpisodes())
                podcastHelper.getEpisodes().add(episode.getName());

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

    public static ObjectNode showAlbums(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        assert user != null;
        objectNode.put("user", user.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        ArrayList<AlbumHelper> albumHelpers = new ArrayList<>();
        for (Album album1 : user.getAlbums()) {
            AlbumHelper albumHelper = new AlbumHelper();
            for (Song songInput : album1.getSongs())
                albumHelper.getSongs().add(songInput.getName());
            albumHelper.setName(album1.getName());

            albumHelpers.add(albumHelper);
        }
        objectNode.put("result", objectMapper.valueToTree(albumHelpers));
        return objectNode;
    }

    public static ObjectNode addRemoveInPlaylist(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
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

    public static ObjectNode switchVisibility(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.switchPlaylistVisibility(commandInput.getPlaylistId());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode showPlaylists(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        ArrayList<PlaylistOutput> playlists = user.showPlaylists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    public static ObjectNode follow(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String message = user.follow();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("message", message);

        return objectNode;
    }

    public static ObjectNode status(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        if (user != null) {
            PlayerStats stats = user.getPlayerStats();
            if (!user.getOnlineStatus())
                stats.setPaused(false);
            objectNode.put("stats", objectMapper.valueToTree(stats));
        }
        return objectNode;
    }

    public static ObjectNode showLikedSongs(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        ArrayList<String> songs = user.showPreferredSongs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    public static ObjectNode getPreferredGenre(CommandInput commandInput) {
        User user = Admin.getUser(commandInput.getUsername());
        assert user != null;
        String preferredGenre = user.getPreferredGenre();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("user", commandInput.getUsername());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(preferredGenre));

        return objectNode;
    }

    public static ObjectNode getTop5Songs(CommandInput commandInput) {
        List<String> songs = Admin.getTop5Songs();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(songs));

        return objectNode;
    }

    public static ObjectNode getTop5Playlists(CommandInput commandInput) {
        List<String> playlists = Admin.getTop5Playlists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(playlists));

        return objectNode;
    }

    public static ObjectNode getTop5Albums(CommandInput commandInput) {
        List<String> albums = Admin.getTop5Albums();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(albums));

        return objectNode;
    }

    public static ObjectNode getTop5Artists(CommandInput commandInput) {
        List<String> artists = Admin.getTop5Artists();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("command", commandInput.getCommand());
        objectNode.put("timestamp", commandInput.getTimestamp());
        objectNode.put("result", objectMapper.valueToTree(artists));

        return objectNode;
    }
}
