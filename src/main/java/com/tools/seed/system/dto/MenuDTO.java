package com.tools.seed.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "菜单DTO")
@Data
public class MenuDTO {
    @Schema(description = "菜单ID")
    private Long id;

    @Schema(description = "父菜单ID（0为顶级）")
    private Long parentId;

    @Schema(description = "菜单名称")
    @NotBlank(message = "菜单名称不能为空")
    @Size(max = 64)
    private String name;

    @Schema(description = "路由路径")
    private String path;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "前端组件路径")
    private String component;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;
}
