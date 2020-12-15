import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

public class EntryManager implements Serializable {
    private final ArrayList<Entry> entries;
    private final ArrayList<User> users;

    public EntryManager() {
        this.entries = new ArrayList<>();
        this.users = new ArrayList<>();
    }

    public ArrayList<Entry> getEntries() {
        return entries;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public User getUserById(int id) {
        ArrayList<User> users = this.getUsers();
        for (User user : users) {
            if (user.getId() == id) return user;
        }
        return null;
    }

    public Entry createEntry(String content, User author, int id, Timestamp modificationDate, Timestamp creationDate) {
        Entry entry = new Entry(content, author, id, modificationDate, creationDate);
        this.entries.add(entry);
        return entry;
    }

    public Entry createEntry(String content, User author) {
        Entry entry = new Entry(content, author);
        this.entries.add(entry);
        return entry;
    }

    public User createUser(String name, int id) {
        User user = new User(name, id);
        this.users.add(user);
        return user;
    }

    public User createUser(String name) {
        User user = new User(name);
        this.users.add(user);
        return user;
    }
}
