package org.akir.common.core.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/6下午8:47
 * @className : RedisMQ
 * @description : Redis 消息队列实现
 */
@RequiredArgsConstructor
@Configuration
public class RedisMQ {
    private final RedisTemplate<String, String> redisTemplate;
}
