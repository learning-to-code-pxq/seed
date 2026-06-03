package com.tools.seed.config;

import com.tools.seed.common.exception.BusinessException;
import com.tools.seed.service.RedisTokenService;
import com.tools.seed.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从Header取token
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            throw new BusinessException(401, "未登录");
        }

        // 2. 去掉Bearer前缀（如果有）
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // 3. 校验token
        if (!jwtUtil.validateToken(token)) {
            throw new BusinessException(401, "登录已过期，请重新登录");
        }

        Long userId = jwtUtil.getUserIdFromToken(token);

        // 原来只验签名，现在多验一步Redis
        if (!redisTokenService.validateToken(userId, token)) {
            // token不存在或已被替换（改密/登出），拒绝
            throw new BusinessException(401, "登录已过期，请重新登录");
        }

        // 4. 解析用户信息，存到request里供后续使用
        request.setAttribute("userId", userId);
        request.setAttribute("username", jwtUtil.getUsernameFromToken(token));

        return true;
    }
}
