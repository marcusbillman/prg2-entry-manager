import java.io.Serializable;
import java.util.ArrayList;

public class EntryManager implements Serializable {
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

    public String[] getUserNames() {
        ArrayList<User> users = this.getUsers();
        String[] userNames = new String[users.size()];

        for (int i = 0; i < users.size(); i++) {
            userNames[i] = users.get(i).getName();
        }

        return userNames;
    }

    public User getUserById(int id) {
        ArrayList<User> users = this.getUsers();
        for (User user : users) {
            if (user.getId() == id) return user;
        }
        return null;
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
