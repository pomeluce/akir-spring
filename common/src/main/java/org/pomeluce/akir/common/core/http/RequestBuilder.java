package org.pomeluce.akir.common.core.http;

import lombok.Getter;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.pomeluce.akir.common.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-07 19:20
 * @className : RequestBuilder
 * @description : HTTP 请求构造对象
 */
public class RequestBuilder {
    private HttpMethod method = HttpMethod.GET;
    private String path;
    private final @Getter Map<String, String> headers = new HashMap<>();
    private final Map<String, String> params = new HashMap<>();
    private byte[] body;
    private boolean binary;
    private ContentType contentType = ContentType.APPLICATION_JSON;

    protected boolean isBinary() {
        return binary;
    }

    /**
     * Specifies the HTTP method and resource path.
     * Must be called before build().
     *
     * @param method HTTP verb (GET, POST, etc.)
     * @param path   resource path (e.g. "/users")
     * @return this {@code RequestBuilder}
     */
    public RequestBuilder method(HttpMethod method, String path) {
        this.method = method;
        this.path = path;
        return this;
    }

    /**
     * Adds a request header.
     *
     * @param name  header name
     * @param value header value
     * @return this {@code RequestBuilder}
     */
    public RequestBuilder header(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    /**
     * Adds URL query parameters.
     *
     * @param params map of key->value
     * @return this RequestBuilder
     */
    public RequestBuilder params(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    /**
     * Attaches a text body with specified content type.
     *
     * @param text        the string content
     * @param contentType MIME type (e.g. "application/json")
     * @return this {@code RequestBuilder}
     */
    public RequestBuilder textBody(String text, ContentType contentType) {
        this.body = text.getBytes(contentType.getCharset());
        this.binary = false;
        this.contentType = contentType;
        return this;
    }

    /**
     * Attaches a binary body with specified content type.
     *
     * @param bytes       raw byte data
     * @param contentType MIME type (e.g. "application/octet-stream")
     * @return this {@code RequestBuilder}
     */
    public RequestBuilder binaryBody(byte[] bytes, ContentType contentType) {
        this.body = bytes;
        this.binary = true;
        this.contentType = contentType;
        return this;
    }

    /**
     * Builds the {@link Request} for execution, combining method, URL, headers, and body.
     *
     * @param baseUrl configured base URL prefix
     * @return a configured Fluent {@code Request}
     * @throws IllegalStateException if method or path is missing, or contentType missing for body
     */
    protected Request build(String baseUrl) {
        if (method == null || path == null) throw new IllegalStateException("HTTP method and path must be set");
        String uri = StringUtils.isNotBlank(baseUrl) ? baseUrl + path : path;
        if (!params.isEmpty()) uri += "?" + params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
        Request request = switch (method) {
            case HttpMethod.GET -> Request.get(uri);
            case HttpMethod.POST -> attachBody(Request.post(uri));
            case HttpMethod.PUT -> attachBody(Request.put(uri));
            case HttpMethod.PATCH -> attachBody(Request.patch(uri));
            case HttpMethod.DELETE -> Request.delete(uri);
            case HttpMethod.HEAD -> Request.head(uri);
            case HttpMethod.OPTIONS -> Request.options(uri);
            case HttpMethod.TRACE -> Request.trace(uri);
        };
        headers.forEach(request::addHeader);
        return request;
    }

    private Request attachBody(Request req) {
        if (binary) req.bodyByteArray(body, contentType);
        else req.bodyString(new String(body, contentType.getCharset()), contentType);
        return req;
    }
}
