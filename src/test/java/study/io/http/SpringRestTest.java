package study.io.http;

import easezhi.study.io.http.SpringRest;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class SpringRestTest {
    @Test
    public void testRestTemplate() {
        Map<String, String> param = new HashMap<>();
        var resStr = SpringRest.postJson("http://localhost:8080/demo/json", param);
        System.out.println(resStr);
    }
}
