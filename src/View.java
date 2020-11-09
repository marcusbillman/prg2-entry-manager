import javax.swing.*;

public class View {
    private JPanel panel;
    private JButton createEntryButton;
    private JTextArea textArea1;
    private JComboBox comboBox1;
    private JTable table1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Entry Manager");
        frame.setContentPane(new View().panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
