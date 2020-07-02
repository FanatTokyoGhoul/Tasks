import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;

public class Frame extends JFrame {
    private JTextField textFieldBracket;
    private JButton treeButton;
    private JTextArea textAreaSystemOutPrint;
    private JPanel mainPanel;
    private JButton loadButton;
    private JButton saveButton;
    private JTextField textFieldSum;


    public Frame() {
        super("Task5");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(500, 500));
        this.pack();
        this.setVisible(true);
        textFieldBracket.setText("1 2 7 8 3 4 10");
        textFieldSum.setText("7");


        treeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<int[]> list = null;
                try {
                    list = Utils.searchForThreeTerms(stringToIntMatrix(textFieldBracket.getText()), Integer.parseInt(textFieldSum.getText()));
                } catch (Exception e) {
                    showErrorMessageBox("Ошибка!", null, e);
                }
                if (list != null) {
                    StringBuilder builder = new StringBuilder();

                    for (int[] array : list) {
                        builder.append(array[0] + " " + array[1] + " " + array[2] + "\n");
                    }

                    textAreaSystemOutPrint.setText(builder.toString());
                }
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
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Ошибка загрузки файла!", "Output", JOptionPane.PLAIN_MESSAGE);
                    }
                }
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

    private int[] stringToIntMatrix(String line) {
        String[] arrayString = line.split("[ ]");
        int[] arrayInt = new int[arrayString.length];
        for (int i = 0; i < arrayString.length; i++) {
            arrayInt[i] = Integer.parseInt(arrayString[i]);
        }
        return arrayInt;
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
