import java.io.*;
import java.util.Scanner;

public class WorkWithFile {
    public static File pathToFile(String path) {
        return new File(path);
    }

    public static void saveString(String path, String line) throws IOException {
        FileWriter writer = new FileWriter(new File(path));
        writer.write(line);
        writer.flush();
        writer.close();
    }

    public static String pathToString(String path) throws FileNotFoundException {
        FileReader reader = new FileReader(new File(path));
        Scanner scanFileReader = new Scanner(reader);
        return scanFileReader.nextLine();
    }
}
