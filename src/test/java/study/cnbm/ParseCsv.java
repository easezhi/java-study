package study.cnbm;

import easezhi.study.io.FileUtil;
import org.junit.Test;

import java.util.ArrayList;

public class ParseCsv {
    @Test
    public void genCodeArray() {
        var srcPath = "C:\\Users\\easezhi\\Downloads\\hwps.csv";
        var lines = FileUtil.readLinesFromFile(srcPath);
        var codes = new ArrayList<String>(lines.size());
        for (var line: lines) {
            var segs = line.split("\\|");
            if (segs.length > 1) {
                codes.add(segs[1].trim());
            }
        }
        var tpl = "var codes = ['" + String.join("','", codes) + "'];";
        var tarPath = "D:\\cnbm-work\\迭代\\排产需求7773\\purchase_contract_codes.js";
        FileUtil.writeStringToFile(tarPath, tpl);
        System.out.println("写入 " + codes.size());
    }
}
