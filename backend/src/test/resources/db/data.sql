-- =====================================================
-- 初始化数据（角色、权限、测试用户）
-- =====================================================

-- 插入权限数据（包含用户管理、角色管理、活动管理）
INSERT INTO `permissions` (`name`, `description`, `resource`, `action`) VALUES
-- 用户管理
('user:create', '创建用户', 'user', 'create'),
('user:read', '查看用户', 'user', 'read'),
('user:update', '更新用户', 'user', 'update'),
('user:delete', '删除用户', 'user', 'delete'),
-- 角色管理
('role:create', '创建角色', 'role', 'create'),
('role:read', '查看角色', 'role', 'read'),
('role:update', '更新角色', 'role', 'update'),
('role:delete', '删除角色', 'role', 'delete'),
-- 权限管理
('permission:create', '创建权限', 'permission', 'create'),
('permission:read', '查看权限', 'permission', 'read'),
('permission:update', '更新权限', 'permission', 'update'),
('permission:delete', '删除权限', 'permission', 'delete'),
-- 活动管理
('activity:create', '创建活动', 'activity', 'create'),
('activity:read', '查看活动', 'activity', 'read'),
('activity:update', '更新活动', 'activity', 'update'),
('activity:delete', '删除活动', 'activity', 'delete'),
-- 系统配置
('system:config', '系统配置', 'system', 'config');

-- 插入角色
INSERT INTO `roles` (`name`, `description`) VALUES
('ROLE_ADMIN', '系统管理员，拥有所有权限'),
('ROLE_USER', '普通用户，拥有基本活动权限'),
('ROLE_GUEST', '访客，只有查看权限');

-- 分配权限给角色

-- 管理员：所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT (SELECT id FROM `roles` WHERE `name` = 'ROLE_ADMIN'), id FROM `permissions`;

-- 普通用户：活动读写、用户只读等（可按需调整）
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT (SELECT id FROM `roles` WHERE `name` = 'ROLE_USER'), id
FROM `permissions`
WHERE `name` IN ('user:read', 'activity:create', 'activity:read', 'activity:update', 'activity:delete');

-- 访客：只有查看权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT (SELECT id FROM `roles` WHERE `name` = 'ROLE_GUEST'), id
FROM `permissions`
WHERE `action` = 'read';

-- 插入测试用户（密码都是 password123，BCrypt 加密）
INSERT INTO `users` (`student_id`, `username`, `password`, `email`, `nickname`, `college`, `grade`, `credit_score`, `enabled`) VALUES
('20240001', 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye1j50KGjM5q6jZW.UuCtGFP.VfVCWHnK', 'admin@campus.edu', '管理员', '计算机学院', 2024, 100, TRUE),
('20240002', 'user1', '$2a$10$N9qo8uLOickgx2ZMRZoMye1j50KGjM5q6jZW.UuCtGFP.VfVCWHnK', 'user1@campus.edu', '张三', '计算机学院', 2024, 100, TRUE),
('20240003', 'user2', '$2a$10$N9qo8uLOickgx2ZMRZoMye1j50KGjM5q6jZW.UuCtGFP.VfVCWHnK', 'user2@campus.edu', '李四', '外语学院', 2023, 95, TRUE),
('20240004', 'guest', '$2a$10$N9qo8uLOickgx2ZMRZoMye1j50KGjM5q6jZW.UuCtGFP.VfVCWHnK', 'guest@campus.edu', '访客', NULL, NULL, 100, TRUE),
('20240005', 'disabled', '$2a$10$N9qo8uLOickgx2ZMRZoMye1j50KGjM5q6jZW.UuCtGFP.VfVCWHnK', 'disabled@campus.edu', '禁用账号', NULL, NULL, 100, FALSE);

-- 分配角色给用户
INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `users` u, `roles` r
WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `users` u, `roles` r
WHERE u.username = 'user1' AND r.name = 'ROLE_USER';

INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `users` u, `roles` r
WHERE u.username = 'user2' AND r.name = 'ROLE_USER';

INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `users` u, `roles` r
WHERE u.username = 'guest' AND r.name = 'ROLE_GUEST';

-- disabled 用户没有分配角色，但也可以分配（可根据需要）
INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id, r.id FROM `users` u, `roles` r
WHERE u.username = 'disabled' AND r.name = 'ROLE_GUEST';

SET FOREIGN_KEY_CHECKS = 1;