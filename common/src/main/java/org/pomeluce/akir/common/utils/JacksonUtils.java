package org.pomeluce.akir.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.pomeluce.akir.common.exception.AkirCommonUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/9/29下午3:25
 * @className : JacksonUtils
 * @description : jackson 工具类
 */
public class JacksonUtils {

    private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);

    /**
     * 获取 ObjectMapper 对象
     *
     * @return 返回一个 ObjectMapper 对象
     */
    public static ObjectMapper instance() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        mapper.registerModule(timeModule);
        return mapper;
    }

    /**
     * 将 object 对象转换为 json 字符串
     *
     * @param obj 待转换的 object 对象 {@link Object}
     * @return 返回一个 {@link String} 类型的 json 字符串
     */
    public static String toJsonString(Object obj) {
        try {
            return instance().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("JacksonUtil Serialization failed: {}", e.getMessage());
            throw new AkirCommonUtilException("object convert to json failed", e);
        }
    }

    /**
     * 将 json 字符串转换为 object
     *
     * @param json      待转换的 json 字符串 {@link String}
     * @param valueType 待转换的类型 {@link Class<T>}
     * @param <T>       泛型参数
     * @return 返回一个 {@link T} 类型的 object 对象
     */
    public static <T> T parseValue(String json, Class<T> valueType) {
        try {
            return instance().readValue(json, valueType);
        } catch (JsonProcessingException e) {
            log.error("JacksonUtil Deserialization failed: {}", e.getMessage());
            throw new AkirCommonUtilException("json convert to object failed", e);
        }
    }
}
