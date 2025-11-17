package com.fc.v2.common.mq.rocketmq;

import com.fc.v2.common.conf.redis.RedisService;
import com.fc.v2.common.monitor.MessageQueueMonitor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * RocketMQ 消费者服务
 * @ClassName: RocketMQConsumerService
 * @author fuce
 * @date 2025-11-17 14:14
 */
@Service
public abstract class RocketMQConsumerService<T> implements RocketMQListener<T> {
    
    protected static final Logger log = LoggerFactory.getLogger(RocketMQConsumerService.class);
    
    @Autowired
    protected RedisService redisService;
    
    @Autowired
    protected MessageQueueMonitor messageQueueMonitor;
    
    /**
     * 消息消费方法
     * 
     * @param message 消息内容
     */
    @Override
    public void onMessage(T message) {
        long startTime = System.currentTimeMillis();
        boolean success = false;
        
        try {
            // 生成消息唯一标识
            String messageId = generateMessageId(message);
            
            // 检查消息是否已被处理
            if (checkIdempotency(messageId)) {
                log.info("消息已处理，忽略重复消费: {}", messageId);
                success = true;
                return;
            }
            
            // 处理消息
            handleMessage(message);
            
            // 标记消息为已处理
            markIdempotent(messageId);
            
            success = true;
        } catch (Exception e) {
            log.error("消息处理失败: {}", e.getMessage());
            // 可以在这里实现消息重试机制
        } finally {
            long delay = System.currentTimeMillis() - startTime;
            messageQueueMonitor.recordMessageProcessing(delay, success);
        }
    }
    
    /**
     * 生成消息唯一标识
     * 
     * @param message 消息内容
     * @return 消息唯一标识
     */
    protected String generateMessageId(T message) {
        // 默认使用UUID生成唯一标识，可以被子类重写
        return UUID.randomUUID().toString();
    }
    
    /**
     * 处理消息
     * 
     * @param message 消息内容
     */
    protected abstract void handleMessage(T message) throws Exception;
    
    /**
     * 幂等性检查：检查消息是否已经被消费过
     * 
     * @param messageId 消息ID
     * @return 是否已消费
     */
    public boolean checkIdempotency(String messageId) {
        // 这里可以实现基于Redis的幂等性检查
        // 例如：将消息ID作为Key存储到Redis，如果存在则返回true，表示已经消费过
        return false;
    }
    
    /**
     * 幂等性标记：标记消息已经被消费过
     * 
     * @param messageId 消息ID
     */
    public void markIdempotent(String messageId) {
        // 这里可以实现基于Redis的幂等性标记
        // 例如：将消息ID作为Key存储到Redis，并设置过期时间
    }
}
