package easezhi.study.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class IOStreamUtil {
    public static String readStreamToStr(InputStream is) {
        return readStreamToStr(is, StandardCharsets.UTF_8);
    }

    public static String readStreamToStr(InputStream is, Charset charset) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
        StringBuilder builder = new StringBuilder();
        reader.lines().forEach(builder::append);
        return builder.toString();
    }
}
