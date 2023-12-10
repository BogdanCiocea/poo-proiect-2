package fileio.input;

public final class UserInput {
    private String username;
    private int age;
    private String city;
    /**
     * Constructor
     */
    public UserInput() {
    }
    /**
     * Getter for username
     * @return username
     */
    public String getUsername() {
        return username;
    }
    /**
     * Setter for username
     * @param username username
     */
    public void setUsername(final String username) {
        this.username = username;
    }
    /**
     * Getter for age
     * @return age
     */
    public int getAge() {
        return age;
    }
    /**
     * Setter for age
     * @param age age
     */
    public void setAge(final int age) {
        this.age = age;
    }
    /**
     * Getter for city
     * @return city
     */
    public String getCity() {
        return city;
    }
    /**
     * Setter for city
     * @param city city
     */
    public void setCity(final String city) {
        this.city = city;
    }
    /**
     * toString method
     * @return string
     */
    @Override
    public String toString() {
        return "UserInput{" + "username='" + username + '\'' + ", age=" + age
                + ", city='" + city + '\'' + '}';
    }
}
