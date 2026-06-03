package com.tools.seed.service.impl;

import com.tools.seed.common.dto.ChangePasswordRequest;
import com.tools.seed.common.dto.LoginRequest;
import com.tools.seed.common.dto.LoginResponse;
import com.tools.seed.common.dto.RegisterRequest;
import com.tools.seed.common.exception.BusinessException;
import com.tools.seed.common.limiter.LoginRateLimiter;
import com.tools.seed.config.JwtProperties;
import com.tools.seed.entity.SysUser;
import com.tools.seed.service.AuthService;
import com.tools.seed.service.RedisTokenService;
import com.tools.seed.service.SysUserService;
import com.tools.seed.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final SysUserService sysUserService;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final PasswordEncoder passwordEncoder;
    private final LoginRateLimiter loginRateLimiter;
    private final RedisTokenService redisTokenService;

    @Override
    public LoginResponse login(LoginRequest request, String ip) {
        // 限流校验放在最前面，密码都不用查
        if (loginRateLimiter.isBlocked(ip)) {
            throw new BusinessException("登录失败次数过多，请1分钟后再试");
        }

        SysUser user = sysUserService.getByUsername(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            loginRateLimiter.recordFail(ip);  // 失败了才计数
            throw new BusinessException("用户名或密码错误");
        }

        // 3. 校验状态
        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        // 4. 生成token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        redisTokenService.saveToken(user.getId(), token, Duration.ofMillis(jwtProperties.getExpiration()));

        return new LoginResponse(token, user.getUsername(), user.getId());
    }

    @Override
    public void register(RegisterRequest request) {
        // 1. 检查用户名是否已存在
        SysUser existing = sysUserService.getByUsername(request.getUsername());
        if (existing != null) {
            throw new BusinessException("用户名已存在");
        }

        // 2. 创建用户
        SysUser user = new SysUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setStatus(1);
        sysUserService.save(user);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordRequest request) {
        // 1. 查用户
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        // 2. 校验旧密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("旧密码错误");
        }

        // 3. 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        sysUserService.updateById(user);
        redisTokenService.deleteToken(user.getId());
    }
}
