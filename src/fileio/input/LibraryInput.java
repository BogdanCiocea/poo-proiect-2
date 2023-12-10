package fileio.input;

import java.util.ArrayList;

public final class LibraryInput {
    private ArrayList<SongInput> songs;
    private ArrayList<PodcastInput> podcasts;
    private ArrayList<UserInput> users;

    /**
     * Constructor for library input.
     */
    public LibraryInput() {
    }
    /**
     * Getter for songs.
     * @return songs
     */
    public ArrayList<SongInput> getSongs() {
        return songs;
    }
    /**
     * Setter for songs.
     * @param songs songs
     */
    public void setSongs(final ArrayList<SongInput> songs) {
        this.songs = songs;
    }
    /**
     * Getter for podcasts.
     * @return podcasts
     */
    public ArrayList<PodcastInput> getPodcasts() {
        return podcasts;
    }
    /**
     * Setter for podcasts.
     * @param podcasts podcasts
     */
    public void setPodcasts(final ArrayList<PodcastInput> podcasts) {
        this.podcasts = podcasts;
    }
    /**
     * Getter for users.
     * @return users
     */
    public ArrayList<UserInput> getUsers() {
        return users;
    }
    /**
     * Setter for users.
     * @param users users
     */
    public void setUsers(final ArrayList<UserInput> users) {
        this.users = users;
    }
    /**
     * toString method.
     * @return string
     */
    @Override
    public String toString() {
        return "LibraryInput{" + "songs=" + songs + ", podcasts=" + podcasts + ", users="
                + users + '}';
    }
}
