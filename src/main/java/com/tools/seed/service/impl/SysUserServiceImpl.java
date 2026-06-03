package com.tools.seed.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.seed.entity.SysUser;
import com.tools.seed.mapper.SysUserMapper;
import com.tools.seed.service.SysUserService;
import org.springframework.stereotype.Service;

/**
* @author 22913
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2026-05-22 15:47:36
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Override
    public void logicDeleteById(Long id) {
        this.lambdaUpdate().set(SysUser::getDeleted, true)
            .eq(SysUser::getId, id)
            .update();
    }

    @Override
    public SysUser getByUsername(String username) {
        return this.getOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username));
    }

}




