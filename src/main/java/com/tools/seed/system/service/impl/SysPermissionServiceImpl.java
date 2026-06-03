package com.tools.seed.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.tools.seed.system.entity.SysOperation;
import com.tools.seed.system.entity.SysRoleMenu;
import com.tools.seed.system.entity.SysRoleOperation;
import com.tools.seed.system.entity.SysUserRole;
import com.tools.seed.system.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysPermissionServiceImpl implements SysPermissionService {

    private final SysUserRoleService sysUserRoleService;
    private final SysRoleOperationService sysRoleOperationService;
    private final SysOperationService sysOperationService;
    private final SysRoleMenuService sysRoleMenuService;

    /**
     * 获取用户所有权限标识
     * 流程：userId → 角色ID列表 → 权限点ID列表 → perms列表
     */
    @Override
    public Set<String> getUserPerms(Long userId) {
        // 1. 查角色
        List<Long> roleIds = sysUserRoleService.list(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId)
        ).stream().map(SysUserRole::getRoleId).toList();
        if (roleIds.isEmpty()) return Set.of();

        // 2. 查权限点ID
        List<Long> operationIds = sysRoleOperationService.list(
                Wrappers.<SysRoleOperation>lambdaQuery().in(SysRoleOperation::getRoleId, roleIds)
        ).stream().map(SysRoleOperation::getOperationId).toList();
        if (operationIds.isEmpty()) return Set.of();

        // 3. 查perms
        List<SysOperation> operations = sysOperationService.listByIds(operationIds);
        return operations.stream()
                .filter(op -> op.getStatus() == 1)
                .map(SysOperation::getPerms)
                .collect(Collectors.toSet());
    }

    /**  获取用户可见菜单ID（角色关联的菜单） */
    @Override
    public Set<Long> getUserMenuIds(Long userId) {
        List<Long> roleIds = sysUserRoleService.list(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId)
        ).stream().map(SysUserRole::getRoleId).toList();
        if (roleIds.isEmpty()) return Set.of();

        return new HashSet<>(sysRoleMenuService.list(
                Wrappers.<SysRoleMenu>lambdaQuery().in(SysRoleMenu::getRoleId, roleIds)
        ).stream().map(SysRoleMenu::getMenuId).toList());
    }
}
