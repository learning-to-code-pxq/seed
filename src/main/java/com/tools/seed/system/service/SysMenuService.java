package com.tools.seed.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.seed.system.entity.SysMenu;
import com.tools.seed.system.dto.MenuDTO;

import java.util.List;

/**
* @author 22913
* @description 针对表【sys_menu(菜单表)】的数据库操作Service
* @createDate 2026-06-01 09:09:49
*/
public interface SysMenuService extends IService<SysMenu> {

    List<SysMenu> listUndeletedMenusBySort();

    void create(MenuDTO dto);

    void update(MenuDTO dto);

    void delete(Long id);
}
