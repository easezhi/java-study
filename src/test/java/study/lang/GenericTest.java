package study.lang;

import com.alibaba.fastjson2.JSON;
import org.junit.Test;
import study.bean.GenericBean;

public class GenericTest {
    @Test
    public void sedeTest() {
        var jsonStr = """
            {"name":"sede", "inner": {"order":{"name":"xxx"}}}
            """;
        // 内部类使用外部类的泛型参数，也能被解析
        var data = JSON.parseObject(jsonStr, GenericBean.class);
        System.out.println(data);
        System.out.println(data.getName());
        System.out.println(data.getInner().getOrder());
    }
}
