-- 2026-05-22 创建数据库
CREATE DATABASE IF NOT EXISTS `seed_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `seed_platform`;

-- 新建用户表
CREATE TABLE IF NOT EXISTS `sys_user` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` varchar(50) NOT NULL COMMENT '用户名',
    `password` varchar(100) NOT NULL COMMENT '密码',
    `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
    `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态 1正常 0禁用',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除 0未删除 1已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2026-05-25 插入一条测试数据，password这个是123456的BCrypt密文
-- INSERT INTO `sys_user` (`username`, `password`, `nickname`, `status`) VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', 1);

-- 2026-05-30 新建文件表
CREATE TABLE IF NOT EXISTS `sys_file` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
    `original_name` varchar(255) NOT NULL COMMENT '原始文件名',
    `stored_name` varchar(255) NOT NULL COMMENT '存储文件名',
    `file_path` varchar(500) NOT NULL COMMENT '存储路径',
    `file_size` bigint NOT NULL COMMENT '文件大小(字节)',
    `file_type` varchar(100) DEFAULT NULL COMMENT '文件MIME类型',
    `extension` varchar(20) DEFAULT NULL COMMENT '文件扩展名',
    `upload_user_id` bigint DEFAULT NULL COMMENT '上传者ID',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件表';

-- 2026-06-01
-- 角色表
CREATE TABLE IF NOT EXISTS `sys_role` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `role_key`    VARCHAR(64)  NOT NULL COMMENT '角色标识（如admin、editor）',
    `role_name`   VARCHAR(64)  NOT NULL COMMENT '角色名称',
    `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态（1正常 0禁用）',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0未删 1已删）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_key` (`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 菜单表（parent_id树形，后端返回扁平列表，前端构建树）
CREATE TABLE IF NOT EXISTS `sys_menu` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
    `parent_id`   BIGINT       NOT NULL DEFAULT 0 COMMENT '父菜单ID（0为顶级）',
    `name`        VARCHAR(64)  NOT NULL COMMENT '菜单名称',
    `path`        VARCHAR(255) DEFAULT NULL COMMENT '路由路径',
    `icon`        VARCHAR(64)  DEFAULT NULL COMMENT '图标',
    `component`   VARCHAR(255) DEFAULT NULL COMMENT '前端组件路径',
    `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态（1正常 0禁用）',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- 权限点/按钮表
CREATE TABLE IF NOT EXISTS `sys_operation` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '权限点ID',
    `menu_id`     BIGINT       NOT NULL COMMENT '所属菜单ID',
    `name`        VARCHAR(64)  NOT NULL COMMENT '权限点名称',
    `perms`       VARCHAR(128) NOT NULL COMMENT '权限标识（如system:user:add）',
    `sort`        INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态（1正常 0禁用）',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_perms` (`perms`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限点表';

-- 用户-角色关联表
CREATE TABLE IF NOT EXISTS `sys_user_role` (
    `id`      BIGINT NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色-菜单关联表
CREATE TABLE IF NOT EXISTS `sys_role_menu` (
    `id`      BIGINT NOT NULL AUTO_INCREMENT,
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `menu_id` BIGINT NOT NULL COMMENT '菜单ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_menu` (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色菜单关联表';

-- 角色-权限点关联表
CREATE TABLE IF NOT EXISTS `sys_role_operation` (
    `id`           BIGINT NOT NULL AUTO_INCREMENT,
    `role_id`      BIGINT NOT NULL COMMENT '角色ID',
    `operation_id` BIGINT NOT NULL COMMENT '权限点ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_role_operation` (`role_id`, `operation_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限点关联表';

-- 超级管理员角色
-- INSERT INTO sys_role (role_key, role_name, sort, status) VALUES ('admin', '超级管理员', 1, 1);

-- 示例菜单（两级）
-- INSERT INTO sys_menu (parent_id, name, path, icon, component, sort) VALUES (0, '系统管理', '/system', 'setting', NULL, 1);
-- INSERT INTO sys_menu (parent_id, name, path, icon, component, sort) VALUES (1, '用户管理', '/system/user', 'user', 'system/user/index', 1);
-- INSERT INTO sys_menu (parent_id, name, path, icon, component, sort) VALUES (1, '角色管理', '/system/role', 'peoples', 'system/role/index', 2);
-- INSERT INTO sys_menu (parent_id, name, path, icon, component, sort) VALUES (1, '菜单管理', '/system/menu', 'tree-table', 'system/menu/index', 3);

-- 示例权限点
-- INSERT INTO sys_operation (menu_id, name, perms, sort) VALUES (2, '用户查询', 'system:user:list', 1);
-- INSERT INTO sys_operation (menu_id, name, perms, sort) VALUES (2, '用户新增', 'system:user:add', 2);
-- INSERT INTO sys_operation (menu_id, name, perms, sort) VALUES (2, '用户修改', 'system:user:edit', 3);
-- INSERT INTO sys_operation (menu_id, name, perms, sort) VALUES (2, '用户删除', 'system:user:delete', 4);

-- admin角色关联所有菜单和权限点（按实际ID填写）
-- INSERT INTO sys_role_menu (role_id, menu_id) VALUES (1, 1), (1, 2), (1, 3), (1, 4);
-- INSERT INTO sys_role_operation (role_id, operation_id) VALUES (1, 1), (1, 2), (1, 3), (1, 4);

-- 给用户分配admin角色
-- INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);