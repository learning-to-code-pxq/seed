package com.tools.seed.controller;

import com.tools.seed.common.dto.ChangePasswordRequest;
import com.tools.seed.common.dto.LoginRequest;
import com.tools.seed.common.dto.LoginResponse;
import com.tools.seed.common.dto.RegisterRequest;
import com.tools.seed.common.result.Result;
import com.tools.seed.entity.SysUser;
import com.tools.seed.service.AuthService;
import com.tools.seed.service.SysUserService;
import com.tools.seed.system.service.SysPermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Tag(name = "认证管理", description = "登录、注册、改密")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final SysUserService userService;
    private final SysPermissionService permissionService;

    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request,
                                       HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        return Result.success(authService.login(request, ip));
    }

    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return Result.success();
    }

    @Operation(summary = "修改密码")
    @PutMapping("/password")
    public Result<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request,
                                       HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        authService.changePassword(userId, request);
        return Result.success();
    }

    @GetMapping("/info")
    @Operation(summary = "当前用户信息+权限列表")
    public Result<Map<String, Object>> userInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        SysUser user = userService.getById(userId);

        Set<String> perms = permissionService.getUserPerms(userId);
        Set<Long> menuIds = permissionService.getUserMenuIds(userId);

        Map<String, Object> info = new HashMap<>();
        info.put("user", user);
        info.put("perms", perms);
        info.put("menuIds", menuIds);
        return Result.success(info);
    }
}
