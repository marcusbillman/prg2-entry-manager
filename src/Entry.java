import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class Entry implements Serializable {
    private String content;
    private final User author;
    private final Timestamp creationDate;
    private Timestamp modificationDate;
    private final int id;

    public Entry(String content, User author, int id, Timestamp modificationDate, Timestamp creationDate) {
        this.content = content;
        this.author = author;
        this.modificationDate = modificationDate;
        this.creationDate = creationDate;
        this.id = id;
    }

    public Entry(String content, User author) {
        this.content = content;
        this.author = author;

        Timestamp now = Timestamp.from(Instant.now());
        this.creationDate = now;
        this.modificationDate = now;

        this.id = generateID(6);
    }

    public void modify(String newContent) {
        this.content = newContent;
        this.modificationDate = Timestamp.from(Instant.now());
    }

    public String getContent() {
        return content;
    }

    public User getAuthor() {
        return author;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public Timestamp getModificationDate() {
        return modificationDate;
    }

    public int getId() {
        return id;
    }

    private int generateID(int length) {
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
}
