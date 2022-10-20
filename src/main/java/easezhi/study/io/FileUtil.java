package easezhi.study.io;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileUtil {
    public static String readFileToString(String filePath) {
        try {
            return new String(Files.readAllBytes(Path.of(filePath)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<String> readLinesFromFile(String filePath) {
        try (var is = new FileInputStream(filePath);) {
            return IOStreamUtil.readStreamToLines(is);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void writeStringToFile(String filePath, String text) {
        try (var os = new FileWriter(filePath)) {
            os.write(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeLinesToFile(String filePath, List<String> lines) {
        try (var os = new FileWriter(filePath)) {
            for (var line: lines) {
                os.write(line);
                os.write("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
