package com.tools.seed.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.seed.common.exception.BusinessException;
import com.tools.seed.system.entity.SysMenu;
import com.tools.seed.system.mapper.SysMenuMapper;
import com.tools.seed.system.service.SysMenuService;
import com.tools.seed.system.dto.MenuDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 22913
* @description 针对表【sys_menu(菜单表)】的数据库操作Service实现
* @createDate 2026-06-01 09:09:49
*/
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu>
    implements SysMenuService{

    /**  扁平列表，前端自行构建树 */
    @Override
    public List<SysMenu> listUndeletedMenusBySort() {
        return this.list(
                Wrappers.<SysMenu>lambdaQuery()
                        .eq(SysMenu::getStatus, 1)
                        .orderByAsc(SysMenu::getSort)
        );
    }

    @Override
    public void create(MenuDTO dto) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(dto, menu);
        this.save(menu);
    }

    @Override
    public void update(MenuDTO dto) {
        SysMenu menu = new SysMenu();
        BeanUtils.copyProperties(dto, menu);
        this.updateById(menu);
    }

    @Override
    public void delete(Long id) {
        // 检查是否有子菜单
        long count = this.count(
                Wrappers.<SysMenu>lambdaQuery().eq(SysMenu::getParentId, id)
        );
        if (count > 0) {
            throw new BusinessException("存在子菜单，无法删除");
        }
        this.lambdaUpdate().set(SysMenu::getDeleted, true).eq(SysMenu::getId, id).update();
    }
}




