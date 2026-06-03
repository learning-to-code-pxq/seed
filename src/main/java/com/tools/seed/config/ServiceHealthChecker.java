package com.tools.seed.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceHealthChecker implements ApplicationRunner {

    private final DataSource dataSource;
    private final RedisConnectionFactory redisConnectionFactory;

    @Override
    public void run(ApplicationArguments args) {
        checkMySQL();
        checkRedis();
        log.info("✅ 所有依赖服务检测通过");
    }

    private void checkMySQL() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(3)) {
                log.info("✅ MySQL 连接正常");
            } else {
                throw new RuntimeException("MySQL 连接验证失败");
            }
        } catch (Exception e) {
            log.error("❌ MySQL 不可用: {}", e.getMessage());
            throw new RuntimeException("MySQL 不可用，启动终止", e);
        }
    }

    private void checkRedis() {
        try {
            String pong = redisConnectionFactory.getConnection().ping();
            if ("PONG".equalsIgnoreCase(pong)) {
                log.info("✅ Redis 连接正常");
            } else {
                throw new RuntimeException("Redis PING 返回异常: " + pong);
            }
        } catch (Exception e) {
            log.error("❌ Redis 不可用: {}", e.getMessage());
            throw new RuntimeException("Redis 不可用，启动终止", e);
        }
    }
}