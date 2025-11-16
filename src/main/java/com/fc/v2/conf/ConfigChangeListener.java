package com.fc.v2.conf;

import org.springframework.cloud.bus.event.RefreshRemoteApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ConfigChangeListener {

    private static final Logger logger = LoggerFactory.getLogger(ConfigChangeListener.class);

    @EventListener
    public void handleRefreshEvent(RefreshRemoteApplicationEvent event) {
        logger.info("Received config refresh event from origin: {}", event.getOriginService());
        logger.info("Config event id: {}", event.getId());
        // 可以在这里添加自定义的配置刷新逻辑
    }
}
