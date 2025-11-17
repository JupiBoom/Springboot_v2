package com.fc.v2.common.mq.rocketmq;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ 配置类
 * @ClassName: RocketMQConfig
 * @author fuce
 * @date 2025-11-17 14:12
 */
@Configuration
public class RocketMQConfig {
    
    @Autowired(required = false)
    private RocketMQTemplate rocketMQTemplate;
    
    // 可以在这里配置全局的RocketMQ监听者
    // 或者在具体的业务类中使用@RocketMQMessageListener注解
}
