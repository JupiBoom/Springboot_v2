package com.fc.v2.common.mq.rocketmq;

import com.fc.v2.common.monitor.MessageQueueMonitor;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * RocketMQ 生产者服务
 * @ClassName: RocketMQProducerService
 * @author fuce
 * @date 2025-11-17 14:13
 */
@Service
public class RocketMQProducerService {
    
    @Autowired(required = false)
    private RocketMQTemplate rocketMQTemplate;
    
    @Autowired
    private MessageQueueMonitor messageQueueMonitor;
    
    /**
     * 发送同步消息
     * 
     * @param topic 主题
     * @param message 消息内容
     * @return 发送结果
     */
    public SendResult sendSyncMessage(String topic, Object message) {
        long startTime = System.currentTimeMillis();
        try {
            if (rocketMQTemplate == null) {
                throw new IllegalStateException("RocketMQTemplate is not available");
            }
            SendResult result = rocketMQTemplate.syncSend(topic, message);
            long delay = System.currentTimeMillis() - startTime;
            messageQueueMonitor.recordMessageProcessing(delay, true);
            return result;
        } catch (Exception e) {
            long delay = System.currentTimeMillis() - startTime;
            messageQueueMonitor.recordMessageProcessing(delay, false);
            throw e;
        }
    }
    
    /**
     * 发送带标签的同步消息
     * 
     * @param topic 主题
     * @param tag 标签
     * @param message 消息内容
     * @return 发送结果
     */
    public SendResult sendSyncMessage(String topic, String tag, Object message) {
        String destination = topic + ":" + tag;
        long startTime = System.currentTimeMillis();
        try {
            if (rocketMQTemplate == null) {
                throw new IllegalStateException("RocketMQTemplate is not available");
            }
            SendResult result = rocketMQTemplate.syncSend(destination, message);
            long delay = System.currentTimeMillis() - startTime;
            messageQueueMonitor.recordMessageProcessing(delay, true);
            return result;
        } catch (Exception e) {
            long delay = System.currentTimeMillis() - startTime;
            messageQueueMonitor.recordMessageProcessing(delay, false);
            throw e;
        }
    }
    
    /**
     * 发送异步消息
     * 
     * @param topic 主题
     * @param message 消息内容
     * @param callback 回调函数
     */
    public void sendAsyncMessage(String topic, Object message, org.apache.rocketmq.client.producer.SendCallback callback) {
        if (rocketMQTemplate != null) {
            rocketMQTemplate.asyncSend(topic, message, callback);
        }
    }
    
    /**
     * 发送单向消息
     * 
     * @param topic 主题
     * @param message 消息内容
     */
    public void sendOneWayMessage(String topic, Object message) {
        if (rocketMQTemplate != null) {
            rocketMQTemplate.sendOneWay(topic, message);
        }
    }
    
    /**
     * 发送延迟消息
     * 
     * @param topic 主题
     * @param message 消息内容
     * @param delayLevel 延迟级别（1-18）
     * @return 发送结果
     */
    public SendResult sendDelayMessage(String topic, Object message, int delayLevel) {
        if (rocketMQTemplate == null) {
            throw new IllegalStateException("RocketMQTemplate is not available");
        }
        org.springframework.messaging.Message<Object> rocketMessage = MessageBuilder.withPayload(message)
                .build();
        return rocketMQTemplate.syncSend(topic, rocketMessage, 3000, delayLevel);
    }
    
    /**
     * 发送事务消息
     * 
     * @param txProducerGroup 事务生产者组
     * @param topic 主题
     * @param message 消息内容
     * @return 发送结果
     */
    public void sendTransactionMessage(String txProducerGroup, Object message) {
        if (rocketMQTemplate != null) {
            org.springframework.messaging.Message<Object> rocketMessage = MessageBuilder.withPayload(message)
                    .build();
            rocketMQTemplate.sendMessageInTransaction(txProducerGroup, rocketMessage, null);
        }
    }
}