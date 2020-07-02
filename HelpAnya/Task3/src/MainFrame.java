import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;

public class MainFrame extends JFrame {

    private int[][] arrays;

    private JTable mainTable;
    private JButton goButton;
    private JButton openButton;
    private JButton saveButton;
    private JPanel mainPanel;
    private JTable table;

    public MainFrame() {
        super("Task 4");
        this.setContentPane(mainPanel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);

        JTableUtils.initJTableForArray(mainTable, 70, true, true, false, true);
        JTableUtils.initJTableForArray(table, 70, true, true, false, true);

        openButton.addActionListener(new ActionListener() {
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
                        arrays = WorkWithFile.fileTo2ArrayInt(nameFile);
                        JTableUtils.writeArrayToJTable(mainTable, arrays);
                    } catch (NullPointerException | FileNotFoundException | NumberFormatException e ) {
                        JOptionPane.showMessageDialog(null, "Ошибка загрузки файла!", "Output", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }
        });
        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                arrays = JTableUtils.readIntMatrixFromJTable(mainTable);

                MyQueue<Integer> queueFirst = new MyImplementationQueue<>();
                MyQueue<Integer> queueSecond = new MyImplementationQueue<>();

                for(int i = 0; i < arrays[0].length; i++){
                    queueFirst.enqueue(arrays[0][i]);
                }

                for(int i = 0; i < arrays[1].length; i++){
                    queueSecond.enqueue(arrays[1][i]);
                }

                UtilsQueue.addMyQueueLast(queueFirst, queueSecond);

                arrays = new int[2][queueFirst.size()];

                int counter = 0;

                while (!queueFirst.empty()){
                    arrays[0][counter] = queueFirst.dequeue();
                    arrays[1][counter] = queueSecond.dequeue();
                    counter++;
                }

                JTableUtils.writeArrayToJTable(table, arrays);
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });
    }
}
