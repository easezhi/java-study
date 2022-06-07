package easezhi.study.io;

import java.io.FileInputStream;

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
}
