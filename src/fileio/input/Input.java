package fileio.input;

import java.util.ArrayList;

public final class Input {
    private LibraryInput library;
    private ArrayList<UserInput> users;
    private ArrayList<CommandInput> commands;

    public Input() {
    }
    /**
     * Getter for library.
     * @return library
     */
    public LibraryInput getLibrary() {
        return library;
    }
    /**
     * Setter for library.
     * @param library library
     */
    public void setLibrary(final LibraryInput library) {
        this.library = library;
    }
    /**
     * Getter for users.
     * @return users
     */
    public ArrayList<UserInput> getUsers() {
        return users;
    }
    /**
     * Setter for users.
     * @param users users
     */
    public void setUsers(final ArrayList<UserInput> users) {
        this.users = users;
    }
    /**
     * Getter for commands.
     * @return commands
     */
    public ArrayList<CommandInput> getCommands() {
        return commands;
    }
    /**
     * Setter for commands.
     * @param commands commands
     */
    public void setCommands(final ArrayList<CommandInput> commands) {
        this.commands = commands;
    }
    /**
     * toString method.
     * @return string
     */
    @Override
    public String toString() {
        return "AppInput{"
                + "library=" + library
                + ", users=" + users
                + ", commands=" + commands
                + '}';
    }
}
