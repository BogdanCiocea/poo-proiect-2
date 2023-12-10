package app.announcement;

public class Announcement {
    private String name;
    private String description;

    /**
     * Constructor for Announcement
     * @param name name
     * @param description description
     */
    public Announcement(final String name, final String description) {
        this.name = name;
        this.description = description;
    }
    /**
     * Getter for name
     * @return name
     */
    public String getName() {
        return name;
    }
    /**
     * Setter for name
     * @param name name
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * Getter for description
     * @return description
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setter for description
     * @param description description
     */
    public void setDescription(final String description) {
        this.description = description;
    }
}
