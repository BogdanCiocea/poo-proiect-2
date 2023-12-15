package app.player;

import app.audio.Collections.AudioCollection;
import app.audio.Files.AudioFile;
import app.utils.Enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PlayerSource {
    private final Enums.PlayerSourceType type;
    private AudioCollection audioCollection;
    private AudioFile audioFile;
    private int index;
    private int indexShuffled;
    private int remainedDuration;
    private final List<Integer> indices = new ArrayList<>();
    /**
     * Getter for a type.
     * @return type
     */
    public Enums.PlayerSourceType getType() {
        return type;
    }
    /**
     * Getter for audioCollection.
     * @return audioCollection
     */
    public AudioCollection getAudioCollection() {
        return audioCollection;
    }
    /**
     * Getter for audioFile.
     * @return audioFile
     */
    public AudioFile getAudioFile() {
        return audioFile;
    }
    /**
     * Getter for index.
     * @return index
     */
    public int getIndex() {
        return index;
    }
    /**
     * Getter for indexShuffled.
     * @return indexShuffled
     */
    public int getIndexShuffled() {
        return indexShuffled;
    }
    /**
     * Getter for remainedDuration.
     * @return remainedDuration
     */
    public int getRemainedDuration() {
        return remainedDuration;
    }
    /**
     * Getter for indices.
     * @return indices
     */
    public List<Integer> getIndices() {
        return indices;
    }

    /**
     * Constructor for PlayerSource
     * @param type type
     * @param audioFile audioFile
     */
    public PlayerSource(final Enums.PlayerSourceType type, final AudioFile audioFile) {
        this.type = type;
        this.audioFile = audioFile;
        this.remainedDuration = audioFile.getDuration();
    }
    /**
     * Constructor for PlayerSource
     * @param type type
     * @param audioCollection audioCollection
     */
    public PlayerSource(final Enums.PlayerSourceType type, final AudioCollection audioCollection) {
        this.type = type;
        this.audioCollection = audioCollection;
        this.audioFile = audioCollection.getTrackByIndex(0);
        this.index = 0;
        this.indexShuffled = 0;
        this.remainedDuration = audioFile.getDuration();
    }

    /**
     * Constructor for PlayerSource
     * @param type type
     * @param audioCollection audioCollection
     * @param bookmark bookmark
     */
    public PlayerSource(final Enums.PlayerSourceType type, final AudioCollection audioCollection,
                        final PodcastBookmark bookmark) {
        this.type = type;
        this.audioCollection = audioCollection;
        this.index = bookmark.getId();
        this.remainedDuration = bookmark.getTimestamp();
        this.audioFile = audioCollection.getTrackByIndex(index);
    }

    /**
     * Returns the duration of the current audio file
     * @return duration
     */
    public int getDuration() {
        return remainedDuration;
    }

    /**
     * Sets the next audio file
     * @param repeatMode repeatMode
     * @param shuffle shuffle
     * @return true if the player is paused
     */
    public boolean setNextAudioFile(final Enums.RepeatMode repeatMode, final boolean shuffle) {
        boolean isPaused = false;

        if (type == Enums.PlayerSourceType.LIBRARY) {
            if (repeatMode != Enums.RepeatMode.NO_REPEAT) {
                remainedDuration = audioFile.getDuration();
            } else {
                remainedDuration = 0;
                isPaused = true;
            }
        } else {
            if (repeatMode == Enums.RepeatMode.REPEAT_ONCE
                    || repeatMode == Enums.RepeatMode.REPEAT_CURRENT_SONG
                    || repeatMode == Enums.RepeatMode.REPEAT_INFINITE) {
                remainedDuration = audioFile.getDuration();
            } else if (repeatMode == Enums.RepeatMode.NO_REPEAT) {
                if (shuffle) {
                    if (indexShuffled == indices.size() - 1) {
                        remainedDuration = 0;
                        isPaused = true;
                    } else {
                        indexShuffled++;

                        index = indices.get(indexShuffled);
                        updateAudioFile();
                        remainedDuration = audioFile.getDuration();
                    }
                } else {
                    if (index == audioCollection.getNumberOfTracks() - 1) {
                        remainedDuration = 0;
                        isPaused = true;
                    } else {
                        index++;
                        updateAudioFile();
                        remainedDuration = audioFile.getDuration();
                    }
                }
            } else if (repeatMode == Enums.RepeatMode.REPEAT_ALL) {
                if (shuffle) {
                    indexShuffled = (indexShuffled + 1) % indices.size();
                    index = indices.get(indexShuffled);
                } else {
                    index = (index + 1) % audioCollection.getNumberOfTracks();
                }
                updateAudioFile();
                remainedDuration = audioFile.getDuration();
            }
        }

        return isPaused;
    }

    /**
     * Sets the previous audio file
     * @param shuffle shuffle
     */
    public void setPrevAudioFile(final boolean shuffle) {
        if (type == Enums.PlayerSourceType.LIBRARY) {
            remainedDuration = audioFile.getDuration();
        } else {
            if (remainedDuration != audioFile.getDuration()) {
                remainedDuration = audioFile.getDuration();
            } else {
                if (shuffle) {
                    if (indexShuffled > 0) {
                        indexShuffled--;
                    }
                    index = indices.get(indexShuffled);
                    updateAudioFile();
                    remainedDuration = audioFile.getDuration();
                } else {
                    if (index > 0) {
                        index--;
                    }
                    updateAudioFile();
                    remainedDuration = audioFile.getDuration();
                }
            }
        }
    }

    /**
     * Generates a shuffle order
     * @param seed seed
     */
    public void generateShuffleOrder(final Integer seed) {
        indices.clear();
        Random random = new Random(seed);
        for (int i = 0; i < audioCollection.getNumberOfTracks(); i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, random);
    }

    /**
     * Updates the shuffle index
     */
    public void updateShuffleIndex() {
        for (int i = 0; i < indices.size(); i++) {
            if (indices.get(i) == index) {
                indexShuffled = i;
                break;
            }
        }
    }
    /**
     * Skips the current audio file
     * @param duration duration
     */
    public void skip(final int duration) {
        remainedDuration += duration;
        if (remainedDuration > audioFile.getDuration()) {
            remainedDuration = 0;
            index++;
            updateAudioFile();
        } else if (remainedDuration < 0) {
            remainedDuration = 0;
        }
    }

    /**
     * Updates the audio file
     */
    private void updateAudioFile() {
        if (this.audioCollection != null) {
            setAudioFile(audioCollection.getTrackByIndex(index));
        }
    }
    /**
     * Sets the audio file
     * @param audioFile audioFile
     */
    public void setAudioFile(final AudioFile audioFile) {
        this.audioFile = audioFile;
    }
}
