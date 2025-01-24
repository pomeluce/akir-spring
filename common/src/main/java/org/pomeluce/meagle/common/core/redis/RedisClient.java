package org.pomeluce.meagle.common.core.redis;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.pomeluce.meagle.common.utils.JacksonUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2023/9/29 20:17
 * @className : RedisClient
 * @description : redis 操作客户端
 */
@Configuration
public class RedisClient {
    private @Resource RedisTemplate<String, Object> redisTemplate;

    /**
     * 初始化 redis 配置
     */
    private @PostConstruct void init() {
        ObjectMapper mapper = JacksonUtils.instance();
        mapper.activateDefaultTyping(mapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(mapper, Object.class);
        redisTemplate.setValueSerializer(serializer);
        redisTemplate.setHashValueSerializer(serializer);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setStringSerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
    }

    /**
     * 查询 key 值, 支持模糊搜索
     *
     * @param key 待查询的 key {@link String}
     * @return 返回一个 {@link Set<String>} 类型的 key 集合
     */
    public Set<String> keys(String key) {
        return redisTemplate.keys(key);
    }

    /**
     * 根据 key 查询 value
     *
     * @param key 要获取的 value 对应的 key 值 {@link String}
     * @return 返回一个 {@link Object} 类型的 value
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置值
     *
     * @param key   key 值 {@link String}
     * @param value value 值 {@link Object}
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 设置值
     *
     * @param key    key 值 {@link String}
     * @param value  value 值 {@link Object}
     * @param expire 过期时间 {@link Integer}
     * @param unit   时间单位 {@link TimeUnit}
     */
    public void set(String key, Object value, Integer expire, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, expire, unit);
    }

    /**
     * 保存 hash 值
     *
     * @param key   key {@link String}
     * @param field 属性 {@link String}
     * @param value 值 {@link Object}
     */
    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 保存 hash 值
     *
     * @param key    key {@link String}
     * @param field  属性 {@link String}
     * @param value  值 {@link Object}
     * @param expire 过期时间 {@link Integer}
     * @param unit   时间单位 {@link TimeUnit}
     */
    public void hset(String key, String field, Object value, Integer expire, TimeUnit unit) {
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, expire, unit);
    }

    /**
     * 保存 hash 值
     *
     * @param key key {@link String}
     * @param map map {@link HashMap}
     */
    public void hset(String key, HashMap<String, ?> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * 获取 hash 值
     *
     * @param key   key {@link String}
     * @param field 属性 {@link String}
     * @return 返回一个为 {@link Object} 类型的 value
     */
    public Object hget(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 获取 hash 值
     *
     * @param key key {@link String}
     * @return 返回一个泛型为 {@link String}, {@link Object} 类型的 hash 值集合
     */
    public Map<String, Object> hget(String key) {
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }

    /**
     * 获取 hash 的所有 key
     *
     * @param key key {@link String}
     * @return 返回一个 {@link Set<String>} 类型的 field 集合
     */
    public Set<String> hkeys(String key) {
        HashOperations<String, String, Object> hash = redisTemplate.opsForHash();
        return hash.keys(key);
    }

    /**
     * 删除 hash 值
     *
     * @param key key 值 {@link String}
     * @return 返回一个 boolean 类型的删除结果
     */
    public boolean hdel(String key) {
        return Boolean.TRUE.equals(redisTemplate.opsForHash().getOperations().delete(key));
    }

    /**
     * 删除 hash 值中指定的属性
     *
     * @param key    key 值 {@link String}
     * @param fields 属性 {@link Object}...
     * @return 返回一个 {@link Boolean} 类型的删除结果
     */
    public boolean hdel(String key, Object... fields) {
        return Boolean.TRUE.equals(redisTemplate.opsForHash().delete(key, fields) > 0);
    }

    /**
     * 删除 hash 值
     *
     * @param collection 要删除的 key 集合 {@link Collection<String>}
     * @return 返回一个 {@link Boolean} 类型的删除结果
     */
    public boolean hdel(Collection<String> collection) {
        return Objects.requireNonNull(redisTemplate.opsForHash().getOperations().delete(collection)) > 0;
    }

    /**
     * 判断 key 是否存在 field 属性
     *
     * @param key   key 值 {@link String}
     * @param field 属性 {@link String}
     * @return 返回一个 {@link Boolean} 类型的检查结果
     */
    public boolean hexists(String key, String field) {
        return redisTemplate.opsForHash().hasKey(key, field);
    }

    /**
     * 删除 key 对应的值
     *
     * @param key key 值 {@link String}
     * @return 返回一个 {@link Boolean} 类型的删除结果
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 根据 key 集合删除 key 对应的值
     *
     * @param collect 要删除的的 key 集合 {@link Collection<String>}
     * @return 返回一个 {@link Boolean} 类型的删除结果
     */
    public boolean delete(Collection<String> collect) {
        return Objects.requireNonNull(redisTemplate.delete(collect)) > 0;
    }
}
