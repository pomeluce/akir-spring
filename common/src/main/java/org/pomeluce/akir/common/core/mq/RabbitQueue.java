package org.pomeluce.akir.common.core.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : marcus
 * @version : 1.0
 * @date : 2023/10/11下午9:10
 * @className : RabbitQueue
 * @description : RabbitMQ 消息队列
 */
@Configuration
public class RabbitQueue {

    /**
     * 注入 RabbitMQ 消息队列 bean
     *
     * @return 返回一个 Queue 类型的消息队列
     */
    public @Bean Queue SimpleQueue() {
        return new Queue("");
    }
}
