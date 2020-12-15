import java.io.*;

public class FileIO {
    public static void save(EntryManager entryManager, String fileName) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)))) {
            out.writeObject(entryManager);
        }
    }

    public static EntryManager load(String fileName) throws IOException, ClassNotFoundException {
        EntryManager entryManager;

        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            entryManager = (EntryManager) in.readObject();
        }

        return entryManager;
    }
}
