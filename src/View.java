import javax.swing.*;
import java.awt.event.ActionListener;

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

    void addCreateEntryListener(ActionListener actionListener) {
        createEntryButton.addActionListener(actionListener);
    }

    // TODO: populateAuthorComboBox()

    // TODO: populateEntriesTable()

    void displayErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
