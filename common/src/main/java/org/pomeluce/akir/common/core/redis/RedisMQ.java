package org.pomeluce.akir.common.core.redis;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/6下午8:47
 * @className : RedisMQ
 * @description : Redis 消息队列实现
 */
@Configuration
public class RedisMQ {
    private @Resource RedisTemplate<String, String> redisTemplate;
}
