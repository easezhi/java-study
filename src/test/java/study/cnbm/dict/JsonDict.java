package study.cnbm.dict;

import com.alibaba.fastjson2.JSON;

import java.util.Map;

public class JsonDict {
    public static Map<String, Object> getPurchaseOrgMap() {
        var json = """
            {
                "1000" : "信息产品事业群",
                "1002" : "信息产品事业群港杂费采购组织",
                "2000" : "合作业务部",
                "2002" : "合作业务部港杂费采购组织",
                "3000" : "医疗产品部",
                "3002" : "医疗产品部港杂费采购组织",
                "4000" : "数字化事业群",
                "9000" : "进出口公司采购组织",
                "9001" : "中建材香港公司采购组织",
                "9002" : "进出口公司港杂费采购组织",
                "9199" : "公司间采购组织",
                "0001" : "Einkaufsorg. 0001"
            }
            """;
        return JSON.parseObject(json);
    }
}
