import java.sql.Timestamp;

public class Modification {
    private User author;
    private Timestamp date;

    public Modification(User author, Timestamp date) {
        this.author = author;
        this.date = date;
    }

    public User getAuthor() {
        return author;
    }

    public Timestamp getDate() {
        return date;
    }
}
