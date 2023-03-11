package easezhi.study.io.http;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson2.JSON;

public class HutoolHttp {

    public static String postJson(String url, Object data) {
        String body;
        if (data instanceof String) {
            body = (String) data;
        } else {
            body = JSON.toJSONString(data);
        }
        return HttpRequest.post(url)
            .body(body)
            .execute()
            .body();
    }

    public static <E> E postJson(String url, Object body, Class<E> clazz) {
        var rst = postJson(url, body);
        return JSON.parseObject(rst, clazz);
    }
}
