package com.tools.seed.system.controller;

import com.tools.seed.common.result.Result;
import com.tools.seed.system.dto.OperationDTO;
import com.tools.seed.system.entity.SysOperation;
import com.tools.seed.system.service.SysOperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/operation")
@RequiredArgsConstructor
@Tag(name = "权限点管理")
public class SysOperationController {

    private final SysOperationService operationService;

    @GetMapping("/list")
    @Operation(summary = "权限点列表")
    public Result<List<SysOperation>> listBySort() {
        return Result.success(operationService.listBySort());
    }

    @PostMapping
    @Operation(summary = "创建权限点")
    public Result<Void> create(@RequestBody @Valid OperationDTO dto) {
        operationService.create(dto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新权限点")
    public Result<Void> update(@RequestBody @Valid OperationDTO dto) {
        operationService.update(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除权限点")
    public Result<Void> delete(@PathVariable Long id) {
        operationService.delete(id);
        return Result.success();
    }
}
