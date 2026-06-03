package com.tools.seed.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.seed.system.dto.UserRoleAssignDTO;
import com.tools.seed.system.entity.SysRole;
import com.tools.seed.system.entity.SysUserRole;
import com.tools.seed.system.mapper.SysRoleMapper;
import com.tools.seed.system.mapper.SysUserRoleMapper;
import com.tools.seed.system.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author 22913
* @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Service实现
* @createDate 2026-06-01 09:09:49
*/
@Service
@RequiredArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole>
    implements SysUserRoleService{

    private final SysRoleMapper sysRoleMapper;
    private final StringRedisTemplate redisTemplate;

    /** 分配用户角色 */
    @Override
    @Transactional
    public void assignRoles(UserRoleAssignDTO dto) {
        this.remove(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, dto.getUserId())
        );
        List<SysUserRole> list = dto.getRoleIds().stream().map(roleId -> {
            SysUserRole ur = new SysUserRole();
            ur.setUserId(dto.getUserId());
            ur.setRoleId(roleId);
            return ur;
        }).toList();
        list.forEach(this::save);
        redisTemplate.delete("user:perms:" + dto.getUserId());
    }

    /**  查用户的所有角色 */
    @Override
    public List<SysRole> getUserRoles(Long userId) {
        List<Long> roleIds = this.list(
                Wrappers.<SysUserRole>lambdaQuery().eq(SysUserRole::getUserId, userId)
        ).stream().map(SysUserRole::getRoleId).toList();
        if (roleIds.isEmpty()) return List.of();
        return sysRoleMapper.selectByIds(roleIds);
    }
}




