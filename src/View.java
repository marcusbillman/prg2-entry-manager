import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View {
    private JFrame frame;
    private JPanel panel;
    private JButton createEntryButton;
    private JTextArea newEntryTextArea;
    private JComboBox authorComboBox;
    private JTable entriesTable;

    View(String title) {
        this.frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public String getNewEntryContent() {
        return newEntryTextArea.getText();
    }

    public String getAuthorName() {
        return (String) authorComboBox.getSelectedItem();
    }

    public void addCreateEntryListener(ActionListener actionListener) {
        createEntryButton.addActionListener(actionListener);
    }

    public void populateAuthorComboBox(ArrayList<User> authors) {
        Object selectedItem = authorComboBox.getSelectedItem();

        authorComboBox.removeAllItems();
        for (User author : authors) {
            authorComboBox.addItem(author.getName() + " (" + author.getId() + ")");
        }

        if (selectedItem != null) {
            authorComboBox.setSelectedItem(selectedItem);
        } else {
            authorComboBox.setSelectedIndex(0);
        }
    }

    // TODO: populateEntriesTable()

    void displayErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
