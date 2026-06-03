package com.tools.seed.system.service;

import com.tools.seed.system.entity.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.seed.system.dto.RoleDTO;
import com.tools.seed.system.dto.RoleMenuAssignDTO;
import com.tools.seed.system.dto.RoleOperationAssignDTO;

import java.util.List;
import java.util.Map;

/**
* @author 22913
* @description 针对表【sys_role(角色表)】的数据库操作Service
* @createDate 2026-06-01 09:09:49
*/
public interface SysRoleService extends IService<SysRole> {

    List<SysRole> listBySort();

    Map<String, Object> detail(Long id);

    void create(RoleDTO dto);

    void update(RoleDTO dto);

    void delete(Long id);

    void assignMenus(RoleMenuAssignDTO dto);

    void assignOperations(RoleOperationAssignDTO dto);
}
