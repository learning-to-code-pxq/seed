package com.tools.seed.system.controller;

import com.tools.seed.common.result.Result;
import com.tools.seed.system.dto.UserRoleAssignDTO;
import com.tools.seed.system.entity.SysRole;
import com.tools.seed.system.service.SysUserRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/role")
@RequiredArgsConstructor
@Tag(name = "用户角色分配")
public class SysUserRoleController {

    private final SysUserRoleService userRoleService;

    @PutMapping
    @Operation(summary = "分配用户角色")
    public Result<Void> assignRoles(@RequestBody @Valid UserRoleAssignDTO dto) {
        userRoleService.assignRoles(dto);
        return Result.success();
    }

    @GetMapping("/{userId}")
    @Operation(summary = "查询用户角色列表")
    public Result<List<SysRole>> getUserRoles(@PathVariable Long userId) {
        return Result.success(userRoleService.getUserRoles(userId));
    }
}
