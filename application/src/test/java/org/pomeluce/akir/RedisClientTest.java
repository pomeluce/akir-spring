package org.pomeluce.akir;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.akir.common.constants.RedisKeyConstants;
import org.akir.common.core.redis.RedisClient;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2024/8/10 16:58
 * @className : RedisClientTest
 * @description : RedisClient 测试类
 */
@SpringBootTest
public class RedisClientTest {
    private @Resource RedisClient redisClient;

    public @Test void hdel() {
        redisClient.delete(RedisKeyConstants.PASSWORD_RETRIES_KEY);
    }
}
