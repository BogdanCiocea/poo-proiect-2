package app.merch;

public class Merch {
    private String name;
    private String description;
    private Integer price;
    /**
     * @param name        name of the merch
     * @param description description of the merch
     * @param price       price of the merch
     */
    public Merch(final String name, final String description, final Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    /**
     * Gets the name of the merch
     * @return name of the merch
     */
    public String getName() {
        return name;
    }
    /**
     * Sets the name of the merch
     * @param name name of the merch
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Gets the description of the merch
     * @return description of the merch
     */
    public String getDescription() {
        return description;
    }
    /**
     * Sets the description of the merch
     * @param description description of the merch
     */
    public void setDescription(final String description) {
        this.description = description;
    }
    /**
     * Gets the price of the merch
     * @return price of the merch
     */
    public Integer getPrice() {
        return price;
    }
    /**
     * Sets the price of the merch
     * @param price price of the merch
     */
    public void setPrice(final Integer price) {
        this.price = price;
    }
}
