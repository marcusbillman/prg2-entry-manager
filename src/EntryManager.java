import java.util.ArrayList;

public class EntryManager {
    private ArrayList<Entry> entries;
    private ArrayList<User> users;

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

    public Entry createEntry(String content, User originalAuthor) {
        Entry entry = new Entry(content, originalAuthor);
        this.entries.add(entry);
        return entry;
    }

    public User createUser(String name) {
        User user = new User(name);
        this.users.add(user);
        return user;
    }
}
