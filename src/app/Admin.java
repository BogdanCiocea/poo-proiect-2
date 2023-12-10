package app;

import app.audio.Collections.Album;
import app.audio.Collections.AlbumHelper;
import app.audio.Collections.AlbumSearchHelper;
import app.audio.Collections.ArtistHelper;
import app.audio.Collections.HostHelper;
import app.audio.Collections.Playlist;
import app.audio.Collections.Podcast;
import app.audio.Files.Episode;
import app.audio.Files.Song;
import app.user.User;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import fileio.input.SongInput;
import fileio.input.UserInput;

import java.util.*;

public class Admin {
    private static List<User> users = new ArrayList<>();
    private static List<Song> songs = new ArrayList<>();
    private static List<Podcast> podcasts = new ArrayList<>();
    private static ArrayList<Album> albums = new ArrayList<>();
    private static List<User> artists = new ArrayList<>();
    private static List<ArtistHelper> artistHelpers = new ArrayList<>();
    private static List<AlbumHelper> albumHelpers = new ArrayList<>();
    private static List<AlbumSearchHelper> albumSearchHelpers = new ArrayList<>();
    private static List<User> hosts = new ArrayList<>();
    private static List<HostHelper> hostHelpers = new ArrayList<>();
    public static final int TOP_5 = 5;

    private static List<Song> likedSongs = new ArrayList<>();

    public static List<Song> getLikedSongs() {
        return likedSongs;
    }

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

    /**
     * Sets the users list
     * @param userInputList list of users
     */
    public static void setUsers(final List<UserInput> userInputList) {
        users = new ArrayList<>();
        for (UserInput userInput : userInputList) {
            users.add(new User(userInput.getUsername(), userInput.getAge(), userInput.getCity()));
        }
    }

