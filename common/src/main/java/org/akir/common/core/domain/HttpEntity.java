package org.akir.common.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/30上午10:39
 * @className : HttpEntity
 * @description : http 请求返回实体
 */
@Schema(description = "http 请求相应实体")
@Getter
@Setter
public class HttpEntity<R> implements Serializable {
    /* 请求结果状态码 */
    private @Schema(description = "状态码") String code;
    /* 请求结果信息 */
    private @Schema(description = "响应信息") String message;
    /* 请求结果数据 */
    private @Schema(description = "响应结果") R data;

    /**
     * HttpEntity 构造实例
     *
     * @param <R> 泛型为 R
     * @return 返回一个泛型为 R 的 HttpEntity 实体
     */
    public static <R> HttpEntity<R> instance() {
        return new HttpEntity<>();
    }

    /**
     * HttpEntity 构造实例
     *
     * @param status 请求结果状态码 {@link String}
     * @param <R>    泛型为 R
     * @return 返回一个泛型为 R 的 HttpEntity 实体
     */
    public static <R> HttpEntity<R> instance(String status) {
        return new HttpEntity<>(status);
    }

    /**
     * HttpEntity 构造实例
     *
     * @param status  请求结果状态码 {@link String}
     * @param message 请求结果信息 {@link String}
     * @param <R>     泛型为 R
     * @return 返回一个泛型为 R 的 HttpEntity 实体
     */
    public static <R> HttpEntity<R> instance(String status, String message) {
        return new HttpEntity<>(status, message);
    }

    /**
     * HttpEntity 构造实例
     *
     * @param status  请求结果状态码 {@link String}
     * @param message 请求结果信息 {@link String}
     * @param data    请求结果数据 {@link R}
     * @param <R>     泛型为 R
     * @return 返回一个泛型为 R 的 HttpEntity 实体
     */
    public static <R> HttpEntity<R> instance(String status, String message, R data) {
        return new HttpEntity<>(status, message, data);
    }

    /**
     * 添加结果信息
     *
     * @param message 请求结果信息 {@link String}
     * @return 返回一个泛型为 R 的 HttpEntity 实体
     */
    public HttpEntity<R> set(String message) {
        this.message = message;
        return this;
    }

    /**
     * 添加结果数据
     *
     * @param data 请求结果数据 {@link R}
     * @return 返回一个泛型为 R 的 HttpEntity 实体
     */
    public HttpEntity<R> put(R data) {
        this.data = data;
        return this;
    }

    /**
     * 添加结果信息和数据
     *
     * @param message 请求结果信息 {@link String}
     * @param data    请求结果数据 {@link R}
     * @return 返回一个泛型为 R 的 HttpEntity 实体
     */
    public HttpEntity<R> put(String message, R data) {
        this.message = message;
        this.data = data;
        return this;
    }

    private HttpEntity() {
    }

    private HttpEntity(String code) {
        this.code = code;
    }

    private HttpEntity(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private HttpEntity(String code, String message, R data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        HttpEntity<?> that = (HttpEntity<?>) o;
        return Objects.equals(code, that.code) && Objects.equals(message, that.message) && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(code);
        result = 31 * result + Objects.hashCode(message);
        result = 31 * result + Objects.hashCode(data);
        return result;
    }

    @Override
    public String toString() {
        return "HttpEntity{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
