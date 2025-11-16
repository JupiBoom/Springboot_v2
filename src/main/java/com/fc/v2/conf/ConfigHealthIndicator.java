package com.fc.v2.conf;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.cloud.config.server.environment.EnvironmentRepository;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ConfigHealthIndicator implements HealthIndicator {

    private final EnvironmentRepository environmentRepository;

    public ConfigHealthIndicator(EnvironmentRepository environmentRepository) {
        this.environmentRepository = environmentRepository;
    }

    @Override
    public Health health() {
        try {
            // 尝试获取配置以检查Git仓库连接
            environmentRepository.findOne("test", "dev", "master");
            Map<String, Object> details = new HashMap<>();
            details.put("status", "UP");
            details.put("message", "配置中心运行正常");
            return Health.up().withDetails(details).build();
        } catch (Exception e) {
            Map<String, Object> details = new HashMap<>();
            details.put("status", "DOWN");
            details.put("message", "配置中心连接失败: " + e.getMessage());
            return Health.down().withDetails(details).build();
        }
    }
}