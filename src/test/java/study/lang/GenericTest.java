package study.lang;

import com.alibaba.fastjson2.JSON;
import org.junit.Test;
import study.bean.GenericBean;
import study.bean.GenericImpl;

public class GenericTest {
    @Test
    public void sedeTest() {
        var jsonStr = """
            {"name":"sede", "inner": {"order":{"name":"子曰"}}}
            """;
        // 内部类使用外部类的泛型参数，也能被解析
        var data = JSON.parseObject(jsonStr, GenericBean.class);
        System.out.println(data.getName());
        System.out.println(data.getInner().getOrder());
//        var order = data.getInner().getOrder();
//        System.out.println(order);

//        var json2 = """
//            {"name":"sede", "innerStatic": {"order":{"name":"子曰static"}}}""";
//        var staticOrder = JSON.parseObject(json2, GenericBean.class);
//        System.out.println(staticOrder.getInnerStatic().getOrder().who());
    }

    @Test
    public void innerGenericTest() {
        var json = """
            {"name":"test", "data":{"name":"子曰"}}
            """;
        var bean = new GenericImpl();
        var data = bean.parseGenericJson(json);
        System.out.println(data.who());
    }

    @Test
    public void genericParamTest() {
        var gb = new GenericBean();
        System.out.println(gb.getInnerClazz());
    }
}
