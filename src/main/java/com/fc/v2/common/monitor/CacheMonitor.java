package com.fc.v2.common.monitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 缓存监控类
 * 用于统计缓存命中率和其他缓存相关指标
 * @ClassName: CacheMonitor
 * @author fuce
 * @date 2025-11-17 14:15
 */
@Component
public class CacheMonitor {
    
    @Autowired
    private RedisTemplate redisTemplate;
    
    // 缓存命中次数
    private AtomicLong hitCount = new AtomicLong(0);
    
    // 缓存总请求次数
    private AtomicLong totalCount = new AtomicLong(0);
    
    /**
     * 记录缓存命中
     */
    public void recordHit() {
        hitCount.incrementAndGet();
        totalCount.incrementAndGet();
    }
    
    /**
     * 记录缓存未命中
     */
    public void recordMiss() {
        totalCount.incrementAndGet();
    }
    
    /**
     * 获取缓存命中率
     * 
     * @return 命中率 (0-100)
     */
    public double getHitRate() {
        long total = totalCount.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) hitCount.get() / total * 100;
    }
    
    /**
     * 重置统计指标
     */
    public void reset() {
        hitCount.set(0);
        totalCount.set(0);
    }
    
    /**
     * 获取缓存命中次数
     * 
     * @return 命中次数
     */
    public long getHitCount() {
        return hitCount.get();
    }
    
    /**
     * 获取缓存总请求次数
     * 
     * @return 总请求次数
     */
    public long getTotalCount() {
        return totalCount.get();
    }
}
