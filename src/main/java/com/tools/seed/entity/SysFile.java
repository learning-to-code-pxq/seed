package com.tools.seed.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文件表
 * @TableName sys_file
 */
@Schema(description = "文件表")
@TableName(value ="sys_file")
@Data
public class SysFile implements Serializable {
    /**
     * 主键
     */
    @Schema(description = "主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 原始文件名
     */
    @Schema(description = "原始文件名")
    private String originalName;

    /**
     * 存储文件名
     */
    @Schema(description = "存储文件名")
    private String storedName;

    /**
     * 存储路径
     */
    @Schema(description = "存储路径")
    private String filePath;

    /**
     * 文件大小(字节)
     */
    @Schema(description = "文件大小(字节)")
    private Long fileSize;

    /**
     * 文件MIME类型
     */
    @Schema(description = "文件MIME类型")
    private String fileType;

    /**
     * 文件扩展名
     */
    @Schema(description = "文件扩展名")
    private String extension;

    /**
     * 上传者ID
     */
    @Schema(description = "上传者ID")
    private Long uploadUserId;

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