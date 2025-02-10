DROP TABLE IF EXISTS `sys_route`;
CREATE TABLE `sys_route` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
`name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
`host` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '域名',
`path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '地址',
`parent_menu_id` bigint DEFAULT NULL COMMENT '父级菜单',
`cacheable` bit DEFAULT 0 COMMENT '是否缓存保活',
`compatibility` bit DEFAULT 0 COMMENT '是否降级兼容浏览器',
`icon` varchar(255) NOT NULL COMMENT '图标',
`sort` int DEFAULT 0 COMMENT '排序值',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '路由表' ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `sys_page`;
CREATE TABLE `sys_page` (
`id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
`code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '编码',
`name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '名称',
`content` longtext CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '页面json',
`remark` varchar(255) DEFAULT '' COMMENT '备注',
`status` int NOT NULL DEFAULT 1 COMMENT '状态 - 0:停用,1:启用',
`deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除标记 - 0:正常,1:已删除',
`create_by` bigint DEFAULT null COMMENT '创建人',
`create_date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
`update_by` bigint DEFAULT null COMMENT '最后更新人',
`update_date` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' COMMENT '最后更新时间',
PRIMARY KEY (`id`) USING BTREE,
UNIQUE KEY `code`(`code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '页面表' ROW_FORMAT = Compact;
