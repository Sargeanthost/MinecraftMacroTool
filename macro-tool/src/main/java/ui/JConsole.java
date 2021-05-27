package ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import logic.CommandHelper;
import logic.Main;
import ext.TextAreaOutputStream;

public class JConsole extends JFrame {

    private final JTextField textField;
    private final TextAreaOutputStream textOutput;

    //TODO refactor this into sub methods
    public JConsole(String title) {
        super(title);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e1) {
            e1.printStackTrace();
        }
        setForeground(Color.BLACK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 860, 480);
        JPanel contentPane = new JPanel();
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setBorder(null);
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
        contentPane.setLayout(gbl_contentPane);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 0;
        contentPane.add(scrollPane, gbc_scrollPane);

        JTextArea textArea = new JTextArea();
        scrollPane.setViewportView(textArea);
        textArea.setForeground(Color.WHITE);
        textArea.setFont(new Font("Consolas", Font.PLAIN, 14));
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setFocusable(false);

        textField = new JTextField();
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    CommandHelper.input = textField.getText();
                    textField.setText("");
                }
            }
        });
        textField.setForeground(Color.WHITE);
        textField.setFont(new Font("Consolas", Font.PLAIN, 14));
        textField.setBackground(Color.BLACK);
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.BOTH;
        gbc_textField.gridx = 0;
        gbc_textField.gridy = 1;
        contentPane.add(textField, gbc_textField);
        textOutput = new TextAreaOutputStream(textArea);
        PrintStream ps = new PrintStream(textOutput);
        textField.requestFocusInWindow();
        textField.getCaret().setVisible(true);
        textField.setCaretColor(Color.WHITE);
        System.setOut(ps);
        System.setErr(ps);
    }

    /**
     * Make a call to close the JFrame.
     * Only use when an error is raised in which the program can't recover from.
     * @param timeMs The time in milliseconds for the program to freeze; gives time to read errors.
     */
    public void closeFrame(int timeMs) {
        System.out.println("Closing in " + timeMs / 1000 + " seconds...");
        try {
            Thread.sleep(timeMs);
        } catch (InterruptedException e) {
            //just close
        }
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    public TextAreaOutputStream getTextOutput() {
        return textOutput;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JConsole frame = new JConsole(Main.versionName);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
