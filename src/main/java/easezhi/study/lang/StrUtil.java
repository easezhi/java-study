package easezhi.study.lang;

import java.util.regex.Pattern;

public class StrUtil {

    public static boolean isLowerCase(String str) {
        var len = str.length();
        for (var i = 0; i < len; i++) {
            var c = str.charAt(i);
            if (c >= 'A' && c <= 'Z') return false;
        }
        return true;
    }

    // 查找第一个单词。暂且这么写
    public static String firstWord(String text) {
        var matcher = Pattern.compile("\\s?\\w+").matcher(text);
        if (matcher.find()) {
            return matcher.group().trim();
        } else {
            return null;
        }
    }
}
