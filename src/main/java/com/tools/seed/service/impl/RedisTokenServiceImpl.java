package com.tools.seed.service.impl;

import com.tools.seed.service.RedisTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {

    private final StringRedisTemplate redisTemplate;
    // key格式: token:user:{userId}
    private static final String TOKEN_KEY_PREFIX = "token:user:";

    /**
     * 存储token到Redis，过期时间和JWT一致
     */
    @Override
    public void saveToken(Long userId, String token, Duration expire) {
        String key = TOKEN_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, token, expire);
    }

    /**
     * 验证token是否有效（Redis中存在且值一致）
     */
    @Override
    public boolean validateToken(Long userId, String token) {
        String key = TOKEN_KEY_PREFIX + userId;
        String stored = redisTemplate.opsForValue().get(key);
        return token.equals(stored);
    }

    /**
     * 删除token（改密/登出时调用）
     */
    @Override
    public void deleteToken(Long userId) {
        String key = TOKEN_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
}
