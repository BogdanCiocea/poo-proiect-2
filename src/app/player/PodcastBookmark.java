package app.player;

import lombok.Getter;

@Getter
public class PodcastBookmark {
    private final String name;
    private final int id;
    private final int timestamp;
    /**
     * Getter for name
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Getter for id
     * @return id
     */
    public int getId() {
        return id;
    }
    /**
     * Getter for timestamp
     * @return timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }
    /**
     * Constructor for PodcastBookmark
     * @param name name
     * @param id id
     * @param timestamp timestamp
     */
    public PodcastBookmark(final String name, final int id, final int timestamp) {
        this.name = name;
        this.id = id;
        this.timestamp = timestamp;
    }
    /**
     * toString method
     * @return String
     */
    @Override
    public String toString() {
        return "PodcastBookmark{"
                + "name='" + name + '\''
                + ", id=" + id
                + ", timestamp=" + timestamp
                + '}';
    }
}
