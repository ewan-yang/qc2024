DROP TABLE IF EXISTS `sys_unit`;
CREATE TABLE `sys_unit` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`parent_id` bigint DEFAULT 0 COMMENT '父节点ID',
`relation_path` varchar(255) DEFAULT '' COMMENT '根节点到父级组织的全路径',
`code` varchar(50) NOT NULL COMMENT '编码',
`name` varchar(100) NOT NULL COMMENT '名称',
`type` int DEFAULT 1 COMMENT '公司、部门的区分，1表示公司，2表示部门',
`sort` int DEFAULT 0 COMMENT '排序值',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
INDEX `parent_id`(`parent_id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '组织结构表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`code` varchar(50) NOT NULL COMMENT '登录名',
`name` varchar(200) NOT NULL COMMENT '姓名',
`work_number` varchar(100) DEFAULT '' COMMENT '工号',
`password` varchar(200) NOT NULL COMMENT '密码',
`sex` varchar(10) DEFAULT '' COMMENT '性别(男/女)',
`position` varchar(50) DEFAULT '' COMMENT '职务',
`audit_type` varchar(50) DEFAULT '',
`headimgurl` varchar(255) DEFAULT '' COMMENT '头像图片链接地址',
`telephone` varchar(100) DEFAULT '' COMMENT '办公电话',
`mobile` varchar(100) DEFAULT '' COMMENT '手机号码',
`email` varchar(100) DEFAULT '' COMMENT '邮箱',
`password_date` datetime DEFAULT NULL COMMENT '密码修改日期',
`birth_date` datetime DEFAULT NULL COMMENT '生日',
`entry_date` datetime DEFAULT NULL COMMENT '注册时间',
`work_date` datetime DEFAULT NULL COMMENT '登录成功时间',
`ip_range` varchar(500) DEFAULT '' COMMENT 'IP地址范围，分号分隔，支持*通配符',
`login_fail_ip` varchar(45) DEFAULT '' COMMENT '最后登录失败IP',
`login_fail_date` datetime DEFAULT NULL COMMENT '最后登录失败时间',
`login_flag` int DEFAULT 1 COMMENT '是否可登录，0-长期锁定，1-可登录，2-短期锁定',
`login_success_date` datetime DEFAULT NULL COMMENT '登录成功时间',
`login_success_ip` varchar(45) DEFAULT '' COMMENT '登录成功IP',
`lock_date` datetime DEFAULT NULL COMMENT '锁定时间',
`token` varchar(500) DEFAULT '' COMMENT 'token验证',
`login_fail_count` int DEFAULT 0 COMMENT '登录失败次数',
`rev` int DEFAULT 0 COMMENT '版本号',
`first` varchar(255) DEFAULT '' COMMENT '姓',
`last` varchar(255) DEFAULT '' COMMENT '名',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

CREATE TABLE `sys_user_unit` (
`id` bigint NOT NULL AUTO_INCREMENT,
`user_id` bigint NOT NULL COMMENT '用户id',
`unit_id` bigint NOT NULL COMMENT '部门id',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `user_unit`(`user_id`,`unit_id`) USING BTREE
) ENGINE=InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户组织关系表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`user_id` bigint NOT NULL COMMENT '用户ID',
`role_id` bigint NOT NULL COMMENT '角色ID',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `user_role`(`user_id`,`role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`code` varchar(50) NOT NULL COMMENT '角色编码',
`name` varchar(100) NOT NULL COMMENT '角色名称',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色信息表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`code` varchar(50) NOT NULL COMMENT '资源编码',
`name` varchar(100) NOT NULL COMMENT '资源名称',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统资源表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_operation`;
CREATE TABLE `sys_operation` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`code` varchar(50) NOT NULL COMMENT '操作编码',
`name` varchar(100) NOT NULL COMMENT '操作名称',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '操作信息表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`resource_id` bigint(20) NULL DEFAULT NULL COMMENT '资源ID',
`operation_id` bigint(20) NULL DEFAULT NULL COMMENT '操作ID',
`permission_code` varchar(255) NULL DEFAULT NULL COMMENT '权限code',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `join_permission_index`(`resource_id`,`operation_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统权限表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`role_id` bigint NOT NULL COMMENT '角色ID',
`permission_id` bigint NOT NULL COMMENT '权限ID',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `role_permission_index`(`role_id`,`permission_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色与系统权限关联表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
`name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
`parent_id` bigint DEFAULT NULL COMMENT '父节点主键',
`component_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '主页面路径',
`permission` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '权限',
`sort` int DEFAULT 0 COMMENT '排序',
`type` int DEFAULT 1 COMMENT '类型，1-内部链接，2-外部链接',
`url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '链接',
`css` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '样式',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE,
INDEX `parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_lov`;
CREATE TABLE `sys_lov` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT 'LOV_ID',
`code` varchar(50) NOT NULL COMMENT 'LOV编码',
`name` varchar(100) NOT NULL COMMENT 'LOV名称',
`module` varchar(20) DEFAULT 'global' COMMENT '系统模块',
`type` int DEFAULT 0 COMMENT '是否鉴权 - 0不鉴权,1:鉴权',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE,
INDEX `name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'LOV定义表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_lov_line`;
CREATE TABLE `sys_lov_line` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`lov_id` bigint NOT NULL COMMENT 'LOV主键',
`code` varchar(50) NOT NULL COMMENT '编码值',
`name` varchar(100) NOT NULL COMMENT '显示值',
`value` varchar(255) DEFAULT '' COMMENT '值',
`sort` int DEFAULT 0 COMMENT '显示排序',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `lov_code`(`lov_id`,`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'LOV明细行' ROW_FORMAT = Compact;
