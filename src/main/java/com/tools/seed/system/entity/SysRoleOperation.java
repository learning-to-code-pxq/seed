package com.tools.seed.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色权限点关联表
 * @TableName sys_role_operation
 */
@Schema(description = "角色权限点关联表")
@TableName(value ="sys_role_operation")
@Data
public class SysRoleOperation implements Serializable {
    /**
     * ID
     */
    @Schema(description = "ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID")
    private Long roleId;

    /**
     * 权限点ID
     */
    @Schema(description = "权限点ID")
    private Long operationId;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}