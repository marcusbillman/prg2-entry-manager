import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Acts as an MVC model that contains the state data, including entries and users. Is Serializable in order to enable
 * state saving to file.
 */
public class EntryManager implements Serializable {
    private final ArrayList<Entry> entries;
    private final ArrayList<User> users;

    /**
     * Constructor for EntryManager. Initializes two empty ArrayLists that hold all saved entries and users.
     */
    public EntryManager() {
        this.entries = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    /**
     * Gets an ArrayList of all entries in EntryManager.
     * @return ArrayList of all entries in EntryManager
     */
    public ArrayList<Entry> getEntries() {
        return entries;
    }

    /**
     * Gets an ArrayList of all users in EntryManager.
     * @return ArrayList of all users in EntryManager
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Gets the User that has a given id.
     * @param id - id to search for
     * @return User with given id, or null if no user was found
     */
    public User getUserById(int id) {
        ArrayList<User> users = this.getUsers();
        for (User user : users) {
            if (user.getId() == id) return user;
        }
        return null;
    }

    /**
     * Creates an entry and appends it to the ArrayList. This method takes all attributes of Entry, and thus, is only
     * called by DatabaseIO when restoring state from the database.
     * @param content - content of the entry
     * @param author - author (User) of the entry
     * @param id - id of the entry (for use in database)
     * @param modificationDate - modification date of the entry
     * @param creationDate - creation date of the entry
     * @return the newly created Entry
     */
    public Entry createEntry(String content, User author, int id, Timestamp modificationDate, Timestamp creationDate) {
        Entry entry = new Entry(content, author, id, modificationDate, creationDate);
        this.entries.add(entry);
        return entry;
    }

    /**
     * Creates an entry and appends it to the ArrayList. Uses a random id, and the current time as modification date and
     * creation date.
     * @param content - content of the entry
     * @param author - author of the entry
     * @return the newly created Entry
     */
    public Entry createEntry(String content, User author) {
        Entry entry = new Entry(content, author);
        this.entries.add(entry);
        return entry;
    }

    /**
     * Creates a user and appends it to the ArrayList. This method takes all attributes of Entry, and thus, is only
     * called by DatabaseIO when restoring state from the database.
     * @param name - name of the user
     * @param id - id of the user
     * @return the newly created User
     */
    public User createUser(String name, int id) {
        User user = new User(name, id);
        this.users.add(user);
        return user;
    }

    /**
     * Creates a user and appends it to the ArrayList. Uses a random id.
     * @param name - name of the user
     * @return the newly created User
     */
    public User createUser(String name) {
        User user = new User(name);
        this.users.add(user);
        return user;
    }
}
