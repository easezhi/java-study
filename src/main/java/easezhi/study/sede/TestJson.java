package easezhi.study.sede;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;

import java.time.LocalDateTime;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import easezhi.study.io.FileUtil;
import lombok.Data;

public class TestJson {
    public static void main( String[] args ) {
        var file = "C:\\Users\\easezhi\\Desktop\\json.json";
        var jsonStr = FileUtil.readFileToString(file);
//        var vo = JSON.parseObject(jsonStr, JsonVo.class);
        var vo = JSONUtil.toBean(jsonStr, JsonVo.class);
//        var obj = JSON.parseObject(jsonStr);
//        var vo = JSON.toJavaObject(obj, JsonVo.class);
//        for (Person p: vo.persons) {
//            System.out.println(p.getName() + "-" + p.getDate());
//        }
        System.out.println(JSONUtil.parseObj(vo.getPersons().get(0)));
        System.out.println(JSON.toJSONString(vo.getPersons().get(0)));
    }
}

@Data
class Person {
    String name;
//    @JSONField(name = "age")
    Integer age;
    @JSONField(name = "age")
    Integer aga;
    String ss;
    String xx;
    public void setXx(String v) {
        xx = v;
    }

    @JSONField(format="yyyy-MM-dd")
    LocalDateTime date;
}

@Data
class SubPerson extends Person {
    @JSONField(name = "sex")
    String ss;
    @JSONField(name = "age")
    Integer cc;
    @JSONField(name = "age")
    Integer age;
}

@Data
class JsonVo {
    String title;
    List<Person> persons;
}
