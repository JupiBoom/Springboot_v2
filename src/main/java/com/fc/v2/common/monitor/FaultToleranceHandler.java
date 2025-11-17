package com.fc.v2.common.monitor;

import com.fc.v2.common.conf.redis.RedisService;
import com.fc.v2.common.mq.rocketmq.RocketMQProducerService;
import com.fc.v2.common.spring.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 异常处理与容错类
 * 用于处理缓存或消息队列故障时的降级策略
 * 以及数据不一致的自动检测与修复机制
 * @ClassName: FaultToleranceHandler
 * @author fuce
 * @date 2025-11-17 14:35
 */
@Component
public class FaultToleranceHandler {
    
    private static final Logger log = LoggerFactory.getLogger(FaultToleranceHandler.class);
    
    @Autowired
    private RocketMQProducerService rocketMQProducerService;
    
    /**
     * 缓存故障降级策略
     * 
     * @param key 缓存键
     * @param fallbackData 降级数据
     * @return 缓存数据或降级数据
     */
    public <T> T cacheFallback(String key, T fallbackData) {
        try {
            RedisService redisService = SpringUtils.getBean(RedisService.class);
            T data = redisService.getCacheObject(key);
            if (data != null) {
                return data;
            }
        } catch (Exception e) {
            log.error("缓存获取失败，执行降级策略: {}", e.getMessage());
        }
        
        // 缓存故障时返回降级数据
        return fallbackData;
    }
    
    /**
     * 消息队列故障降级策略
     * 
     * @param topic 主题
     * @param message 消息内容
     * @return 是否成功
     */
    public boolean messageQueueFallback(String topic, Object message) {
        try {
            // 尝试发送消息
            rocketMQProducerService.sendSyncMessage(topic, message);
            return true;
        } catch (Exception e) {
            log.error("消息发送失败，执行降级策略: {}", e.getMessage());
            
            // 可以在这里实现消息持久化到数据库等降级策略
            // 目前暂时只记录日志
            return false;
        }
    }
    
    /**
     * 检测缓存与数据库数据一致性
     * 
     * @param cacheKey 缓存键
     * @param dbData 数据库数据
     * @return 是否一致
     */
    public <T> boolean checkDataConsistency(String cacheKey, T dbData) {
        try {
            RedisService redisService = SpringUtils.getBean(RedisService.class);
            T cacheData = redisService.getCacheObject(cacheKey);
            
            // 如果缓存中没有数据，认为一致（需要重新加载）
            if (cacheData == null) {
                return true;
            }
            
            // 比较缓存数据与数据库数据
            return cacheData.equals(dbData);
        } catch (Exception e) {
            log.error("数据一致性检查失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 修复缓存与数据库数据一致性
     * 
     * @param cacheKey 缓存键
     * @param dbData 数据库数据
     * @param expireTime 过期时间
     * @param timeUnit 时间单位
     */
    public <T> void fixDataConsistency(String cacheKey, T dbData, long expireTime, TimeUnit timeUnit) {
        try {
            RedisService redisService = SpringUtils.getBean(RedisService.class);
            // 更新缓存数据
            redisService.setCacheObject(cacheKey, dbData, expireTime, timeUnit);
            log.info("数据一致性修复成功，缓存键: {}", cacheKey);
        } catch (Exception e) {
            log.error("数据一致性修复失败: {}", e.getMessage());
        }
    }
}
