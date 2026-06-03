package com.tools.seed.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.seed.common.exception.BusinessException;
import com.tools.seed.system.dto.RoleDTO;
import com.tools.seed.system.dto.RoleMenuAssignDTO;
import com.tools.seed.system.dto.RoleOperationAssignDTO;
import com.tools.seed.system.entity.SysRole;
import com.tools.seed.system.entity.SysRoleMenu;
import com.tools.seed.system.entity.SysRoleOperation;
import com.tools.seed.system.entity.SysUserRole;
import com.tools.seed.system.mapper.SysRoleMapper;
import com.tools.seed.system.service.SysRoleMenuService;
import com.tools.seed.system.service.SysRoleOperationService;
import com.tools.seed.system.service.SysRoleService;
import com.tools.seed.system.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author 22913
* @description 针对表【sys_role(角色表)】的数据库操作Service实现
* @createDate 2026-06-01 09:09:49
*/
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole>
    implements SysRoleService{

    private final SysRoleMenuService roleMenuService;
    private final SysRoleOperationService roleOperationService;
    private final SysUserRoleService userRoleService;
    private final StringRedisTemplate redisTemplate;
    @Override
    public List<SysRole> listBySort() {
        return this.list(
                Wrappers.<SysRole>lambdaQuery().orderByAsc(SysRole::getSort)
        );
    }

    /** 查角色详情 + 已分配的菜单ID和权限点ID */
    @Override
    public Map<String, Object> detail(Long id) {
        SysRole role = this.getById(id);
        List<Long> menuIds = roleMenuService.list(
                Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, id)
        ).stream().map(SysRoleMenu::getMenuId).toList();
        List<Long> operationIds = roleOperationService.list(
                Wrappers.<SysRoleOperation>lambdaQuery().eq(SysRoleOperation::getRoleId, id)
        ).stream().map(SysRoleOperation::getOperationId).toList();
        Map<String, Object> result = new HashMap<>();
        result.put("role", role);
        result.put("menuIds", menuIds);
        result.put("operationIds", operationIds);
        return result;
    }

    @Override
    public void create(RoleDTO dto) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        this.save(role);
    }

    @Override
    public void update(RoleDTO dto) {
        SysRole role = new SysRole();
        BeanUtils.copyProperties(dto, role);
        this.updateById(role);
    }

    @Override
    public void delete(Long id) {
        // 检查是否有用户关联此角色
        long count = userRoleService.count(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getRoleId, id)
        );
        if (count > 0) {
            throw new BusinessException("该角色下存在用户，无法删除");
        }
        this.lambdaUpdate().set(SysRole::getDeleted, true).eq(SysRole::getId, id).update();
        // 同步清理关联表
        roleMenuService.remove(
                Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, id)
        );
        roleOperationService.remove(
                Wrappers.<SysRoleOperation>lambdaQuery().eq(SysRoleOperation::getRoleId, id)
        );
    }

    /**  分配角色菜单 */
    @Override
    @Transactional
    public void assignMenus(RoleMenuAssignDTO dto) {
        // 先删后插
        roleMenuService.remove(
                Wrappers.<SysRoleMenu>lambdaQuery().eq(SysRoleMenu::getRoleId, dto.getRoleId())
        );
        List<SysRoleMenu> list = dto.getMenuIds().stream().map(menuId -> {
            SysRoleMenu rm = new SysRoleMenu();
            rm.setRoleId(dto.getRoleId());
            rm.setMenuId(menuId);
            return rm;
        }).toList();
        list.forEach(roleMenuService::save);
        clearRoleUserPermsCache(dto.getRoleId());
    }

    /** 分配角色权限点 */
    @Override
    @Transactional
    public void assignOperations(RoleOperationAssignDTO dto) {
        roleOperationService.remove(
                Wrappers.<SysRoleOperation>lambdaQuery().eq(SysRoleOperation::getRoleId, dto.getRoleId())
        );
        List<SysRoleOperation> list = dto.getOperationIds().stream().map(opId -> {
            SysRoleOperation ro = new SysRoleOperation();
            ro.setRoleId(dto.getRoleId());
            ro.setOperationId(opId);
            return ro;
        }).toList();
        list.forEach(roleOperationService::save);
        clearRoleUserPermsCache(dto.getRoleId());
    }

    /**  清除某角色下所有用户的权限缓存 */
    private void clearRoleUserPermsCache(Long roleId) {
        List<Long> userIds = userRoleService.list(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getRoleId, roleId)
        ).stream().map(SysUserRole::getUserId).toList();
        List<String> keys = userIds.stream()
                .map(uid -> "user:perms:" + uid)
                .toList();
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}




