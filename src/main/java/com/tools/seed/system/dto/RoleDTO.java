package com.tools.seed.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Schema(description = "角色DTO")
@Data
public class RoleDTO {
    @Schema(description = "角色ID")
    private Long id;

    @Schema(description = "角色标识")
    @NotBlank(message = "角色标识不能为空")
    @Size(max = 64)
    private String roleKey;

    @Schema(description = "角色名称")
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 64)
    private String roleName;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;
}