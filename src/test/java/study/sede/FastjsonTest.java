package study.sede;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import easezhi.study.io.FileUtil;
import lombok.Data;
import org.junit.Test;
import study.sede.model.PersonListDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FastjsonTest {
    @Test
    public void testParse() {
        String jsonFile = "./resource/person-list.json";
        String jsonStr = FileUtil.readFileToString(jsonFile);
        var perDto = JSON.parseObject(jsonStr, PersonListDto.class);
        var persons = perDto.getPersonList();
        System.out.println(persons);
    }

    @Test
    public void testJson() {
        String jsonStr = """
            {"dd":"2022-09-16T03:09:38.753"}
            """;
        TmpModel model = JSON.parseObject(jsonStr, TmpModel.class);
        System.out.println(model);
    }
}

@Data
class TmpModel {
    @JSONField(format = "yyyy-MM-ddTHH:mm:ss.sss")
    LocalDateTime dd;
}
