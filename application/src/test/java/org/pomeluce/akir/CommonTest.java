package org.pomeluce.akir;

import org.apache.hc.core5.http.HttpVersion;
import org.junit.jupiter.api.Test;
import org.pomeluce.akir.common.config.AkirEnvironment;
import org.pomeluce.akir.common.core.http.HttpClient;
import org.pomeluce.akir.common.core.http.HttpMethod;
import org.pomeluce.akir.common.core.http.HttpResult;
import org.pomeluce.akir.common.utils.JacksonUtils;
import org.pomeluce.akir.common.utils.StringUtils;
import org.pomeluce.akir.common.utils.location.IpAddrUtils;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/29上午11:58
 * @className : CommonTest
 * @description : common 模块测试类
 */
// @SpringBootTest
public class CommonTest {
    String body = "";

    public @Test void ipTest() {
        System.out.println(IpAddrUtils.getMultistageReverseProxyIp("192.168.1.100, 10.0.0.1, 203.0.113.5"));
    }

    public @Test void httpTest() {
        HttpResult ipInfo = HttpClient.instance(cfg -> cfg.httpVersion(HttpVersion.HTTP_2_0)).request(rb -> rb
                .method(HttpMethod.GET, "http://whois.pconline.com.cn/ipJson.jsp")
                .params(Map.of(
                        "json", "true",
                        "ip", "127.0.0.1"
                ))
        );
        // System.out.println(ipInfo);
        body = ipInfo.body();
        // HttpRequest instance = HttpRequest.instance(
        //         5000L,
        //         new HashMap<>() {
        //             {
        //                 put("Content-Type", "application/json");
        //             }
        //         }
        // );
        /*
            HttpResult result = instance.POST("/posts", new HashMap<>() {
                {
                    put("userId", "1");
                    put("title", "foo");
                    put("body", "bar");
                }
            });
        */
        // HttpResult put = instance.PUT("/posts/1", new HashMap<>() {
        //     {
        //
        //         put("userId", 1001);
        //         put("id", 1);
        //         put("title", "title is updated");
        //         put("body", "body is null");
        //     }
        // });
        // System.out.println(put);
        HttpClient http = HttpClient.instance(cfg -> cfg.baseUrl("http://jsonplaceholder.typicode.com").httpVersion(HttpVersion.HTTP_2_0));
        http.requestAsync(rb -> rb.method(HttpMethod.GET, "/posts/1")).thenApply(result -> {
            System.out.println("------------->");
            String code = result.statusCode() + result.message();
            System.out.println(code);
            System.out.println(result);
            return result;
        });
        System.out.println("running ....");
        // HttpResult result = instance.DELETE("/posts/1");
    }

    public @Test void propertyTest() {
        boolean[] arr = AkirEnvironment.instance.get("spring.security.enableds", boolean[].class);
        System.out.println(Arrays.toString(arr));
    }

    public @Test void localTest() {
        Locale zhCn = Locale.of("zh", "CN");
        System.out.println(zhCn.equals(Locale.SIMPLIFIED_CHINESE));
        Locale zh = Locale.of("zh");
        System.out.println(zh.equals(Locale.CHINESE));
        // System.out.println(zhCn.equals(Locale.SIMPLIFIED_CHINESE));
        // System.out.println(zhCn.equals(Locale.CHINESE));
    }

    public @Test void stringTest() {
        System.out.println(StringUtils.format("{{}} 测试 \\\\\\{}", "hello", "world"));
    }

    public @Test void jacksonTest() {
        httpTest();
        System.out.println(JacksonUtils.fromJson(body, Map.class));
    }
}
