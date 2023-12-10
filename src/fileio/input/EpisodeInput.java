package fileio.input;

public final class EpisodeInput {
    private String name;
    private Integer duration;
    private String description;

    /**
     * Constructor
     */
    public EpisodeInput() {
    }

    /**
     * Gets the name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Gets the duration
     * @return duration
     */
    public Integer getDuration() {
        return duration;
    }
    /**
     * Sets the duration
     * @param duration duration
     */
    public void setDuration(final Integer duration) {
        this.duration = duration;
    }
    /**
     * Gets the description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description
     * @param description description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * toString method
     * @return string
     */
    @Override
    public String toString() {
        return "EpisodeInput{" + "name='" + name + '\'' + ", description='" + description + '\''
                + ", duration=" + duration + '}';
    }
}
