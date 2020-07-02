import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

public class WorkWithFile {
    public static MyLinkedList readFromFileLinkedList(String nameFile) throws FileNotFoundException {
        FileReader fileReader = new FileReader(nameFile);
        Scanner scanFile = new Scanner(fileReader);
        MyLinkedList<Double> list = new MyLinkedList<>();
        StringTokenizer tokenizer;
        if (scanFile.hasNextLine()) {
            tokenizer = new StringTokenizer(scanFile.nextLine(), " ");
            while (tokenizer.hasMoreTokens()) {
                list.addLast(Double.parseDouble(tokenizer.nextToken()));
            }
        }
        return list;
    }

    public String[] fileToString(String nameFile) throws FileNotFoundException {
        FileReader fileReader = new FileReader(nameFile);
        Scanner scanFile = new Scanner(fileReader);
        List<String> listArray = new ArrayList<>();
        while (scanFile.hasNextLine()) {
            listArray.add(scanFile.nextLine());
        }
        return listArray.toArray(new String[0]);
    }

    public static int[] fileToArrayInt(String nameFile) throws FileNotFoundException {
        FileReader fileReader = new FileReader(nameFile);
        Scanner scanFile = new Scanner(fileReader);
        String[] array = scanFile.nextLine().split(" ");
        int[] newArray = new int[array.length];
        for(int i = 0; i < array.length;i++){
            newArray[i] = Integer.parseInt(array[i]);
        }
        return newArray;
    }

    public static int[][] fileTo2ArrayInt(String nameFile) throws FileNotFoundException {
        FileReader fileReader = new FileReader(nameFile);
        Scanner scanFile = new Scanner(fileReader);
        String[] arrayFirst = scanFile.nextLine().split(" ");
        String[] arraySecond = scanFile.nextLine().split(" ");
        int[][] newArray = new int[2][];

        newArray[0] = new int[arrayFirst.length];
        for(int i = 0; i < arrayFirst.length;i++){
            newArray[0][i] = Integer.parseInt(arrayFirst[i]);
        }

        newArray[1] = new int[arraySecond.length];

        for(int i = 0; i < arraySecond.length;i++){
            newArray[1][i] = Integer.parseInt(arraySecond[i]);
        }

        return newArray;
    }



    public static void saveLinkedListFromFile(MyLinkedList list, String path) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(path));
            for(Object value: list){
                bw.write(value.toString() + " ");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

