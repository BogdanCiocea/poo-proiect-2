package app.event;

public class Event {
    private String name;
    private String description;
    private String date;
    /**
     * @param name        name of the event
     * @param description description of the event
     * @param date        date of the event
     */
    public Event(final String name, final String description, final String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public Event() {
    }
    /**
     * @return name of the event
     */
    public String getName() {
        return name;
    }
    /**
     * @param name name of the event
     */
    public void setName(final String name) {
        this.name = name;
    }
    /**
     * @return description of the event
     */
    public String getDescription() {
        return description;
    }
    /**
     * @param description description of the event
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Gets the date of the event.
     * @return the date of the event.
     */
    public String getDate() {
        return date;
    }
    /**
     * Sets the date of the event.
     * @param date the date of the event.
     */
    public void setDate(final String date) {
        this.date = date;
    }
}
