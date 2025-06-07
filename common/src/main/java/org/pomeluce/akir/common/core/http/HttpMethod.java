package org.pomeluce.akir.common.core.http;


import java.io.Serializable;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2025-06-07 21:54
 * @className : HttpMethod
 * @description : HTTP 请求类型对象
 */
public enum HttpMethod implements Comparable<HttpMethod>, Serializable {
    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    public boolean matches(String method) {
        return this.name().equals(method);
    }
}
