package com.tools.seed.system.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tools.seed.system.entity.SysOperation;
import com.tools.seed.system.mapper.SysOperationMapper;
import com.tools.seed.system.service.SysOperationService;
import com.tools.seed.system.dto.OperationDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 22913
* @description 针对表【sys_operation(权限点表)】的数据库操作Service实现
* @createDate 2026-06-01 09:09:49
*/
@Service
public class SysOperationServiceImpl extends ServiceImpl<SysOperationMapper, SysOperation>
    implements SysOperationService{

    @Override
    public List<SysOperation> listBySort() {
        return this.list(
                Wrappers.<SysOperation>lambdaQuery().orderByAsc(SysOperation::getSort)
        );
    }

    @Override
    public void create(OperationDTO dto) {
        SysOperation op = new SysOperation();
        BeanUtils.copyProperties(dto, op);
        this.save(op);
    }

    @Override
    public void update(OperationDTO dto) {
        SysOperation op = new SysOperation();
        BeanUtils.copyProperties(dto, op);
        this.updateById(op);
    }

    @Override
    public void delete(Long id) {
        this.lambdaUpdate().set(SysOperation::getDeleted, true).eq(SysOperation::getId, id).update();
    }
}




