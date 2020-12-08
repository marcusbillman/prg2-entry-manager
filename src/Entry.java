import java.io.Serializable;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Random;

public class Entry implements Serializable {
    private String content;
    private User originalAuthor;
    private Timestamp creationDate;
    private Timestamp modificationDate;
    private ArrayList<Modification> modifications;
    private int id;

    public Entry(String content, User originalAuthor) {
        this.originalAuthor = originalAuthor;
        this.modifications = new ArrayList<>();
        this.modify(content, originalAuthor);
        this.creationDate = this.modifications.get(0).getDate();
        this.id = generateID(6);
    }

    public void modify(String newContent, User author) {
        this.content = newContent;

        Timestamp now = Timestamp.from(Instant.now());

        Modification modification = new Modification(author, now);
        this.modifications.add(modification);

        this.updateModificationDate();
    }

    public void updateModificationDate() {
        Modification latestModification = this.modifications.get(modifications.size() - 1);
        this.modificationDate = latestModification.getDate();
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

    public ArrayList<Modification> getModifications() {
        return modifications;
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
