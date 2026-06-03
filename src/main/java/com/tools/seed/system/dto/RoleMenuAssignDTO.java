package com.tools.seed.system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "角色菜单分配DTO")
@Data
public class RoleMenuAssignDTO {
    @Schema(description = "角色ID")
    @NotNull(message = "角色ID不能为空")
    private Long roleId;

    @Schema(description = "菜单ID列表")
    @NotEmpty(message = "菜单ID列表不能为空")
    private List<Long> menuIds;
}
