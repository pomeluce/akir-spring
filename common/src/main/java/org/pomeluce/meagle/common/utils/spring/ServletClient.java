package org.pomeluce.meagle.common.utils.spring;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.pomeluce.meagle.common.core.domain.HttpEntity;
import org.pomeluce.meagle.common.text.TextConvert;
import org.pomeluce.meagle.common.utils.JacksonUtils;
import org.pomeluce.meagle.common.utils.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/28下午9:51
 * @className : ServletClient
 * @description : 客户端工具类
 */
public class ServletClient {

    static final TextConvert convert = TextConvert.instance();

    /**
     * 根据 name 获取 request 参数
     *
     * @param name 参数名称
     * @return 返回一个 String 类型的参数值
     */
    public static String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 根据 name 获取 request 参数
     *
     * @param name         参数名称
     * @param defaultValue 默认值
     * @return 返回一个 String 类型的参数值
     */
    public static String getParameter(String name, String defaultValue) {
        return convert.toString(getParameter(name), defaultValue);
    }

    /**
     * 根据 name 获取 request 参数
     *
     * @param name 参数名称
     * @return 返回一个 {@link Integer} 类型的参数值
     */
    public static Integer getIntegerParameter(String name) {
        return convert.toInt(getParameter(name));
    }

    /**
     * 根据 name 获取 request 参数
     *
     * @param name         参数名称
     * @param defaultValue 默认值
     * @return 返回一个 {@link Integer} 类型的参数值
     */
    public static Integer getIntegerParameter(String name, Integer defaultValue) {
        return convert.toInt(getParameter(name), defaultValue);
    }

    /**
     * 获取 ServletRequestAttributes 对象
     *
     * @return 返回一个 ServletRequestAttributes 对象
     */
    private static ServletRequestAttributes getRequestAttributes() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    }

    /**
     * 获取 request 对象
     *
     * @return 返回一个 HttpServletRequest 对象
     */
    public static HttpServletRequest getRequest() {
        return getRequestAttributes().getRequest();
    }

    /**
     * 获取 response 对象
     *
     * @return 返回一个 HttpServletResponse 对象
     */
    public static HttpServletResponse getResponse() {
        return getRequestAttributes().getResponse();
    }

    /**
     * 获取 session 对象
     *
     * @return 返回一个 HttpSession 对象
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取请求参数
     *
     * @param request 请求对象 {@link HttpServletRequest}
     * @return 返回一个 Map<String, String[]> 类型的请求参数集合
     */
    public static Map<String, String[]> getUnmodifiableParamMap(HttpServletRequest request) {
        return Collections.unmodifiableMap(request.getParameterMap());
    }

    /**
     * 获取请求参数
     *
     * @param request http 请求对象 {@link HttpServletRequest}
     * @return 返回一个 Map<String, String> 类型的请求参数集合
     */
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        return getUnmodifiableParamMap(request).entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> StringUtils.join(e.getValue(), ",")));
    }

    /**
     * 响应数据到客户端
     *
     * @param response   HTTP 响应对象 {@link HttpServletResponse}
     * @param httpEntity HTTP 响应实体 {@link HttpEntity}
     * @param <K>        HTTP 响应实体的 key 类型
     * @param <V>        HTTP 响应实体的 value 类型
     * @throws IOException IO 异常
     */
    public static <K, V> void responseBody(HttpServletResponse response, HttpEntity<K, V> httpEntity) throws IOException {
        response.setStatus(httpEntity.getCode());
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(JacksonUtils.toJsonString(httpEntity));
        writer.flush();
        writer.close();
    }

    /**
     * 设置请求 URI
     *
     * @param uri 请求 URI {@link String}
     */
    public static void setRequestURI(String uri) {
        getRequest().setAttribute("requestURI", uri);
    }

    /**
     * 获取请求 URI
     *
     * @return 返回一个 String 类型的 requestURI
     */
    public static String getRequestURI() {
        return (String) getRequest().getAttribute("requestURI");
    }
}