    /**
     * Sets the song list
     *
     * @param songInputList list of songs
     */
    public static void setSongs(final List<SongInput> songInputList) {
        songs = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Sets the songs list
     *
     * @param songInputList list of songs
     */
    public static void setSongsNew(final List<Song> songInputList) {
        songs = new ArrayList<>();
        for (Song songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }

    /**
     * Adds songs to the songs list
     * @param songInputList list of songs
     * @return the songs list
     */
    public static List<Song> addSongs(final List<SongInput> songInputList) {
        List<Song> songList = new ArrayList<>();
        for (SongInput songInput : songInputList) {
            songList.add(new Song(songInput.getName(), songInput.getDuration(),
                    songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
        return songList;
    }

    /**
     * Sets the songs list
     * @param songInputList list of songs
     */
    public static void setSongList(final List<SongInput> songInputList) {
        for (SongInput songInput : songInputList) {
            songs.add(new Song(songInput.getName(), songInput.getDuration(), songInput.getAlbum(),
                    songInput.getTags(), songInput.getLyrics(), songInput.getGenre(),
                    songInput.getReleaseYear(), songInput.getArtist()));
        }
    }
    /**
     * Sets the podcasts list
     * @param podcastInputList list of podcasts
     */
    public static void setPodcasts(final List<PodcastInput> podcastInputList) {
        podcasts = new ArrayList<>();
        for (PodcastInput podcastInput : podcastInputList) {
            List<Episode> episodes = new ArrayList<>();
            for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
                episodes.add(new Episode(episodeInput.getName(),
                        episodeInput.getDuration(), episodeInput.getDescription()));
            }
            podcasts.add(new Podcast(podcastInput.getName(), podcastInput.getOwner(), episodes));
        }
    }

    /**
     * Gets the songs list
     * @return the songs list
     */
    public static List<Song> getSongs() {
        return new ArrayList<>(songs);
    }
    /**
     * Gets the songs list
     * @return the songs list
     */
    public static List<Song> getSongsList() {
        return songs;
    }
    /**
     * Gets the podcasts list
     * @return the podcasts list
     */
    public static List<Podcast> getAdminPodcasts() {
        return podcasts;
    }
    public static List<Podcast> getPodcasts() {
        return new ArrayList<>(podcasts);
    }

    public static List<Podcast> getPodcastsList() {
        return podcasts;
    }

    /**
     * Gets the playlists list
     * @return the playlists list
     */
    public static List<Playlist> getPlaylists() {
        List<Playlist> playlists = new ArrayList<>();
        for (User user : users) {
            playlists.addAll(user.getPlaylists());
        }
        return playlists;
    }

    /**
     * Gets the user
     * @param username username
     * @return the user
     */
    public static User getUser(final String username) {
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

    /**
     * Updates the timestamp
     * @param newTimestamp new timestamp
     */
    public static void updateTimestamp(final int newTimestamp) {
        int elapsed = newTimestamp - timestamp;
        timestamp = newTimestamp;
        if (elapsed == 0) {
            return;
        }

        for (User user : users) {
            user.simulateTime(elapsed);
        }
    }
    public static Integer getNumberOfLikes(List<Song> songs) {
        int likes = 0;
        for (Song song : songs)
            likes += song.getLikes();
        return likes;
    }
    /**
     * Gets the top 5 songs
     * @return the top 5 songs
     */
    public static List<String> getTop5Songs() {
        List<Song> sortedSongs = new ArrayList<>(songs);
        sortedSongs.sort(Comparator.comparingInt(Song::getLikes).reversed());
        List<String> topSongs = new ArrayList<>();
        int count = 0;
        for (Song song : sortedSongs) {
            if (count >= TOP_5) {
                break;
            }
            topSongs.add(song.getName());
            count++;
        }
        return topSongs;
    }

    public static Song getSongByPointer(Song song) {
        for (Song song1 : songs) {
            if (song1.equals(song)) {
                return song1;
            }
        }
        return null;
    }

    public static void updateSongLikes(Song updatedSong) {
        for (int i = 0; i < songs.size(); i++) {
            if (songs.get(i).equals(updatedSong)) {
                songs.set(i, updatedSong);
                break;
            }
        }
    }

    /**
     * Gets the top 5 playlists
     * @return the top 5 playlists
     */
    public static List<String> getTop5Playlists() {
        List<Playlist> sortedPlaylists = new ArrayList<>(getPlaylists());
        sortedPlaylists.sort(Comparator.comparingInt(Playlist::getFollowers)
                .reversed()
                .thenComparing(Playlist::getTimestamp, Comparator.naturalOrder()));
        List<String> topPlaylists = new ArrayList<>();
        int count = 0;
        for (Playlist playlist : sortedPlaylists) {
            if (count >= TOP_5) {
                break;
            }
            topPlaylists.add(playlist.getName());
            count++;
        }
        return topPlaylists;
    }

    /**
     * Gets the top 5 albums
     * @return the top 5 albums
     */
    public static List<String> getTop5Albums() {
        List<Album> sortedAlbums = new ArrayList<>(getAlbums());

        // Sort by total likes in descending order, then alphabetically by name
        sortedAlbums.sort(Comparator.comparingInt(Album::getTotalLikes).reversed()
                .thenComparing(Album::getName));

        List<String> topAlbums = new ArrayList<>();
        int count = 0;
        for (Album album : sortedAlbums) {
            if (count >= TOP_5) {
                break;
            }
            topAlbums.add(album.getName());
            count++;
        }
        return topAlbums;
    }
    /**
     * Gets the top 5 artists
     * @return the top 5 artists
     */
    public static List<String> getTop5Artists() {

        List<User> artists = new ArrayList<>(getUsers().stream().filter(user ->
                (user.getType() != null && user.getType().equals("artist"))).toList());
        artists.sort(Comparator.comparingInt(User::getNumberOfAlbumLikes).reversed());
        List<String> topArtists = new ArrayList<>();
        int count = 0;
        for (User artist : artists) {
            if (count >= TOP_5) {
                break;
            }
            topArtists.add(artist.getUsername());
            count++;
        }
        Set<String> uniqueArtists = new LinkedHashSet<>(topArtists);
        topArtists = new ArrayList<>(uniqueArtists);
        return topArtists;
    }

    /**
     * Gets the songs that start with the given name
     *
     * @param name name
     */
    public static Song getSong(final String name) {
        Song songs1;
        for (Song song : songs) {
            if (song.getName().equals(name)) {
                songs1 = song;
                return songs1;
            }
        }
        return null;
    }

    /**
     * Gets the songs that start with the given name
     * @param song song
     * @return the songs that has the same name and album
     */
    public static Song getOnlyOneSong(final Song song) {
        for (Song song1 : songs) {
            if (song1.getAlbum().equals(song.getAlbum())
                    && song1.getName().equals(song.getName())
                    && song1.getArtist().equals(song.getArtist())) {
                return song1;
            }
        }
        return null;
    }

    /**
     * Gets the artist
     * @param name name
     * @return the artist
     */
    public static User getArtist(final String name) {
        for (User artist : getArtists()) {
            if (artist.getName() != null && artist.getName().equals(name)
                    && artist.getType().equals("artist")) {
                return artist;
            }
        }
        return null;
    }

    /**
     * Resets the lists
     */
    public static void reset() {
        users = new ArrayList<>();
        songs = new ArrayList<>();
        podcasts = new ArrayList<>();
        hostHelpers = new ArrayList<>();
        albumHelpers = new ArrayList<>();
        albums = new ArrayList<>();
        hosts = new ArrayList<>();
        artists = new ArrayList<>();
        artistHelpers = new ArrayList<>();
        albumSearchHelpers = new ArrayList<>();
        timestamp = 0;
    }
}
