package org.pomeluce.meagle.common.core.http;

import org.pomeluce.meagle.common.utils.JacksonUtils;
import org.pomeluce.meagle.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/10/5 20:06
 * @className : HttpRequest
 * @description : Http 请求对象
 */
public class HttpRequest {
    private String baseURL;
    private long timeout = 5000L;
    private String method = "GET";
    private Map<String, String> headers = HashMap.newHashMap(0);
    private java.net.http.HttpRequest.Builder instance = java.net.http.HttpRequest.newBuilder();
    private final String[] methods = {"GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS", "TRACE"};
    private final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    private HttpRequest() {
    }

    public static HttpRequest instance() {
        return new HttpRequest();
    }

    public static HttpRequest instance(String baseURL) {
        HttpRequest request = new HttpRequest();
        request.baseURL = baseURL;
        return request;
    }

    public static HttpRequest instance(long timeout) {
        HttpRequest request = new HttpRequest();
        request.timeout = timeout;
        return request;
    }

    public static HttpRequest instance(Map<String, String> headers) {
        HttpRequest request = new HttpRequest();
        request.headers = headers;
        return request;
    }

    public static HttpRequest instance(String baseURL, long timeout) {
        HttpRequest request = new HttpRequest();
        request.baseURL = baseURL;
        request.timeout = timeout;
        return request;
    }

    public static HttpRequest instance(String baseURL, Map<String, String> headers) {
        HttpRequest request = new HttpRequest();
        request.baseURL = baseURL;
        request.headers = headers;
        return request;
    }

    public static HttpRequest instance(long timeout, Map<String, String> headers) {
        HttpRequest request = new HttpRequest();
        request.timeout = timeout;
        request.headers = headers;
        return request;
    }

    public static HttpRequest instance(String baseURL, long timeout, Map<String, String> headers) {
        HttpRequest request = instance(baseURL, timeout);
        request.headers = headers;
        return request;
    }

    /**
     * 请求通用方法
     *
     * @param url     请求 url {@link String}
     * @param method  请求类型 {@link String}
     * @param body    请求体 {@link String}
     * @param headers 请求头 {@link Map}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult request(String url, String method, String body, Map<String, String> headers) {
        try (HttpClient client = HttpClient.newBuilder().build()) {
            if (Arrays.binarySearch(methods, this.method = method.toUpperCase()) == -1) {
                throw new IllegalStateException("HttpRequest.request IllegalStateException, 找不到或不支持 " + method + " 类型的请求");
            }

            instance = java.net.http.HttpRequest.newBuilder()
                    .uri(URI.create(StringUtils.isNotBlank(baseURL) ? baseURL + url : url))
                    .timeout(Duration.ofMillis(timeout))
                    .method(this.method, Objects.isNull(body)
                            ? java.net.http.HttpRequest.BodyPublishers.noBody()
                            : java.net.http.HttpRequest.BodyPublishers.ofString(body)
                    );

            if (headers != null) this.headers.putAll(headers);
            this.headers.forEach((k, v) -> instance.header(k, v));

            HttpResponse<String> response = client.send(instance.build(), HttpResponse.BodyHandlers.ofString());
            log.info("HttpRequest method {} is success, response { statusCode: {}, body {} }", this.method, response.statusCode(), response.body());
            return new HttpResult(Objects.requireNonNull(HttpStatus.resolve(response.statusCode())), response.body());
        } catch (IOException | InterruptedException | IllegalStateException e) {
            log.error("HttpRequest method {} is {}, url:{}, body: {}", method, e.getClass().getSimpleName(), url, body, e);
            return new HttpResult(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    /**
     * 请求通用方法
     *
     * @param url 请求 url {@link String}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult request(String url) {
        return request(url, this.method, null, this.headers);
    }

    /**
     * 请求通用方法
     *
     * @param url    请求 url {@link String}
     * @param method 请求类型 {@link String}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult request(String url, String method) {
        return request(url, method, null, this.headers);
    }

    /**
     * 请求通用方法
     *
     * @param url     请求 url {@link String}
     * @param method  请求类型 {@link String}
     * @param headers 请求头 {@link Map}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult request(String url, String method, Map<String, String> headers) {
        return request(url, method, null, headers);
    }

    /**
     * 请求通用方法
     *
     * @param url    请求 url {@link String}
     * @param method 请求类型 {@link String}
     * @param body   请求体 {@link String}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult request(String url, String method, String body) {
        return request(url, method, body, this.headers);
    }

    /**
     * get 请求
     *
     * @param url 请求 url {@link String}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult GET(String url) {
        return request(url, "GET");
    }

    /**
     * get 请求
     *
     * @param url     请求 url {@link String}
     * @param headers 请求头 {@link Map}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult GET(String url, Map<String, String> headers) {
        return request(url, "GET", headers);
    }

    /**
     * post 请求
     *
     * @param url  请求 url {@link String}
     * @param body 请求参数 {@link Object}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult POST(String url, Object body) {
        return request(url, "POST", JacksonUtils.toJsonString(body));
    }

    /**
     * post 请求
     *
     * @param url     请求 url {@link String}
     * @param body    请求参数 {@link Object}
     * @param headers 请求头 {@link Map}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult POST(String url, Object body, Map<String, String> headers) {
        return request(url, "POST", JacksonUtils.toJsonString(body), headers);
    }

    /**
     * put 请求
     *
     * @param url  请求 url {@link String}
     * @param body 请求参数 {@link Object}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult PUT(String url, Object body) {
        return request(url, "PUT", JacksonUtils.toJsonString(body));
    }

    /**
     * put 请求
     *
     * @param url     请求 url {@link String}
     * @param body    请求参数 {@link Object}
     * @param headers 请求头 {@link Map}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult PUT(String url, Object body, Map<String, String> headers) {
        return request(url, "PUT", JacksonUtils.toJsonString(body), headers);
    }

    /**
     * delete 请求
     *
     * @param url 请求 url {@link String}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult DELETE(String url) {
        return request(url, "DELETE");
    }

    /**
     * delete 请求
     *
     * @param url     请求 url {@link String}
     * @param headers 请求头 {@link Map}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult DELETE(String url, Map<String, String> headers) {
        return request(url, "DELETE", headers);
    }

    /**
     * 表单请求
     *
     * @param url    请求 url {@link String}
     * @param params 请求参数 {@link Map}
     *               例如: key1=value1&key2=value2
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult FORM(String url, Map<String, Object> params) {
        var builder = new StringBuilder("&");
        params.forEach((key, value) -> builder
                .append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8)));
        return request(url, "POST", builder.toString());
    }

    /**
     * 表单请求
     *
     * @param url     请求 url {@link String}
     * @param params  请求参数 {@link Map}
     *                例如: key1=value1&key2=value2
     * @param headers 请求头 {@link Map}
     * @return 返回一个 HttpResult 类型的请求结果
     */
    public HttpResult FORM(String url, Map<String, Object> params, Map<String, String> headers) {
        var builder = new StringBuilder("&");
        params.forEach((key, value) -> builder
                .append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                .append("=")
                .append(URLEncoder.encode(value.toString(), StandardCharsets.UTF_8)));
        return request(url, "POST", builder.toString(), headers);
    }
}
