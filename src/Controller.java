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
    }

    private class CreateEntryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String authorRaw = view.getAuthorName();
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
                        throw new IllegalArgumentException("invalid user id");
                    }
                } else {
                    authorUser = entryManager.createUser(authorName);
                    view.populateAuthorComboBox(entryManager.getUsers(), true);
                }

                entryManager.createEntry(view.getNewEntryContent(), authorUser);
                view.populateEntriesTable(entryManager.getEntries());
            }
            catch (Exception ex) {
                System.out.println(ex);
                view.displayErrorMessage("Oops! Something went wrong.", "Error");
            }
        }
    }
}
