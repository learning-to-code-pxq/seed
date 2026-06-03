package com.tools.seed.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "权限点DTO")
@Data
public class OperationDTO {
    @Schema(description = "权限点ID")
    private Long id;

    @Schema(description = "所属菜单ID")
    @NotNull(message = "所属菜单不能为空")
    private Long menuId;

    @Schema(description = "权限点名称")
    @NotBlank(message = "权限点名称不能为空")
    @Size(max = 64)
    private String name;

    @Schema(description = "权限标识")
    @NotBlank(message = "权限标识不能为空")
    @Size(max = 128)
    private String perms;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;
}
