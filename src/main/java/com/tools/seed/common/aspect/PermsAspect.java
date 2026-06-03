package com.tools.seed.common.aspect;

import com.tools.seed.common.annotation.RequiresPerms;
import com.tools.seed.common.exception.BusinessException;
import com.tools.seed.system.service.SysPermissionService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class PermsAspect {

    private final SysPermissionService permissionService;
    private final StringRedisTemplate redisTemplate;

    @Around("@annotation(requiresPerms)")
    public Object checkPermission(ProceedingJoinPoint joinPoint,
                                  RequiresPerms requiresPerms) throws Throwable {
        // 1. 从request取userId（JwtInterceptor存入的）
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes()).getRequest();
        Object userIdObj = request.getAttribute("userId");
        if (!(userIdObj instanceof Long userId)) {
            throw new BusinessException(401, "未登录");
        }

        // 2. 先从Redis缓存取权限
        String cacheKey = "user:perms:" + userId;
        Set<String> perms;
        String cached = redisTemplate.opsForValue().get(cacheKey);

        if (StringUtils.hasText(cached)) {
            perms = new HashSet<>(Arrays.asList(cached.split(",")));
        } else {
            // 3. 缓存没有，查库
            perms = permissionService.getUserPerms(userId);
            // 缓存30分钟
            redisTemplate.opsForValue().set(cacheKey,
                    String.join(",", perms), 30, TimeUnit.MINUTES);
        }

        if (perms.contains("*:*:*")) return joinPoint.proceed(); // 超级管理员直接放行

        // 4. 校验
        String required = requiresPerms.value();
        if (!perms.contains(required)) {
            throw new BusinessException("权限不足：" + required);
        }

        return joinPoint.proceed();
    }
}
