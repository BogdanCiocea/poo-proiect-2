package app.player;

import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.audio.LibraryEntry;
import app.utils.Enums;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Enums.RepeatMode repeatMode;
    private boolean shuffle = false;
    private boolean paused;
    private PlayerSource source;
    public static final int SKIP_TIME = 90;
    @Getter
    private String type;
    /**
     * @return the repeatMode
     */
    public Enums.RepeatMode getRepeatMode() {
        return repeatMode;
    }
    /**
     * @param repeatMode the repeatMode to set
     */
    public void setRepeatMode(final Enums.RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }
    /**
     * @return the shuffle
     */
    public boolean isShuffle() {
        return shuffle;
    }
    /**
     * @param shuffle the shuffle to set
     */
    public void setShuffle(final boolean shuffle) {
        this.shuffle = shuffle;
    }
    /**
     * @return the paused
     */
    public boolean isPaused() {
        return paused;
    }
    /**
     * @param paused the paused to set
     */
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }
    /**
     * @return the source
     */
    public PlayerSource getSource() {
        return source;
    }
    /**
     * @param source the source to set
     */
    public void setSource(final PlayerSource source) {
        this.source = source;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(final String type) {
        this.type = type;
    }
    /**
     * @return the bookmarks
     */
    public ArrayList<PodcastBookmark> getBookmarks() {
        return bookmarks;
    }
    /**
     * @param bookmarks the bookmarks to set
     */
    public void setBookmarks(final ArrayList<PodcastBookmark> bookmarks) {
        this.bookmarks = bookmarks;
    }

    private ArrayList<PodcastBookmark> bookmarks = new ArrayList<>();

    /**
     * Constructor for the player.
     */
    public Player() {
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.paused = true;
    }
    /**
     * Stops the player.
     */
    public void stop() {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        repeatMode = Enums.RepeatMode.NO_REPEAT;
        paused = true;
        source = null;
        shuffle = false;
    }

    /**
     * Bookmarks the current podcast.
     */
    private void bookmarkPodcast() {
        if (source != null && source.getAudioFile() != null) {
            PodcastBookmark currentBookmark = new
                    PodcastBookmark(source.getAudioCollection().getName(),
                    source.getIndex(), source.getDuration());
            bookmarks.removeIf(bookmark -> bookmark.getName().equals(currentBookmark.getName()));
            bookmarks.add(currentBookmark);
        }
    }
    /**
     * Creates a source for the player.
     * @param type type
     * @param entry entry
     * @param bookmarks bookmarks
     * @return source
     */
    public static PlayerSource createSource(final String type, final LibraryEntry entry,
                                            final List<PodcastBookmark> bookmarks) {
        if ("song".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.LIBRARY, (AudioFile) entry);
        } else if ("playlist".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.PLAYLIST, (AudioCollection) entry);
        } else if ("podcast".equals(type)) {
            return createPodcastSource((AudioCollection) entry, bookmarks);
        } else if ("album".equals(type)) {
            return new PlayerSource(Enums.PlayerSourceType.ALBUM, (AudioCollection) entry);
        }

        return null;
    }

    /**
     * Creates a podcast source for the player.
     * @param collection collection
     * @param bookmarks bookmarks
     * @return source
     */
    private static PlayerSource createPodcastSource(final AudioCollection collection,
                                                    final List<PodcastBookmark> bookmarks) {
        for (PodcastBookmark bookmark : bookmarks) {
            if (bookmark.getName().equals(collection.getName())) {
                return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection, bookmark);
            }
        }
        return new PlayerSource(Enums.PlayerSourceType.PODCAST, collection);
    }
    /**
     * Sets the source for the player.
     * @param entry entry
     * @param type type
     */
    public void setSource(final LibraryEntry entry, final String type) {
        if ("podcast".equals(this.type)) {
            bookmarkPodcast();
        }

        this.type = type;
        this.source = createSource(type, entry, bookmarks);
        this.repeatMode = Enums.RepeatMode.NO_REPEAT;
        this.shuffle = false;
        this.paused = true;
    }

    /**
     * Plays / pauses the current audio file.
     */
    public void pause() {
        paused = !paused;
    }
    /**
     * Shuffles the current audio file.
     * @param seed seed
     */
    public void shuffle(final Integer seed) {
        if (seed != null) {
            source.generateShuffleOrder(seed);
        }

        if (source.getType() == Enums.PlayerSourceType.PLAYLIST
                || source.getType() == Enums.PlayerSourceType.ALBUM) {
            shuffle = !shuffle;
            if (shuffle) {
                source.updateShuffleIndex();
            }
        }
    }
    /**
     * Repeats the current audio file.
     * @return repeatMode
     */
    public Enums.RepeatMode repeat() {
        if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
            if (source.getType() == Enums.PlayerSourceType.LIBRARY) {
                repeatMode = Enums.RepeatMode.REPEAT_ONCE;
            } else {
                repeatMode = Enums.RepeatMode.REPEAT_ALL;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
                repeatMode = Enums.RepeatMode.REPEAT_INFINITE;
            } else {
                if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                    repeatMode = Enums.RepeatMode.REPEAT_CURRENT_SONG;
                } else {
                    repeatMode = Enums.RepeatMode.NO_REPEAT;
                }
            }
        }

        return repeatMode;
    }
    /**
     * Simulates the player.
     * @param time time
     */
    public void simulatePlayer(int time) {
        if (!paused && this.source != null) {
            while (time >= source.getDuration()) {
                time -= source.getDuration();
                next();
                if (paused) {
                    break;
                }
            }
            if (!paused) {
                source.skip(-time);
            }
        }
    }
    /**
     * Plays the next audio file.
     */
    public void next() {
        paused = source.setNextAudioFile(repeatMode, shuffle);
        if (repeatMode == Enums.RepeatMode.REPEAT_ONCE) {
            repeatMode = Enums.RepeatMode.NO_REPEAT;
        }

        if (source.getDuration() == 0 && paused) {
            stop();
        }
    }
    /**
     * Plays the previous audio file.
     */
    public void prev() {
        source.setPrevAudioFile(shuffle);
        paused = false;
    }
    /**
     * Skips the current audio file.
     * @param duration duration
     */
    private void skip(final int duration) {
        source.skip(duration);
        paused = false;
    }
    /**
     * Skips the next audio file.
     */
    public void skipNext() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(-SKIP_TIME);
        }
    }
    /**
     * Skips the previous audio file.
     */
    public void skipPrev() {
        if (source.getType() == Enums.PlayerSourceType.PODCAST) {
            skip(SKIP_TIME);
        }
    }
    /**
     * Gets the current audio file.
     * @return current audio file
     */
    public AudioFile getCurrentAudioFile() {
        if (source == null) {
            return null;
        }
        return source.getAudioFile();
    }

    /**
     * Gets the paused state.
     * @return paused state
     */
    public boolean getPaused() {
        return paused;
    }
    /**
     * Gets the shuffle state.
     * @return shuffle state
     */
    public boolean getShuffle() {
        return shuffle;
    }

    /**
     * Gets the stats of the player.
     * @return stats
     */
    public PlayerStats getStats() {
        String filename = "";
        int duration = 0;
        if (source != null && source.getAudioFile() != null) {
            filename = source.getAudioFile().getName();
            duration = source.getDuration();
        } else {
            stop();
        }
        return new PlayerStats(filename, duration, repeatMode, shuffle, paused);
    }
}
