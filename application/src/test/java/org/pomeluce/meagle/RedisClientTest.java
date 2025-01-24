package org.pomeluce.meagle;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.pomeluce.meagle.common.core.redis.RedisClient;
import org.pomeluce.meagle.common.enums.CacheKey;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : lucas
 * @version 1.0
 * @date : 2024/8/10 16:58
 * @className : RedisClientTest
 * @description : RedisClient 测试类
 */
@SpringBootTest
public class RedisClientTest {
    private @Resource RedisClient redisClient;

    public @Test void hdel() {
        redisClient.delete(CacheKey.PASSWORD_RETRIES_KEY.value());
    }
}
