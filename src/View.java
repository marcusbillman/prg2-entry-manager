import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View {
    private JFrame frame;
    private JPanel panel;
    private JButton createEntryButton;
    private JTextArea newEntryTextArea;
    private JComboBox authorComboBox;
    private JTable entriesTable;
    private JButton saveToFileButton;
    private JButton loadFromFileButton;

    View(String title) {
        this.frame = new JFrame(title);
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();

        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("Content");
        tableModel.addColumn("Original Author");
        tableModel.addColumn("Creation Date");
        tableModel.addColumn("Modification Date");
        entriesTable.setModel(tableModel);
        entriesTable.setDefaultEditor(Object.class, null); // Disable editing for the table

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

    public void populateAuthorComboBox(ArrayList<User> authors, boolean selectLast) {
        Object selectedItem = authorComboBox.getSelectedItem();

        authorComboBox.removeAllItems();
        for (User author : authors) {
            authorComboBox.addItem(author.getName() + " (" + author.getId() + ")");
        }

        if (selectLast) {
            authorComboBox.setSelectedIndex(authorComboBox.getItemCount() - 1);
        } else if (selectedItem != null) {
            authorComboBox.setSelectedItem(selectedItem);
        } else {
            authorComboBox.setSelectedIndex(0);
        }
    }

    public void populateEntriesTable(ArrayList<Entry> entries) {
        DefaultTableModel tableModel = (DefaultTableModel) entriesTable.getModel();
        tableModel.setRowCount(0);

        for (Entry entry : entries) {
            tableModel.addRow(new String[] {
                    entry.getContent(),
                    entry.getOriginalAuthor().getName() + " (" + entry.getOriginalAuthor().getId() + ")",
                    entry.getCreationDate().toString(),
                    entry.getModificationDate().toString()
            });
        }
    }

    void displayErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(frame, message, title, JOptionPane.ERROR_MESSAGE);
    }
}
