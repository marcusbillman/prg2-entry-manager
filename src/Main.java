public class Main {
    public static void main(String[] args) {
        EntryManager entryManager = new EntryManager();
        View view = new View("Entry Manager");
        new Controller(entryManager, view, true);
    }
}
