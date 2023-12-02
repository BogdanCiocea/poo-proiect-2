package app;

import app.audio.Collections.*;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import fileio.input.*;

import java.util.*;

public class Admin {
    private static List<User> users = new ArrayList<>();
    private static ArrayList<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static ArrayList<Album> albums = new ArrayList<>();
    private static List<User> artists = new ArrayList<>();
    private static List<ArtistHelper> artistHelpers = new ArrayList<>();
    private static List<AlbumHelper> albumHelpers = new ArrayList<>();
    private static List<AlbumSearchHelper> albumSearchHelpers = new ArrayList<>();
    private static List<User> hosts = new ArrayList<>();
    private static List<HostHelper> hostHelpers = new ArrayList<>();

    public static List<User> getHosts() {
        return hosts;
    }

    public static List<HostHelper> getHostHelpers() {
        return hostHelpers;
    }

    public static List<AlbumSearchHelper> getAlbumSearchHelpers() {
        return albumSearchHelpers;
    }

    public static List<AlbumHelper> getAlbumHelpers() {
        return albumHelpers;
    }

    public static List<ArtistHelper> getArtistHelpers() {
        return artistHelpers;
    }

    public static List<User> getArtists() {
        return artists;
    }

    public static ArrayList<Album> getAlbums() {
        return albums;
    }

    private static int timestamp = 0;

    public static void setUsers(List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    public static List<Song> setSongs(List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
        return songs;
    }

    public static List<Song> addSongs(List<SongInput> songInputList) {
        List<Song> songList = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songList.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
        return songList;
    }

    public static void setSongList(List<SongInput> songInputList) {
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    public static void setPodcasts(List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(), episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }

    public static List<Podcast> getAdminPodcasts() {
        return podcasts;
    }
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    public static User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void updateTimestamp(int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }

    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= 5) break;
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= 5) break;
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    public static List<String> getTop5Albums() {
        Map<String, Integer> albumLikes = new HashMap<>();
        List<Album> albums1 = new ArrayList<>(getAlbums());
        albums1.sort(Comparator.comparingInt(Album::getTotalLikes).reversed());
        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album albumz : albums1) {
            if (count >= 5)
                break;
            topAlbums.add(albumz.getName());
            count++;
        }
        Set<String> uniqueAlbums = new LinkedHashSet<>(topAlbums);
        topAlbums = new ArrayList<>(uniqueAlbums);
        return topAlbums;
    }

    public static List<Song> getSong(String name) {
        List<Song> songs1 = new ArrayList<>();
        for (Song song : songs) {
            if (song.getName().startsWith(name))
                songs1.add(song);
        }
        return songs1;
    }

    public static User getArtist(String name) {
        for (User artist : getArtists()) {
            if (artist.getName() != null && artist.getName().equals(name) && artist.getType().equals("artist"))
                return artist;
        }
        return null;
    }
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        timestamp = 0;
    }
}
