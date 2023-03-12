package study.sede;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.annotation.JSONField;
import easezhi.study.io.FileUtil;
import lombok.Data;
import org.junit.Test;
import study.sede.model.PersonGenericDto;
import study.sede.model.PersonListDto;

import java.math.BigDecimal;
import java.time.*;

import static org.junit.Assert.*;

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
            {"dz":"2022-06-21T06:21:10.862Z", "name":"xx", "birth":"2023-01-01 10:20:11", "amt": "1122.3"}
            """;
        TmpModel model = JSON.parseObject(jsonStr, TmpModel.class);
        System.out.println(model);
        System.out.println(model.getDd());
//        System.out.println(model.getDz().withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
//        System.out.println(model.getDoff().toLocalDateTime());
        System.out.println(model.getBirth());
    }

    @Test
    public void testGeneric() {
        var jsonStr = """
            {"one":{"name":"张三"}, "beanList":[{"name":"李四"}]}
            """;
        var model = JSON.parseObject(jsonStr, PersonGenericDto.class);
        System.out.println("嵌套的泛型类型也能反序列化");
        assertEquals(model.getOne().getName(), "张三");
        assertEquals(model.getBeanList().get(0).getName(), "李四");
    }
}

@Data
class TmpModel{
    @JSONField(format = "yyyy-MM-ddTHH:mm:ss")
    LocalDateTime dd;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    LocalDate birth;

    ZonedDateTime dz;

//    OffsetDateTime doff;

    String name;

    TmpModel sib;

    BigDecimal amt;
}
