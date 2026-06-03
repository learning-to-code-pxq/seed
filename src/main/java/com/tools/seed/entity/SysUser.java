package com.tools.seed.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户表
 * @TableName sys_user
 */
@Schema(name = "SysUser", description = "用户表")
@TableName(value ="sys_user")
@Data
public class SysUser implements Serializable {
    /**
     * 主键
     */
    @Schema(name = "id", description = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @Schema(name = "username", description = "用户名")
    private String username;

    /**
     * 密码
     */
    @Schema(name = "password", description = "密码")
    private String password;

    /**
     * 昵称
     */
    @Schema(name = "nickname", description = "昵称")
    private String nickname;

    /**
     * 邮箱
     */
    @Schema(name = "email", description = "邮箱")
    private String email;

    /**
     * 状态 1正常 0禁用
     */
    @Schema(name = "status", description = "状态 1正常 0禁用")
    private Integer status;

    /**
     * 创建时间
     */
    @Schema(name = "createTime", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Schema(name = "updateTime", description = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除 0未删除 1已删除
     */
    @Schema(name = "deleted", description = "逻辑删除 0未删除 1已删除")
    @TableLogic
    private Integer deleted;

    @Serial
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}