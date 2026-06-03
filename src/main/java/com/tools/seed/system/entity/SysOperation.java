package com.tools.seed.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 权限点表
 * @TableName sys_operation
 */
@Schema(description = "权限点表")
@TableName(value ="sys_operation")
@Data
public class SysOperation implements Serializable {
    /**
     * 权限点ID
     */
    @Schema(description = "权限点ID")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属菜单ID
     */
    @Schema(description = "所属菜单ID")
    private Long menuId;

    /**
     * 权限点名称
     */
    @Schema(description = "权限点名称")
    private String name;

    /**
     * 权限标识（如system:user:add）
     */
    @Schema(description = "权限标识")
    private String perms;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 状态（1正常 0禁用）
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除
     */
    @Schema(description = "逻辑删除")
    @TableLogic
    private Integer deleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}