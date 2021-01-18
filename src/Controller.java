import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Acts as an MVC controller that handles all communication between View (MVC view) and EntryManager (MVC model). Also
 * holds an instance of DatabaseIO.
 */
public class Controller {
    private final View view;
    private EntryManager entryManager;
    private DatabaseIO databaseIO;

    /**
     * Constructor for Controller.
     * @param entryManager MVC model that stores all state data, including entries and users
     * @param view MVC view that handles UI
     * @param useDatabase boolean that decides whether to use database functionality
     */
    public Controller(EntryManager entryManager, View view, boolean useDatabase) {
        this.entryManager = entryManager;
        this.view = view;
        if (useDatabase) this.databaseIO = new DatabaseIO();

        this.view.addCreateEntryListener(new CreateEntryListener());
        this.view.addSaveListener(new SaveListener());
        this.view.addLoadListener(new LoadListener());
        this.view.addTableClickListener(new TableClickListener());
        this.view.addCloseListener(new WindowListener() {
            public void windowOpened(WindowEvent e) {
            }

            /**
             * Exits the program safely when the window is closed by the user. Closes the database connection.
             * @param e event that invokes the listener (by default closing the window)
             */
            public void windowClosing(WindowEvent e) {
                databaseIO.closeConnection();
                System.out.println("Bye!");
                view.getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }

            public void windowClosed(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            public void windowDeiconified(WindowEvent e) {
            }

            public void windowActivated(WindowEvent e) {
            }

            public void windowDeactivated(WindowEvent e) {
            }
        });

        if (databaseIO != null) {
            try {
                this.entryManager = databaseIO.load();
            } catch (Exception ex) {
                view.showMessageDialog(
                        "Couldn't load data from database. This session will only use manual file storage.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                this.databaseIO = null;
                return;
            }

            try {
                this.view.populateEntriesTable(this.entryManager.getEntries());
                this.view.populateAuthorComboBox(this.entryManager.getUsers(), "first");
            } catch (IllegalArgumentException ignored) {
            }

            this.view.showMessageDialog("Loaded state from database", "Loaded",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Custom listener based on ActionListener that, when invoked, creates an entry and, if database is used, inserts
     * the entry into the database.
     */
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
            } catch (Exception ex) {
                ex.printStackTrace();
                view.showMessageDialog(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Custom listener based on ActionListener that, when invoked, lets the user save the current state of EntryManager
     * to a file.
     */
    private class SaveListener implements ActionListener {
        /**
         * Opens a file chooser that lets the user choose a file to save to. Calls the save() method of FileIO.
         * @param e event that invokes the listener (by default a click of the Save button)
         */
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
            } catch (Exception ex) {
                ex.printStackTrace();
                view.showMessageDialog(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Custom listener based on ActionListener that, when invoked, lets the user restore the state of EntryManager from
     * a file.
     */
    private class LoadListener implements ActionListener {
        /**
         * Opens a file chooser that lets the user choose a file to load from. Calls the load() method of FileIO and
         * updates the UI.
         * @param e event that invokes the listener (by default a click of the Load button)
         */
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
            } catch (Exception ex) {
                ex.printStackTrace();
                view.showMessageDialog(ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Custom listener based on ActionListener that, when invoked, lets the user edit an entry through the UI.
     */
    private class TableClickListener implements MouseListener {
        /**
         * Opens an input dialog that lets the user edit the content of the entry that was clicked. Modifies the entry,
         * updates the UI and updates the entry in the database.
         * @param mouseEvent event that invokes the listener (by default a double-click in the entries table)
         */
        public void mouseClicked(MouseEvent mouseEvent) {
            try {
                JTable table = (JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int index = table.rowAtPoint(point);

                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Entry entry = entryManager.getEntries().get(index);

                    String newContent = view.showInputDialog("Entry Content", "Modify Entry",
                            entry.getContent());

                    if (newContent == null) return;
                    if (newContent.length() < 1) throw new IllegalArgumentException("Entry content is empty");

                    entry.modify(newContent);
                    view.populateEntriesTable(entryManager.getEntries());
                    view.clearNewEntryContent();

                    if (databaseIO != null) databaseIO.updateEntry(entry);
                }
            } catch (Exception ex) {
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

    /**
     * Parses the contents of the author input field and finds the corresponding User in EntryManager. If no User is
     * found, a new one gets created.
     * @return corresponding User
     */
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
