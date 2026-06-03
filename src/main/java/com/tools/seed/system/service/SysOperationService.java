package com.tools.seed.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.tools.seed.system.entity.SysOperation;
import com.tools.seed.system.dto.OperationDTO;

import java.util.List;

/**
* @author 22913
* @description 针对表【sys_operation(权限点表)】的数据库操作Service
* @createDate 2026-06-01 09:09:49
*/
public interface SysOperationService extends IService<SysOperation> {

    List<SysOperation> listBySort();

    void create(OperationDTO dto);

    void update(OperationDTO dto);

    void delete(Long id);
}
