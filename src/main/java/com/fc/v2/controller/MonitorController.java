package com.fc.v2.controller;

import com.fc.v2.common.domain.AjaxResult;
import com.fc.v2.common.monitor.CacheMonitor;
import com.fc.v2.common.monitor.MessageQueueMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统监控控制器
 * 用于暴露系统性能监控指标
 * @ClassName: MonitorController
 * @author fuce
 * @date 2025-11-17 14:20
 */
@RestController
@RequestMapping("/monitor")
public class MonitorController {
    
    @Autowired
    private CacheMonitor cacheMonitor;
    
    @Autowired
    private MessageQueueMonitor messageQueueMonitor;
    
    /**
     * 获取缓存监控信息
     * 
     * @return AjaxResult
     */
    @GetMapping("/cache")
    public AjaxResult getCacheMonitorInfo() {
        Map<String, Object> cacheInfo = new HashMap<>();
        cacheInfo.put("hitRate", cacheMonitor.getHitRate());
        cacheInfo.put("hitCount", cacheMonitor.getHitCount());
        cacheInfo.put("totalCount", cacheMonitor.getTotalCount());
        return AjaxResult.successData(200, cacheInfo).put("msg", "获取缓存监控信息成功");
    }
    
    /**
     * 获取消息队列监控信息
     * 
     * @return AjaxResult
     */
    @GetMapping("/messageQueue")
    public AjaxResult getMessageQueueMonitorInfo() {
        Map<String, Object> messageInfo = new HashMap<>();
        messageInfo.put("totalMessageCount", messageQueueMonitor.getTotalMessageCount());
        messageInfo.put("successMessageCount", messageQueueMonitor.getSuccessMessageCount());
        messageInfo.put("failedMessageCount", messageQueueMonitor.getFailedMessageCount());
        messageInfo.put("averageDelay", messageQueueMonitor.getAverageDelay());
        messageInfo.put("maxDelay", messageQueueMonitor.getMaxDelay());
        messageInfo.put("minDelay", messageQueueMonitor.getMinDelay());
        messageInfo.put("successRate", messageQueueMonitor.getSuccessRate());
        return AjaxResult.successData(200, messageInfo).put("msg", "获取消息队列监控信息成功");
    }
    
    /**
     * 获取所有监控信息
     * 
     * @return AjaxResult
     */
    @GetMapping("/all")
    public AjaxResult getAllMonitorInfo() {
        Map<String, Object> allInfo = new HashMap<>();
        
        // 缓存监控信息
        Map<String, Object> cacheInfo = new HashMap<>();
        cacheInfo.put("hitRate", cacheMonitor.getHitRate());
        cacheInfo.put("hitCount", cacheMonitor.getHitCount());
        cacheInfo.put("totalCount", cacheMonitor.getTotalCount());
        
        // 消息队列监控信息
        Map<String, Object> messageInfo = new HashMap<>();
        messageInfo.put("totalMessageCount", messageQueueMonitor.getTotalMessageCount());
        messageInfo.put("successMessageCount", messageQueueMonitor.getSuccessMessageCount());
        messageInfo.put("failedMessageCount", messageQueueMonitor.getFailedMessageCount());
        messageInfo.put("averageDelay", messageQueueMonitor.getAverageDelay());
        messageInfo.put("maxDelay", messageQueueMonitor.getMaxDelay());
        messageInfo.put("minDelay", messageQueueMonitor.getMinDelay());
        messageInfo.put("successRate", messageQueueMonitor.getSuccessRate());
        
        allInfo.put("cache", cacheInfo);
        allInfo.put("messageQueue", messageInfo);
        
        return AjaxResult.successData(200, allInfo).put("msg", "获取所有监控信息成功");
    }
}
