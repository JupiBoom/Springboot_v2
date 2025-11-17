package com.fc.v2.common.monitor;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 消息队列监控类
 * 用于统计消息处理延迟和其他消息相关指标
 * @ClassName: MessageQueueMonitor
 * @author fuce
 * @date 2025-11-17 14:16
 */
@Component
public class MessageQueueMonitor {
    
    // 消息总处理次数
    private AtomicLong totalMessageCount = new AtomicLong(0);
    
    // 消息处理成功次数
    private AtomicLong successMessageCount = new AtomicLong(0);
    
    // 消息处理失败次数
    private AtomicLong failedMessageCount = new AtomicLong(0);
    
    // 最大消息处理延迟
    private AtomicReference<Long> maxDelay = new AtomicReference<>(0L);
    
    // 最小消息处理延迟
    private AtomicReference<Long> minDelay = new AtomicReference<>(Long.MAX_VALUE);
    
    // 总消息处理延迟
    private AtomicLong totalDelay = new AtomicLong(0);
    
    /**
     * 记录消息处理
     * 
     * @param delay 处理延迟（毫秒）
     * @param success 是否成功
     */
    public void recordMessageProcessing(long delay, boolean success) {
        totalMessageCount.incrementAndGet();
        
        if (success) {
            successMessageCount.incrementAndGet();
        } else {
            failedMessageCount.incrementAndGet();
        }
        
        // 更新延迟统计
        totalDelay.addAndGet(delay);
        
        maxDelay.updateAndGet(currentMax -> Math.max(currentMax, delay));
        minDelay.updateAndGet(currentMin -> Math.min(currentMin, delay));
    }
    
    /**
     * 获取平均消息处理延迟
     * 
     * @return 平均延迟（毫秒）
     */
    public double getAverageDelay() {
        long total = totalMessageCount.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) totalDelay.get() / total;
    }
    
    /**
     * 获取最大消息处理延迟
     * 
     * @return 最大延迟（毫秒）
     */
    public long getMaxDelay() {
        return maxDelay.get();
    }
    
    /**
     * 获取最小消息处理延迟
     * 
     * @return 最小延迟（毫秒）
     */
    public long getMinDelay() {
        return minDelay.get() == Long.MAX_VALUE ? 0 : minDelay.get();
    }
    
    /**
     * 获取消息处理成功率
     * 
     * @return 成功率 (0-100)
     */
    public double getSuccessRate() {
        long total = totalMessageCount.get();
        if (total == 0) {
            return 0.0;
        }
        return (double) successMessageCount.get() / total * 100;
    }
    
    /**
     * 获取消息总处理次数
     * 
     * @return 总处理次数
     */
    public long getTotalMessageCount() {
        return totalMessageCount.get();
    }
    
    /**
     * 获取消息处理成功次数
     * 
     * @return 成功次数
     */
    public long getSuccessMessageCount() {
        return successMessageCount.get();
    }
    
    /**
     * 获取消息处理失败次数
     * 
     * @return 失败次数
     */
    public long getFailedMessageCount() {
        return failedMessageCount.get();
    }
    
    /**
     * 重置统计指标
     */
    public void reset() {
        totalMessageCount.set(0);
        successMessageCount.set(0);
        failedMessageCount.set(0);
        maxDelay.set(0L);
        minDelay.set(Long.MAX_VALUE);
        totalDelay.set(0);
    }
}
