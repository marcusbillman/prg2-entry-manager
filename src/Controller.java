import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private View view;
    private EntryManager entryManager;
    private DatabaseIO databaseIO;

    public Controller(EntryManager entryManager, View view, boolean useDatabase) {
        this.entryManager = entryManager;
        this.view = view;
        if (useDatabase) this.databaseIO = new DatabaseIO();

        this.view.addCreateEntryListener(new CreateEntryListener());
        this.view.addSaveListener(new SaveListener());
        this.view.addLoadListener(new LoadListener());
        this.view.addTableClickListener(new TableClickListener());
    }

    private class CreateEntryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String entryContent = view.getNewEntryContent();
                if (entryContent.length() < 1) throw new IllegalArgumentException("Entry content is empty");

                User authorUser = parseAuthorUser();

                Entry entry = entryManager.createEntry(view.getNewEntryContent(), authorUser);
                view.populateEntriesTable(entryManager.getEntries());
                view.clearNewEntryContent();

                if (databaseIO != null) databaseIO.insertEntry(entry);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                view.showMessageDialog(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Save to file");

                int userSelection = fileChooser.showSaveDialog(view.getFrame());
                if (userSelection != JFileChooser.APPROVE_OPTION) return;
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();

                FileIO.save(entryManager, fileName);

                view.showMessageDialog("Saved state to file:\n" + fileName, "Saved",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                view.showMessageDialog(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class LoadListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Load from file");

                int userSelection = fileChooser.showOpenDialog(view.getFrame());
                if (userSelection != JFileChooser.APPROVE_OPTION) return;
                String fileName = fileChooser.getSelectedFile().getAbsolutePath();

                entryManager = FileIO.load(fileName);
                view.populateEntriesTable(entryManager.getEntries());
                view.populateAuthorComboBox(entryManager.getUsers(), "first");
                view.clearNewEntryContent();

                view.showMessageDialog("Loaded state from file:\n" + fileName, "Loaded",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                view.showMessageDialog(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class TableClickListener implements MouseListener {
        public void mouseClicked(MouseEvent mouseEvent) {
            try {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int index = table.rowAtPoint(point);

                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    String newContent = view.showInputDialog("Entry Content", "Modify Entry");

                    if (newContent == null) return;
                    if (newContent.length() < 1) throw new IllegalArgumentException("Entry content is empty");

                    Entry entry = entryManager.getEntries().get(index);

                    User authorUser = parseAuthorUser();

                    entry.modify(newContent, authorUser);
                    view.populateEntriesTable(entryManager.getEntries());
                    view.clearNewEntryContent();

                    if (databaseIO != null) databaseIO.updateEntry(entry, newContent);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                view.showMessageDialog(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        public void mousePressed(MouseEvent mouseEvent) {

        }

        public void mouseReleased(MouseEvent mouseEvent) {

        }

        public void mouseEntered(MouseEvent mouseEvent) {

        }

        public void mouseExited(MouseEvent mouseEvent) {

        }
    }

    private User parseAuthorUser() {
        String authorRaw = view.getAuthorName();
        if (authorRaw == null || authorRaw.length() < 1) {
            throw new IllegalArgumentException("Author is empty");
        }

        String authorName = authorRaw.replaceAll(" \\(.*\\)", "");
        int authorId = 0;
        User authorUser;

        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(authorRaw);
        if (matcher.find()) {
            authorId = Integer.parseInt(matcher.group(1));
        }

        if (authorId != 0) {
            authorUser = entryManager.getUserById(authorId);
            if (authorUser == null) {
                throw new IllegalArgumentException("Invalid user ID. Don't specify ID when creating user.");
            }
        } else {
            authorUser = entryManager.createUser(authorName);
            view.populateAuthorComboBox(entryManager.getUsers(), "last");

            if (databaseIO != null) databaseIO.insertAuthor(authorUser);
        }

        if (!authorUser.getName().equals(authorName)) {
            throw new IllegalArgumentException("Entered author name doesn't match existing user");
        }

        return authorUser;
    }
}
