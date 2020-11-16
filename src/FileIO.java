import java.io.*;

public class FileIO {
    public static void save(EntryManager entryManager, String fileName) throws IOException {
        ObjectOutputStream out = null;

        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            out.writeObject(entryManager);
        } finally {
            out.close();
        }
    }

    public static EntryManager load(String fileName) throws IOException, ClassNotFoundException {
        EntryManager entryManager;

        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
            entryManager = (EntryManager) in.readObject();
        } finally {
            in.close();
        }

        return entryManager;
    }
}
