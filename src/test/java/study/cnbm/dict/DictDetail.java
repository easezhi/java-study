package study.cnbm.dict;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import easezhi.study.io.FileUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class DictDetail {
    String label;

    String value;

    public DictDetail(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public List<DictDetail> getDictFromJson() {
        var file = "test\\resources\\cnbm\\dict_detail.json";
        var jsonStr = FileUtil.readFileToString(file);
        var model = JSON.parseObject(jsonStr, DictModel.class);
        return model.getDicts();
    }

    public Map<String, String> dictTransMap(List<DictDetail> dicts) {
        var map = new HashMap<String, String>();
        dicts.forEach(dict -> map.put(dict.getValue(), dict.getLabel()));
        return map;
    }
}

@Data
class DictModel {
    @JSONField(name = "sys_dict_detail")
    List<DictDetail> dicts;
}
