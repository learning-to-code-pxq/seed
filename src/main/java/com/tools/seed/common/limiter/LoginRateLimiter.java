package com.tools.seed.common.limiter;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

// 简单方案：用Guava做内存限流
@Schema(description = "登录限流器，防止暴力破解")
@Component
public class LoginRateLimiter {
    // 同一IP，每分钟最多5次登录失败
    @Schema(description = "同一IP，每分钟最多5次登录失败")
    private final Cache<String, AtomicInteger> failCount = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES)
            .build();

    @Schema(description = "判断IP是否被限流")
    public boolean isBlocked(String ip) {
        AtomicInteger count = failCount.getIfPresent(ip);
        return count != null && count.get() >= 5;
    }

    @Schema(description = "记录登录失败")
    public void recordFail(String ip) {
        failCount.asMap().compute(ip, (k, v) -> v == null ? new AtomicInteger(1) : v).incrementAndGet();
    }
}
