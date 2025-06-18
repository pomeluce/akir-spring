package org.akir.common.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.akir.common.exception.AkirCommonUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023-09-29 15:25
 * @className : JacksonUtils
 * @description : jackson 工具类
 */
public final class JacksonUtils {
    private static final Logger log = LoggerFactory.getLogger(JacksonUtils.class);

    // Default singleton ObjectMapper with common configuration
    private static final ObjectMapper DEFAULT_MAPPER = createDefaultMapper();

    /**
     * Build and configure a default ObjectMapper instance.
     *
     * @return configured ObjectMapper
     */
    private static ObjectMapper createDefaultMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Visibility
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // Date formatting
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        // Java 8 date/time module
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        timeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        timeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        mapper.registerModule(timeModule);

        // Configure features
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        mapper.configure(SerializationFeature.INDENT_OUTPUT, false);
        return mapper;
    }

    /**
     * Get the default ObjectMapper.
     *
     * @return singleton ObjectMapper
     */
    public static ObjectMapper getDefaultMapper() {
        return DEFAULT_MAPPER;
    }

    /**
     * Create a customized ObjectMapper by further configuring the default one.
     *
     * @param customizer consumer to apply customization to a new mapper copy
     * @return customized ObjectMapper
     */
    public static ObjectMapper getMapper(Consumer<ObjectMapper> customizer) {
        ObjectMapper mapper = DEFAULT_MAPPER.copy();
        customizer.accept(mapper);
        return mapper;
    }

    /**
     * Serialize an object to JSON string using default mapper.
     *
     * @param obj object to serialize
     * @return JSON string
     */
    public static String toJson(Object obj) {
        try {
            return DEFAULT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON", e);
            throw new RuntimeException("Serialization failed", e);
        }
    }

    /**
     * Serialize an object to pretty-printed JSON string.
     *
     * @param obj object to serialize
     * @return pretty JSON string
     */
    public static String toPrettyJson(Object obj) {
        try {
            return DEFAULT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to pretty JSON", e);
            throw new RuntimeException("Pretty serialization failed", e);
        }
    }

    /**
     * Serialize an object to JSON bytes.
     *
     * @param obj object to serialize
     * @return JSON byte array
     */
    public static byte[] toJsonBytes(Object obj) {
        try {
            return DEFAULT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize object to JSON bytes", e);
            throw new RuntimeException("Serialization to bytes failed", e);
        }
    }

    /**
     * Deserialize JSON string to an object of given class.
     *
     * @param json      JSON string
     * @param valueType target class
     * @param <T>       type
     * @return deserialized object
     */
    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return DEFAULT_MAPPER.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON to {}", valueType, e);
            throw new AkirCommonUtilException("Deserialization failed", e);
        }
    }

    /**
     * Deserialize JSON string to an object using TypeReference (for generics).
     *
     * @param json    JSON string
     * @param typeRef type reference
     * @param <T>     type
     * @return deserialized object
     */
    public static <T> T fromJson(String json, TypeReference<T> typeRef) {
        try {
            return DEFAULT_MAPPER.readValue(json, typeRef);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize JSON to TypeReference {}", typeRef, e);
            throw new AkirCommonUtilException("Deserialization to TypeReference failed", e);
        }
    }

    /**
     * Deserialize from InputStream to an object of given class.
     *
     * @param in        input stream
     * @param valueType target class
     * @param <T>       type
     * @return deserialized object
     */
    public static <T> T fromJson(InputStream in, Class<T> valueType) {
        try {
            return DEFAULT_MAPPER.readValue(in, valueType);
        } catch (IOException e) {
            log.error("Failed to deserialize JSON from InputStream to {}", valueType, e);
            throw new AkirCommonUtilException("Stream deserialization failed", e);
        }
    }

    /**
     * Convert an object to another type using Jackson's convertValue.
     *
     * @param fromValue   source object
     * @param toValueType target class
     * @param <T>         type
     * @return converted object
     */
    public static <T> T convert(Object fromValue, Class<T> toValueType) {
        return DEFAULT_MAPPER.convertValue(fromValue, toValueType);
    }

    /**
     * Convert an object to another type using TypeReference (for generics).
     *
     * @param fromValue source object
     * @param typeRef   type reference
     * @param <T>       type
     * @return converted object
     */
    public static <T> T convert(Object fromValue, TypeReference<T> typeRef) {
        return DEFAULT_MAPPER.convertValue(fromValue, typeRef);
    }

    /**
     * Parse JSON into a JsonNode tree.
     *
     * @param json JSON string
     * @return root JsonNode
     */
    public static JsonNode parseTree(String json) {
        try {
            return DEFAULT_MAPPER.readTree(json);
        } catch (JsonProcessingException e) {
            log.error("Failed to parse JSON to JsonNode", e);
            throw new RuntimeException("JSON parsing failed", e);
        }
    }

    /**
     * Update an existing object with values from JSON.
     * Non-null properties in JSON will overwrite those in the object.
     *
     * @param json           JSON string with updates
     * @param objectToUpdate target object
     * @param <T>            type
     * @return updated object
     */
    public static <T> T update(String json, T objectToUpdate) {
        try {
            return DEFAULT_MAPPER.readerForUpdating(objectToUpdate)
                    .readValue(json);
        } catch (JsonProcessingException e) {
            log.error("Failed to update object from JSON", e);
            throw new RuntimeException("JSON update failed", e);
        }
    }

    /**
     * Read JSON file into an object.
     *
     * @param file      JSON file
     * @param valueType target class
     * @param <T>       type
     * @return deserialized object
     */
    public static <T> T fromFile(File file, Class<T> valueType) {
        try {
            return DEFAULT_MAPPER.readValue(file, valueType);
        } catch (IOException e) {
            log.error("Failed to read JSON from file {}", file, e);
            throw new RuntimeException("File deserialization failed", e);
        }
    }

    /**
     * Convert a Map into a JSON ObjectNode.
     *
     * @param map map to convert
     * @return ObjectNode
     */
    public static ObjectNode toObjectNode(Map<String, Object> map) {
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        map.forEach(node::putPOJO);
        return node;
    }

    /**
     * Register a custom module to the default mapper.
     * Note: this alters the DEFAULT_MAPPER.
     *
     * @param module Jackson Module to register
     */
    public static void registerModule(Module module) {
        DEFAULT_MAPPER.registerModule(module);
    }
}
