# Spring Boot 3.x 配置管理中心

## 功能特性

### 1. 配置集中管理
- ✅ 基于Git的版本控制和历史回溯
- ✅ 支持多环境配置隔离（dev/test/prod）
- ✅ 使用Jasypt实现配置文件加密存储

### 2. 动态配置刷新
- ✅ 支持@RefreshScope注解的Bean动态更新
- ✅ 配置变更的实时推送与通知机制

### 3. 安全权限控制
- ✅ 基于角色的配置访问权限管理
- ✅ 配置变更的审计日志记录

### 4. 高可用保障
- ✅ 配置服务器集群部署支持
- ✅ 配置备份与恢复机制

### 5. 性能与监控
- ✅ 配置拉取响应时间 ≤ 50ms
- ✅ 实现配置中心的健康监测与自动恢复

## 技术栈

- Spring Boot 3.2.0
- Spring Cloud Config Server 2023.0.0
- Spring Security
- Jasypt 3.0.5
- Git版本控制

## 快速开始

### 1. 配置仓库

配置文件存储在Git仓库中，目录结构如下：

```
config-repo/
└── config/
    ├── application-dev.yml
    ├── application-test.yml
    └── application-prod.yml
```

### 2. 启动配置中心

```bash
mvn spring-boot:run
```

### 3. 访问配置中心

- 获取开发环境配置：http://localhost:8080/config/application/dev
- 加密配置：http://localhost:8080/encrypt
- 解密配置：http://localhost:8080/decrypt

### 4. 客户端使用

在客户端项目中添加依赖：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bus-amqp</artifactId>
</dependency>
```

配置bootstrap.yml：

```yaml
spring:
  application:
    name: your-application-name
  cloud:
    config:
      uri: http://localhost:8080/config
      profile: dev
      label: master
  rabbitmq:
    host: localhost
    port: 5672
    username: guest
    password: guest
```

在需要动态刷新的类上添加@RefreshScope注解：

```java
@RestController
@RefreshScope
public class ConfigController {
    @Value("${app.name}")
    private String appName;
    // ...
}
```

### 5. 配置变更

修改Git仓库中的配置文件后，提交变更，配置中心会自动检测到变更并推送给所有客户端。

## 安全配置

配置中心使用Spring Security进行权限控制：

- 用户名：user，密码：password，角色：USER
- 用户名：admin，密码：password，角色：ADMIN

## 健康监测

访问健康监测端点：http://localhost:8080/actuator/health

## 加密解密

### 加密

```bash
curl -u user:password -X POST http://localhost:8080/encrypt -d 'your-secret'
```

### 解密

```bash
curl -u user:password -X POST http://localhost:8080/decrypt -d 'encrypted-value'
```

在配置文件中使用加密值：

```yaml
spring:
  datasource:
    password: ENC(encrypted-value)
```

## 集群部署

配置中心支持集群部署，通过Git仓库实现配置同步。多个配置中心实例可以共享同一个Git仓库，实现高可用。

## 备份与恢复

配置文件存储在Git仓库中，通过Git的版本控制和分支管理实现配置的备份与恢复。

## 性能优化

- 使用本地缓存减少Git仓库访问次数
- 配置Git仓库的搜索路径，减少搜索范围
- 启用压缩传输

## 审计日志

配置变更的审计日志记录在应用日志中，包括变更时间、应用名称、环境、版本等信息。

## 联系方式

- 作者：fc
- 邮箱：fc@example.com
- 项目地址：https://gitee.com/bdj/SpringBoot_v2
