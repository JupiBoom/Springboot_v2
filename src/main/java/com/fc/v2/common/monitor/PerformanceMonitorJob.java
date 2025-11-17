package com.fc.v2.common.monitor;

import com.fc.v2.common.quartz.AbstractQuartzJob;
import com.fc.v2.model.auto.SysQuartzJob;
import com.fc.v2.common.spring.SpringUtils;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 性能监控定时任务
 * 用于检查缓存命中率和消息处理延迟，并在超过阈值时发送告警
 * @ClassName: PerformanceMonitorJob
 * @author fuce
 * @date 2025-11-17 14:30
 */
public class PerformanceMonitorJob extends AbstractQuartzJob {
    
    private static final Logger log = LoggerFactory.getLogger(PerformanceMonitorJob.class);
    
    // 缓存命中率阈值 (低于此值触发告警)
    private static final double CACHE_HIT_RATE_THRESHOLD = 80.0;
    
    // 消息处理平均延迟阈值 (高于此值触发告警，单位：毫秒)
    private static final long MESSAGE_AVG_DELAY_THRESHOLD = 50L;
    
    @Override
    protected void doExecute(JobExecutionContext jobExecutionContext, SysQuartzJob sysJob) throws Exception {
        log.info("开始执行性能监控任务...");
        
        // 获取监控组件
        CacheMonitor cacheMonitor = SpringUtils.getBean(CacheMonitor.class);
        MessageQueueMonitor messageQueueMonitor = SpringUtils.getBean(MessageQueueMonitor.class);
        
        // 检查缓存命中率
        double cacheHitRate = cacheMonitor.getHitRate();
        log.info("当前缓存命中率: {}", cacheHitRate);
        
        if (cacheHitRate < CACHE_HIT_RATE_THRESHOLD) {
            // 发送缓存命中率告警
            sendAlert("缓存命中率告警", "当前缓存命中率为 " + cacheHitRate + "%，低于阈值 " + CACHE_HIT_RATE_THRESHOLD + "%");
        }
        
        // 检查消息处理延迟
        double messageAvgDelay = messageQueueMonitor.getAverageDelay();
        log.info("当前消息处理平均延迟: {} ms", messageAvgDelay);
        
        if (messageAvgDelay > MESSAGE_AVG_DELAY_THRESHOLD) {
            // 发送消息处理延迟告警
            sendAlert("消息处理延迟告警", "当前消息处理平均延迟为 " + messageAvgDelay + " ms，高于阈值 " + MESSAGE_AVG_DELAY_THRESHOLD + " ms");
        }
        
        log.info("性能监控任务执行完成...");
    }
    
    /**
     * 发送告警
     * 
     * @param title 告警标题
     * @param content 告警内容
     */
    private void sendAlert(String title, String content) {
        // 这里可以实现告警发送逻辑，比如邮件、短信、钉钉机器人等
        // 目前暂时只记录日志
        log.warn("[性能监控告警] {}: {}", title, content);
    }
}
