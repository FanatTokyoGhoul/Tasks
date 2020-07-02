import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Frame extends JFrame {
    private JTextField textFieldBracket;
    private JButton treeButton;
    private JTextArea textAreaSystemOutPrint;
    private JButton reverseButton;
    private JPanel mainPanel;
    private JButton loadButton;
    private JButton saveButton;

    private Tree tree;

    public Frame() {
        super("Task5");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));
        this.pack();
        this.setVisible(true);
        textFieldBracket.setText("8 (6,5,7(9, 10, 11))");

        tree = new Tree(s -> Integer.parseInt(s.toString()));

        treeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    tree.fromBracketNotation(textFieldBracket.getText());
                } catch (Exception e) {
                    showErrorMessageBox("Ошибка!", null, e);
                }
                showSystemOut(() -> {
                    tree.preOrderVisit((value, level) -> {
                        printLevelSpace(level);
                        System.out.println(value);
                    });
                });
            }
        });

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileOpen = new JFileChooser();
                fileOpen.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("File(.txt)", "txt");
                fileOpen.addChoosableFileFilter(filter);
                int ret = fileOpen.showDialog(null, "Сохранить в файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileOpen.getSelectedFile();
                    String nameFile = file.getPath();
                    try {
                        WorkWithFile.saveString(nameFile, textFieldBracket.getText());
                    } catch (NullPointerException | NumberFormatException | IOException e) {
                        JOptionPane.showMessageDialog(null, "Ошибка загрузки файла!", "Output", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFileChooser fileOpen = new JFileChooser();
                fileOpen.setAcceptAllFileFilterUsed(false);
                FileNameExtensionFilter filter = new FileNameExtensionFilter("File(.txt)", "txt");
                fileOpen.addChoosableFileFilter(filter);
                int ret = fileOpen.showDialog(null, "Открыть файл");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = fileOpen.getSelectedFile();
                    String nameFile = file.getPath();
                    try {
                        String text = WorkWithFile.pathToString(nameFile);
                        textFieldBracket.setText(text);
                        tree.fromBracketNotation(text);
                        showSystemOut(() -> {
                            tree.preOrderVisit((value, level) -> {
                                printLevelSpace(level);
                                System.out.println(value);
                            });
                        });
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка загрузки файла!", "Output", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });
        reverseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tree.reverseTree();

                showSystemOut(() -> {
                    tree.preOrderVisit((value, level) -> {
                        printLevelSpace(level);
                        System.out.println(value);
                    });
                });
            }
        });
    }

    private void printLevelSpace(int index) {
        for (int i = 0; i < index; i++) {
            System.out.print("    ");
        }
    }

    private void showSystemOut(Runnable action) {
        PrintStream oldOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos, true, "UTF-8"));

            action.run();

            textAreaSystemOutPrint.setText(baos.toString("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            showErrorMessageBox("Ошибка", null, e);
        }
        System.setOut(oldOut);
    }

    public static void showErrorMessageBox(String message, String title, Throwable ex) {
        StringBuilder sb = new StringBuilder(ex.toString());
        if (message != null) {
            sb.append(message);
        }
        if (ex != null) {
            sb.append("\n");
            for (StackTraceElement ste : ex.getStackTrace()) {
                sb.append("\n\tat ");
                sb.append(ste);
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString(), title, JOptionPane.ERROR_MESSAGE);
    }
}
