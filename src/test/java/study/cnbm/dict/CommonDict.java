package study.cnbm.dict;

import java.util.HashMap;

public class CommonDict {
    // 公司主体
    public static HashMap<String, String> company;

    static {
        company = new HashMap<>();
        company.put("1000", "中建材信息技术股份有限公司");
        company.put("1100", "中建材信云智联科技有限公司");
        company.put("1200", "中建材信云智联科技有限公司北京分公司");
        company.put("9100", "中建材信息技术（香港）有限公司");
        company.put("2100", "博瑞夏信息技术（北京）有限公司");
    }
}
