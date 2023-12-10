package app.audio.Collections;

import app.audio.Files.AudioFile;
import app.audio.Files.Song;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Playlist extends AudioCollection {
    private final ArrayList<Song> songs;
    private Enums.Visibility visibility;
    private Integer followers;
    private int timestamp;

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public Enums.Visibility getVisibility() {
        return visibility;
    }

    public Integer getFollowers() {
        return followers;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public Playlist(final String name, final String owner) {
        this(name, owner, 0);
    }

    public Playlist(final String name, final String owner, final int timestamp) {
        super(name, owner);
        this.songs = new ArrayList<>();
        this.visibility = Enums.Visibility.PUBLIC;
        this.followers = 0;
        this.timestamp = timestamp;
    }

    /**
     *
     * @param song song
     * @return true if the playlist contains the song
     */
    public boolean containsSong(final Song song) {
        return songs.contains(song);
    }

    /**
     * Adds a song to the playlist
     * @param song song
     */
    public void addSong(final Song song) {
        songs.add(song);
    }

    /**
     * Removes a song from the playlist
     * @param song song
     */
    public void removeSong(final Song song) {
        songs.remove(song);
    }

    /**
     * Removes a song from the playlist
     * @param index index
     */
    public void removeSong(final int index) {
        songs.remove(index);
    }

    /**
     * Removes all songs from the playlist
     */
    public void switchVisibility() {
        if (visibility == Enums.Visibility.PUBLIC) {
            visibility = Enums.Visibility.PRIVATE;
        } else {
            visibility = Enums.Visibility.PUBLIC;
        }
    }

    /**
     * Increases the number of followers
     */
    public void increaseFollowers() {
        followers++;
    }
    /**
     * Decreases the number of followers
     */
    public void decreaseFollowers() {
        followers--;
    }

    @Override
    public int getNumberOfTracks() {
        return songs.size();
    }

    @Override
    public AudioFile getTrackByIndex(final int index) {
        return songs.get(index);
    }

    @Override
    public boolean isVisibleToUser(final String user) {
        return this.getVisibility() == Enums.Visibility.PUBLIC
                || (this.getVisibility() == Enums.Visibility.PRIVATE
                && this.getOwner().equals(user));
    }

    @Override
    public boolean matchesFollowers(final String followers) {
        return filterByFollowersCount(this.getFollowers(), followers);
    }

    private static boolean filterByFollowersCount(final int count, final String query) {
        if (query.startsWith("<")) {
            return count < Integer.parseInt(query.substring(1));
        } else if (query.startsWith(">")) {
            return count > Integer.parseInt(query.substring(1));
        } else {
            return count == Integer.parseInt(query);
        }
    }
}
