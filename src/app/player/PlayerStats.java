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

    public String getName() {
        return name;
    }

    public int getRemainedTime() {
        return remainedTime;
    }

    public String getRepeat() {
        return repeat;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public PlayerStats(String name, int remainedTime, Enums.RepeatMode repeatMode, boolean shuffle, boolean paused) {
        this.name = name;
        this.remainedTime = remainedTime;
        this.paused = paused;
        switch (repeatMode) {
            case REPEAT_ALL -> this.repeat = "Repeat All";
            case REPEAT_ONCE -> this.repeat = "Repeat Once";
            case REPEAT_INFINITE -> this.repeat = "Repeat Infinite";
            case REPEAT_CURRENT_SONG -> this.repeat = "Repeat Current Song";
            case NO_REPEAT -> this.repeat = "No Repeat";
        }
        this.shuffle = shuffle;
    }

}
