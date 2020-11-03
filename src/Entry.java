import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class Entry {
    private String content;
    private User originalAuthor;
    private Timestamp creationDate;
    private Timestamp modificationDate;
    private ArrayList<Modification> modifications;

    public Entry(String content, User originalAuthor) {
        this.originalAuthor = originalAuthor;
        this.modifications = new ArrayList<>();
        this.modify(content, originalAuthor);
        this.creationDate = this.modifications.get(0).getDate();
    }

    public void modify(String newContent, User author) {
        this.content = newContent;

        Timestamp now = Timestamp.from(Instant.now());
        this.modifications.add(new Modification(author, now));

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
}