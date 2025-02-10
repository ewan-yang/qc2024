-- 管理部门 & 管理员用户 & 管理员角色
INSERT INTO `sys_unit`(`id`, `parent_id`, `relation_path`, `code`, `name`, `type`, `sort`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, NULL, '1', 'admin-unit', '管理(公司)', 1, 1, '', 1, 0, 1, 1);
INSERT INTO `sys_unit`(`id`, `parent_id`, `relation_path`, `code`, `name`, `type`, `sort`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (2, 1, '1,2', 'admin-dept', '管理(部门)', 2, 1, '', 1, 0, 1, 1);
INSERT INTO `sys_user`(`id`, `code`, `name`, `work_number`, `password`, `sex`, `position`, `audit_type`, `headimgurl`, `telephone`, `mobile`, `email`, `password_date`, `birth_date`, `entry_date`, `work_date`, `ip_range`, `login_fail_ip`, `login_fail_date`, `login_flag`, `login_success_date`, `login_success_ip`, `lock_date`, `token`, `login_fail_count`, `rev`, `first`, `last`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 'admin', '管理员', '', '7c4a8d09ca3762af61e59520943dc26494f8941b', '男', '', '', '', '', '', '', now(), NULL, NULL, NULL, '', '', NULL, 1, NULL, '', NULL, '', 0, 0, '', '', '', 1, 0, 1, 1);
INSERT INTO `sys_user_unit`(`id`, `user_id`, `unit_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 1, 1, '', 1, 0, 1, 1);
INSERT INTO `sys_role`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 'admin', '管理员', '', 1, 0, 1, 1);
INSERT INTO `sys_user_role`(`id`, `user_id`, `role_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 1, 1, '', 1, 0, 1, 1);

-- 菜单
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 'system', '系统管理', NULL, 'layout', '', 1000, 1, '/sys', 'vea-gear_alt_fill', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (2, 'org', '组织管理', 1, 'common/parentIndex', '', 1, 1, 'orgMgmt', 'vea-equal_square_fill', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (3, 'unit', '组织机构管理', 2, 'baseData/organizationMgmt/index', 'unit:view', 4, 1, 'organizationMgmt', 'vea-rectangle_stack_person_crop', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (4, 'user', '用户管理', 2, 'baseData/userMgmt/index', 'user:view', 2, 1, 'userMgmt', 'vea-person_crop_circle', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (5, 'role', '角色管理', 2, 'baseData/role/index', 'role:view', 3, 1, 'roleMgmt', 'vea-person_crop_circle_badge_checkmark', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (6, 'function', '功能管理', 1, 'common/parentIndex', '', 2, 1, 'functionMgmt', 'vea-gear', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (7, 'menu', '菜单管理', 6, 'baseData/menuMgmt/index', 'menu:view', 1, 1, 'menuMgmt', 'vea-list_dash', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (8, 'resource', '资源管理', 6, 'baseData/resourceMgmt/index', 'resource:view', 2, 1, 'resourceMgmt', 'vea-qrcode_viewfinder', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (9, 'operation', '操作管理', 6, 'baseData/operationMgmt/index', 'operation:view', 3, 1, 'operationMgmt', 'vea-hand_draw', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (10, 'permission', '权限管理', 6, 'baseData/permissionMgmt/index', 'permission:view', 4, 1, 'permissionMgmt', 'vea-lock', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (11, 'data', '数据管理', 1, 'common/parentIndex', '', 4, 1, 'dataMgmt', 'vea-slider_horizontal_below_rectangle', '', 1, 0, 1, 1);
INSERT INTO `sys_menu`(`id`, `code`, `name`, `parent_id`, `component_path`, `permission`, `sort`, `type`, `url`, `css`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (12, 'lov', '数据字典', 11, 'baseData/dataDictionary/index', 'lov:view', 2, 1, 'lovMgmt', 'vea-rectangle_stack_person_crop', '', 1, 0, 1, 1);

-- 资源
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 'unit', '组织机构管理', '', 1, 0, 1, 1);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (2, 'user', '用户管理', '', 1, 0, 1, 1);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (3, 'role', '角色管理', '', 1, 0, 1, 1);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (4, 'menu', '菜单', '', 1, 0, 1, 1);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (5, 'resource', '资源管理', '', 1, 0, 1, 1);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (6, 'operation', '操作管理', '', 1, 0, 1, 1);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (7, 'permission', '功能管理', '', 1, 0, 1, 1);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (8, 'lov', '值类型', '', 1, 0, 1, 1);
INSERT INTO `sys_resource`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (9, 'lovLine', '值数据', '', 1, 0, 1, 1);

-- 操作
INSERT INTO `sys_operation`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 'view', '查看', '', 1, 0, 1, 1);
INSERT INTO `sys_operation`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (2, 'add', '添加', '', 1, 0, 1, 1);
INSERT INTO `sys_operation`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (3, 'update', '更新', '', 1, 0, 1, 1);
INSERT INTO `sys_operation`(`id`, `code`, `name`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (4, 'delete', '删除', '', 1, 0, 1, 1);

-- 权限
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 1, 1, 'unit:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (2, 1, 2, 'unit:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (3, 1, 3, 'unit:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (4, 1, 4, 'unit:delete', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (5, 2, 1, 'user:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (6, 2, 2, 'user:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (7, 2, 3, 'user:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (8, 2, 4, 'user:delete', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (9, 3, 1, 'role:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (10, 3, 2, 'role:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (11, 3, 3, 'role:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (12, 3, 4, 'role:delete', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (13, 4, 1, 'menu:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (14, 4, 2, 'menu:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (15, 4, 3, 'menu:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (16, 4, 4, 'menu:delete', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (17, 5, 1, 'resource:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (18, 5, 2, 'resource:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (19, 5, 3, 'resource:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (20, 5, 4, 'resource:delete', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (21, 6, 1, 'operation:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (22, 6, 2, 'operation:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (23, 6, 3, 'operation:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (24, 6, 4, 'operation:delete', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (25, 7, 1, 'permission:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (26, 7, 2, 'permission:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (27, 7, 3, 'permission:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (28, 7, 4, 'permission:delete', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (29, 8, 1, 'lov:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (30, 8, 2, 'lov:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (31, 8, 3, 'lov:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (32, 8, 4, 'lov:delete', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (33, 9, 1, 'lovLine:view', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (34, 9, 2, 'lovLine:add', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (35, 9, 3, 'lovLine:update', '', 1, 0, 1, 1);
INSERT INTO `sys_permission`(`id`, `resource_id`, `operation_id`, `permission_code`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (36, 9, 4, 'lovLine:delete', '', 1, 0, 1, 1);

-- 角色权限关联
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (1, 1, 1, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (2, 1, 2, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (3, 1, 3, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (4, 1, 4, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (5, 1, 5, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (6, 1, 6, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (7, 1, 7, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (8, 1, 8, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (9, 1, 9, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (10, 1, 10, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (11, 1, 11, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (12, 1, 12, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (13, 1, 13, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (14, 1, 14, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (15, 1, 15, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (16, 1, 16, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (17, 1, 17, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (18, 1, 18, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (19, 1, 19, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (20, 1, 20, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (21, 1, 21, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (22, 1, 22, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (23, 1, 23, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (24, 1, 24, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (25, 1, 25, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (26, 1, 26, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (27, 1, 27, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (28, 1, 28, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (29, 1, 29, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (30, 1, 30, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (31, 1, 31, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (32, 1, 32, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (33, 1, 33, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (34, 1, 34, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (35, 1, 35, '', 1, 0, 1, 1);
INSERT INTO `sys_role_permission`(`id`, `role_id`, `permission_id`, `remark`, `status`, `deleted`, `create_by`, `update_by`) VALUES (36, 1, 36, '', 1, 0, 1, 1);
