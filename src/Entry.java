import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;

public class Entry implements Serializable {
    private String content;
    private User originalAuthor;
    private Timestamp creationDate;
    private Timestamp modificationDate;
    private int id;

    public Entry(String content, User originalAuthor, int id, Timestamp modificationDate, Timestamp creationDate) {
        this.content = content;
        this.originalAuthor = originalAuthor;
        this.modificationDate = modificationDate;
        this.creationDate = creationDate;
        this.id = id;
    }

    public Entry(String content, User originalAuthor) {
        this.content = content;
        this.originalAuthor = originalAuthor;

        Timestamp now = Timestamp.from(Instant.now());
        this.creationDate = now;
        this.modificationDate = now;

        this.id = generateID(6);
    }

    public void modify(String newContent) {
        this.content = newContent;

        Timestamp now = Timestamp.from(Instant.now());
        this.modificationDate = now;
    }

    public String getContent() {
        return content;
    }

    public User getOriginalAuthor() {
        return originalAuthor;
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
