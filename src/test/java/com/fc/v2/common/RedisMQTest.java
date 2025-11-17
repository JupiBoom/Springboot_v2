package com.fc.v2.common;

import com.fc.v2.common.conf.redis.RedisService;
import com.fc.v2.common.mq.rocketmq.RocketMQProducerService;
import com.fc.v2.common.monitor.CacheMonitor;
import com.fc.v2.common.monitor.MessageQueueMonitor;
import com.fc.v2.common.monitor.FaultToleranceHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisMQTest {
    
    @Autowired
    private RedisService redisService;
    
    @Autowired
    private RocketMQProducerService rocketMQProducerService;
    
    @Autowired
    private CacheMonitor cacheMonitor;
    
    @Autowired
    private MessageQueueMonitor messageQueueMonitor;
    
    @Autowired
    private FaultToleranceHandler faultToleranceHandler;
    
    @Test
    public void testRedisCache() {
        // 测试缓存设置和获取
        String key = "test_key";
        String value = "test_value";
        
        redisService.setCacheObject(key, value, 10L, TimeUnit.MINUTES);
        String cachedValue = redisService.getCacheObject(key);
        
        Assertions.assertEquals(value, cachedValue);
        
        // 测试缓存命中率
        double hitRate = cacheMonitor.getHitRate();
        System.out.println("缓存命中率: " + hitRate);
    }
    
    @Test
    public void testRocketMQ() {
        // 测试消息发送
        String topic = "test_topic";
        String message = "test_message";
        
        rocketMQProducerService.sendSyncMessage(topic, message);
        
        // 测试消息处理延迟
        double averageDelay = messageQueueMonitor.getAverageDelay();
        System.out.println("消息平均处理延迟: " + averageDelay + " ms");
    }
    
    @Test
    public void testFaultTolerance() {
        // 测试缓存降级
        String key = "non_existent_key";
        String fallbackData = "fallback_value";
        
        String result = faultToleranceHandler.cacheFallback(key, fallbackData);
        Assertions.assertEquals(fallbackData, result);
        
        // 测试数据一致性检查
        String cacheKey = "test_consistency_key";
        String dbData = "db_value";
        
        redisService.setCacheObject(cacheKey, dbData, 10L, TimeUnit.MINUTES);
        boolean consistent = faultToleranceHandler.checkDataConsistency(cacheKey, dbData);
        
        Assertions.assertTrue(consistent);
        
        // 测试数据一致性修复
        String newDbData = "new_db_value";
        faultToleranceHandler.fixDataConsistency(cacheKey, newDbData, 10L, TimeUnit.MINUTES);
        
        String updatedCacheData = redisService.getCacheObject(cacheKey);
        assert newDbData.equals(updatedCacheData);
    }
}
