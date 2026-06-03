package com.tools.seed.system.controller;

import com.tools.seed.common.result.Result;
import com.tools.seed.system.dto.RoleDTO;
import com.tools.seed.system.dto.RoleMenuAssignDTO;
import com.tools.seed.system.dto.RoleOperationAssignDTO;
import com.tools.seed.system.entity.SysRole;
import com.tools.seed.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor
@Tag(name = "角色管理")
public class SysRoleController {

    private final SysRoleService roleService;

    @GetMapping("/list")
    @Operation(summary = "角色列表")
    public Result<List<SysRole>> listBySort() {
        return Result.success(roleService.listBySort());
    }

    @GetMapping("/{id}")
    @Operation(summary = "角色详情（含已分配菜单和权限点）")
    public Result<Map<String, Object>> detail(@PathVariable Long id) {
        return Result.success(roleService.detail(id));
    }

    @PostMapping
    @Operation(summary = "创建角色")
    public Result<Void> create(@RequestBody @Valid RoleDTO dto) {
        roleService.create(dto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新角色")
    public Result<Void> update(@RequestBody @Valid RoleDTO dto) {
        roleService.update(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除角色")
    public Result<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return Result.success();
    }

    @PutMapping("/menu")
    @Operation(summary = "分配角色菜单")
    public Result<Void> assignMenus(@RequestBody @Valid RoleMenuAssignDTO dto) {
        roleService.assignMenus(dto);
        return Result.success();
    }

    @PutMapping("/operation")
    @Operation(summary = "分配角色权限点")
    public Result<Void> assignOperations(@RequestBody @Valid RoleOperationAssignDTO dto) {
        roleService.assignOperations(dto);
        return Result.success();
    }
}