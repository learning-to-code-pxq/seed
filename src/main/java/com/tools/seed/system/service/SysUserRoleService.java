package com.tools.seed.system.service;

import com.tools.seed.system.entity.SysRole;
import com.tools.seed.system.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.seed.system.dto.UserRoleAssignDTO;

import java.util.List;

/**
* @author 22913
* @description 针对表【sys_user_role(用户角色关联表)】的数据库操作Service
* @createDate 2026-06-01 09:09:49
*/
public interface SysUserRoleService extends IService<SysUserRole> {

    void assignRoles(UserRoleAssignDTO dto);

    List<SysRole> getUserRoles(Long userId);
}
