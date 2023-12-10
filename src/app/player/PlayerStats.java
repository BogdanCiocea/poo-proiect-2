package app.player;

import app.utils.Enums;
import lombok.Getter;

@Getter
public class PlayerStats {
    private final String name;
    private final int remainedTime;
    private String repeat;
    private final boolean shuffle;
    private boolean paused;
    /**
     * Getter for name
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Getter for remainedTime
     * @return remainedTime
     */
    public int getRemainedTime() {
        return remainedTime;
    }
    /**
     * Getter for repeat
     * @return repeat
     */
    public String getRepeat() {
        return repeat;
    }
    /**
     * Getter for shuffle
     * @return shuffle
     */
    public boolean isShuffle() {
        return shuffle;
    }
    /**
     * Getter for paused
     * @return paused
     */
    public boolean isPaused() {
        return paused;
    }
    /**
     * Setter for paused
     * @param paused paused
     */
    public void setPaused(final boolean paused) {
        this.paused = paused;
    }
    /**
     * Constructor for PlayerStats
     * @param name name
     * @param remainedTime remainedTime
     * @param repeatMode repeatMode
     * @param shuffle shuffle
     * @param paused paused
     */
    public PlayerStats(final String name, final int remainedTime,
                       final Enums.RepeatMode repeatMode, final boolean shuffle,
                       final boolean paused) {
        this.name = name;
        this.remainedTime = remainedTime;
        this.paused = paused;
        switch (repeatMode) {
            case REPEAT_ALL:
                this.repeat = "Repeat All";
                break;
            case REPEAT_ONCE:
                this.repeat = "Repeat Once";
                break;
            case REPEAT_INFINITE:
                this.repeat = "Repeat Infinite";
                break;
            case REPEAT_CURRENT_SONG:
                this.repeat = "Repeat Current Song";
                break;
            case NO_REPEAT:
                this.repeat = "No Repeat";
                break;
            default:
                break;
        }
        this.shuffle = shuffle;
    }
}
