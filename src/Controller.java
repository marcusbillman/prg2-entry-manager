import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                // TODO: Create the entry
            }
            catch (Exception ex) {
                System.out.println(ex);
                view.displayErrorMessage("Oops! Something went wrong.", "Error");
            }
        }
    }
}
