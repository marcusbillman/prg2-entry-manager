import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private View view;
    private EntryManager entryManager;

    public Controller(EntryManager entryManager, View view) {
        this.entryManager = entryManager;
        this.view = view;

        this.view.addCreateEntryListener(new CreateEntryListener());
        this.view.addSaveListener(new SaveListener());
        this.view.addLoadListener(new LoadListener());
    }

    private class CreateEntryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String entryContent = view.getNewEntryContent();
                if (entryContent.length() < 1) throw new IllegalArgumentException("Entry content is empty");

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
                    view.populateAuthorComboBox(entryManager.getUsers(), true);
                }

                if (!authorUser.getName().equals(authorName)) {
                    throw new IllegalArgumentException("Entered author name doesn't match existing user");
                }

                entryManager.createEntry(view.getNewEntryContent(), authorUser);
                view.populateEntriesTable(entryManager.getEntries());
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
                String fileName = "EntryManagerDump.obj";
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
                String fileName = "EntryManagerDump.obj";
                entryManager = FileIO.load(fileName);
                view.populateEntriesTable(entryManager.getEntries());
                view.populateAuthorComboBox(entryManager.getUsers(), false);
                view.showMessageDialog("Loaded state from file:\n" + fileName, "Loaded",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                view.showMessageDialog(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
