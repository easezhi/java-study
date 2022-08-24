package study.sede;

import com.alibaba.fastjson2.JSON;
import easezhi.study.io.FileUtil;
import org.junit.Test;
import study.sede.model.PersonListDto;

public class FastjsonTest {
    @Test
    public void testParse() {
        String jsonFile = "./resource/person-list.json";
        String jsonStr = FileUtil.readFileToString(jsonFile);
        var perDto = JSON.parseObject(jsonStr, PersonListDto.class);
        var persons = perDto.getPersonList();
        System.out.println(persons);
    }
}
