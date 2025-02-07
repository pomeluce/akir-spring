package org.pomeluce.akir.common.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author : lucas
 * @version : 1.0
 * @date : 2023/9/30上午10:39
 * @className : HttpEntity
 * @description : http 请求返回实体
 */
@Schema(description = "http 请求相应实体")
@Getter
@Setter
public class HttpEntity<R, T> implements Serializable {
    /* 请求结果状态码 */
    private @Schema(description = "状态码") int code;
    /* 请求结果信息 */
    private @Schema(description = "响应信息") String message;
    /* 请求结果数据 */
    private @Schema(description = "响应结果") R data;
    /* 请求结果信息体 */
    private @Schema(description = "响应体") Map<String, T> body = new HashMap<>();

    /**
     * HttpEntity 构造实例
     *
     * @param <R> 泛型为 R
     * @param <T> 泛型为 T
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public static <R, T> HttpEntity<R, T> instance() {
        return new HttpEntity<>();
    }

    /**
     * HttpEntity 构造实例
     *
     * @param status 请求结果状态码 {@link Integer}
     * @param <R>    泛型为 R
     * @param <T>    泛型为 T
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public static <R, T> HttpEntity<R, T> instance(int status) {
        return new HttpEntity<>(status);
    }

    /**
     * HttpEntity 构造实例
     *
     * @param status  请求结果状态码 {@link Integer}
     * @param message 请求结果信息 {@link String}
     * @param <R>     泛型为 R
     * @param <T>     泛型为 T
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public static <R, T> HttpEntity<R, T> instance(int status, String message) {
        return new HttpEntity<>(status, message);
    }

    /**
     * HttpEntity 构造实例
     *
     * @param status  请求结果状态码 {@link Integer}
     * @param message 请求结果信息 {@link String}
     * @param data    请求结果数据 {@link R}
     * @param <R>     泛型为 R
     * @param <T>     泛型为 T
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public static <R, T> HttpEntity<R, T> instance(int status, String message, R data) {
        return new HttpEntity<>(status, message, data);
    }

    /**
     * HttpEntity 构造实例
     *
     * @param status  请求结果状态码 {@link Integer}
     * @param message 请求结果信息 {@link String}
     * @param data    请求结果数据 {@link R}
     * @param body    请求结果信息体 {@link Map}
     * @param <R>     泛型为 R
     * @param <T>     泛型为 T
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public static <R, T> HttpEntity<R, T> instance(int status, String message, R data, Map<String, T> body) {
        return new HttpEntity<>(status, message, data, body);
    }

    /**
     * 添加结果信息
     *
     * @param message 请求结果信息 {@link String}
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public HttpEntity<R, T> set(String message) {
        this.message = message;
        return this;
    }

    /**
     * 添加结果数据
     *
     * @param data 请求结果数据 {@link R}
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public HttpEntity<R, T> put(R data) {
        this.data = data;
        return this;
    }

    /**
     * 添加 body 信息
     *
     * @param body 请求结果实体 {@link Map}
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public HttpEntity<R, T> put(HashMap<String, T> body) {
        this.body = body;
        return this;
    }

    /**
     * 添加结果信息和数据
     *
     * @param message 请求结果信息 {@link String}
     * @param data    请求结果数据 {@link R}
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public HttpEntity<R, T> put(String message, R data) {
        this.message = message;
        this.data = data;
        return this;
    }

    /**
     * 添加结果信息 和 body
     *
     * @param message 请求结果信息 {@link String}
     * @param body    请求结果实体 {@link Map}
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public HttpEntity<R, T> put(String message, HashMap<String, T> body) {
        this.message = message;
        this.body = body;
        return this;
    }

    /**
     * 添加结果信息, 数据 和 body
     *
     * @param message 请求结果信息 {@link String}
     * @param data    请求结果数据 {@link R}
     * @param body    请求结果实体 {@link Map}
     * @return 返回一个泛型为 R, T 的 HttpEntity 实体
     */
    public HttpEntity<R, T> put(String message, R data, HashMap<String, T> body) {
        this.message = message;
        this.data = data;
        this.body = body;
        return this;
    }

    private HttpEntity() {
    }

    private HttpEntity(int code) {
        this.code = code;
    }

    private HttpEntity(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private HttpEntity(int code, String message, R data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    private HttpEntity(int code, String message, R data, Map<String, T> body) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HttpEntity<?, ?> that)) return false;

        if (code != that.code) return false;
        if (!Objects.equals(message, that.message)) return false;
        if (!Objects.equals(data, that.data)) return false;
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (data != null ? data.hashCode() : 0);
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HttpEntity{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", body=" + body +
                '}';
    }
}
