package com.tools.seed.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.seed.entity.SysUser;

/**
* @author 22913
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2026-05-22 15:47:36
*/
public interface SysUserService extends IService<SysUser> {

    void logicDeleteById(Long id);

    SysUser getByUsername(String username);
}
