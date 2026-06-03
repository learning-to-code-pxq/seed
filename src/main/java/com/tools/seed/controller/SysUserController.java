package com.tools.seed.controller;

import com.tools.seed.common.annotation.RequiresPerms;
import com.tools.seed.common.result.Result;
import com.tools.seed.common.vo.UserVO;
import com.tools.seed.entity.SysUser;
import com.tools.seed.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "用户管理", description = "用户的增删改查接口")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    @Operation(summary = "根据ID获取用户信息")

    @GetMapping("/{id}")
    public Result<UserVO> getById(@PathVariable Long id) {
        return Result.success(new UserVO(userService.getById(id)));
    }

    @Operation(summary = "获取所有用户信息")
    @RequiresPerms("system:user:list")
    @GetMapping("/list")
    public Result<List<UserVO>> list() {
        return Result.success(userService.list().stream().map(UserVO::new).collect(Collectors.toList()));
    }

    @Operation(summary = "创建用户")
    @RequiresPerms("system:user:add")
    @PostMapping
    public Result<Void> create(@RequestBody SysUser user) {
        userService.save(user);
        return Result.success();
    }

    @Operation(summary = "更新用户信息")
    @PutMapping
    public Result<Void> update(@RequestBody SysUser user) {
        userService.updateById(user);
        return Result.success();
    }

    @Operation(summary = "逻辑删除用户")
    @RequiresPerms("system:user:delete")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        userService.logicDeleteById(id);
        return Result.success();
    }
}