package easezhi.study.io;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;

public class FileUtil {
    public static String readFileToString(String filePath) {
        try {
            var is = new FileInputStream(filePath);
            var text = IOStreamUtil.readStreamToStr(is);
            is.close();
            return text;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
}
