-- Active: 1773751059041@@127.0.0.1@3306@campus_buddy_matching_system
-- =====================================================
-- 校园搭子匹配系统 2.0 - 完整数据库表结构
-- 整合 RBAC 权限模型 + 业务表
-- MySQL 8.0
-- =====================================================
-- 设置字符集
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- =====================================================
-- 1. 用户表（整合 RBAC 用户）
-- =====================================================
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `student_id` VARCHAR(20) NOT NULL COMMENT '学号',
    `username` VARCHAR(50) NOT NULL COMMENT '登录用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `college` VARCHAR(100) DEFAULT NULL COMMENT '学院',
    `grade` INT DEFAULT NULL COMMENT '入学年份',
    `tags` JSON DEFAULT NULL COMMENT '个人标签（如["学霸","熬夜党"]）',
    `credit_score` INT NOT NULL DEFAULT 100 COMMENT '信用分',
    `enabled` BOOLEAN NOT NULL DEFAULT TRUE COMMENT '账号是否启用',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_student_id` (`student_id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`),
    KEY `idx_credit_score` (`credit_score`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表（整合RBAC）';
-- =====================================================
-- 2. 角色表
-- =====================================================
DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '角色ID',
    `name` VARCHAR(50) NOT NULL COMMENT '角色名称（如 ROLE_ADMIN, ROLE_USER）',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '角色描述',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色表';
-- =====================================================
-- 3. 权限表
-- =====================================================
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '权限ID',
    `name` VARCHAR(100) NOT NULL COMMENT '权限名称（如 user:read, activity:create）',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '权限描述',
    `resource` VARCHAR(50) NOT NULL COMMENT '资源类型（如 user, role, activity）',
    `action` VARCHAR(50) NOT NULL COMMENT '操作类型（如 create, read, update, delete）',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`),
    UNIQUE KEY `uk_resource_action` (`resource`, `action`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '权限表';
-- =====================================================
-- 4. 用户-角色关联表（多对多）
-- =====================================================
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`user_id`, `role_id`),
    CONSTRAINT `fk_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户-角色关联表';
-- =====================================================
-- 5. 角色-权限关联表（多对多）
-- =====================================================
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    PRIMARY KEY (`role_id`, `permission_id`),
    CONSTRAINT `fk_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '角色-权限关联表';
-- =====================================================
-- 6. 活动表（搭子活动）
-- =====================================================
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '活动ID',
    `creator_id` BIGINT NOT NULL COMMENT '发起人ID（关联users.id）',
    `category` VARCHAR(20) NOT NULL COMMENT '类型：study/sports/food/game/other',
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `detail` TEXT COMMENT '详细描述',
    `location` VARCHAR(100) DEFAULT NULL COMMENT '地点',
    `appointment_time` DATETIME DEFAULT NULL COMMENT '约定时间',
    `max_members` INT NOT NULL DEFAULT 2 COMMENT '最大人数',
    `current_members` INT NOT NULL DEFAULT 1 COMMENT '当前人数',
    `status` VARCHAR(20) NOT NULL DEFAULT 'open' COMMENT '状态：open/full/closed',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_creator_id` (`creator_id`),
    KEY `idx_category` (`category`),
    KEY `idx_status` (`status`),
    KEY `idx_appointment_time` (`appointment_time`),
    CONSTRAINT `fk_activity_creator` FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '搭子活动表';
-- =====================================================
-- 7. 队伍成员表（活动参与人）
-- =====================================================
DROP TABLE IF EXISTS `team_member`;
CREATE TABLE `team_member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `activity_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `role` VARCHAR(20) NOT NULL DEFAULT 'member' COMMENT '角色：leader/member',
    `joined_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_activity_user` (`activity_id`, `user_id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_team_member_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_team_member_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '队伍成员表';
-- =====================================================
-- 8. 申请表（用户申请加入活动）
-- =====================================================
DROP TABLE IF EXISTS `application`;
CREATE TABLE `application` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `activity_id` BIGINT NOT NULL,
    `applicant_id` BIGINT NOT NULL,
    `status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '状态：pending/approved/rejected',
    `apply_msg` VARCHAR(255) DEFAULT NULL COMMENT '申请留言',
    `processed_at` TIMESTAMP NULL DEFAULT NULL COMMENT '处理时间',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_activity_id` (`activity_id`),
    KEY `idx_applicant_id` (`applicant_id`),
    KEY `idx_status` (`status`),
    CONSTRAINT `fk_application_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_application_user` FOREIGN KEY (`applicant_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '申请表';
-- =====================================================
-- 9. 评价表
-- =====================================================
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `from_user_id` BIGINT NOT NULL COMMENT '评价人ID',
    `to_user_id` BIGINT NOT NULL COMMENT '被评价人ID',
    `activity_id` BIGINT NOT NULL COMMENT '关联的活动ID',
    `rating` TINYINT NOT NULL COMMENT '评分1-5',
    `comment` VARCHAR(255) DEFAULT NULL COMMENT '评语',
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_review_unique` (`from_user_id`, `to_user_id`, `activity_id`),
    KEY `idx_to_user_id` (`to_user_id`),
    CONSTRAINT `fk_review_from_user` FOREIGN KEY (`from_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_review_to_user` FOREIGN KEY (`to_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_review_activity` FOREIGN KEY (`activity_id`) REFERENCES `activity` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评价表';