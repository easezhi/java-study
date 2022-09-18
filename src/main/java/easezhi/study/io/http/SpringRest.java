package easezhi.study.io.http;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

public class SpringRest {
    public static String postJson(String url, Object body) {
        RestTemplate restTemplate = new RestTemplate();
        var res = restTemplate.postForEntity(url, body, String.class);
        var httpStatus = res.getStatusCode();
        if (httpStatus == HttpStatus.OK) {
            return res.getBody();
        } else {
            throw new RuntimeException("请求失败");
        }
    }
}
