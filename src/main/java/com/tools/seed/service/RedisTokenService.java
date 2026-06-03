package com.tools.seed.service;

public interface RedisTokenService {
    /**
     * 存储token到Redis，过期时间和JWT一致
     */
    void saveToken(Long userId, String token, java.time.Duration expire);

    /**
     * 验证token是否有效（Redis中存在且值一致）
     */
    boolean validateToken(Long userId, String token);

    /**
     * 删除token（改密/登出时调用）
     */
    void deleteToken(Long userId);
}
