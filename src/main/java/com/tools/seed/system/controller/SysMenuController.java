package com.tools.seed.system.controller;

import com.tools.seed.common.result.Result;
import com.tools.seed.system.dto.MenuDTO;
import com.tools.seed.system.entity.SysMenu;
import com.tools.seed.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@RequiredArgsConstructor
@Tag(name = "菜单管理")
public class SysMenuController {

    private final SysMenuService menuService;

    @GetMapping("/list")
    @Operation(summary = "菜单扁平列表（前端自行构建树）")
    public Result<List<SysMenu>> listUndeletedMenusBySort() {
        return Result.success(menuService.listUndeletedMenusBySort());
    }

    @PostMapping
    @Operation(summary = "创建菜单")
    public Result<Void> create(@RequestBody @Valid MenuDTO dto) {
        menuService.create(dto);
        return Result.success();
    }

    @PutMapping
    @Operation(summary = "更新菜单")
    public Result<Void> update(@RequestBody @Valid MenuDTO dto) {
        menuService.update(dto);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除菜单")
    public Result<Void> delete(@PathVariable Long id) {
        menuService.delete(id);
        return Result.success();
    }
}
