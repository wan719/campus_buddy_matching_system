-- 校园搭子匹配系统 2.0 数据库建表语句
-- MySQL 8.0

-- 1. 用户表
CREATE TABLE `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `student_id` VARCHAR(20) UNIQUE NOT NULL COMMENT '学号',
    `password_hash` VARCHAR(60) NOT NULL COMMENT 'BCrypt加密密码',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `avatar_url` VARCHAR(255) COMMENT '头像URL',
    `college` VARCHAR(100) COMMENT '学院',
    `grade` INT COMMENT '入学年份',
    `tags` JSON COMMENT '个人标签["学霸","熬夜党"]',
    `credit_score` INT DEFAULT 100 COMMENT '信用分',
    `is_active` BOOLEAN DEFAULT TRUE COMMENT '账号状态',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_student_id (`student_id`),
    INDEX idx_credit_score (`credit_score`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 活动表
CREATE TABLE `activity` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `creator_id` BIGINT NOT NULL COMMENT '发起人ID',
    `category` VARCHAR(20) NOT NULL COMMENT 'study/sports/food/game/other',
    `title` VARCHAR(100) NOT NULL COMMENT '活动标题',
    `detail` TEXT COMMENT '详细描述',
    `location` VARCHAR(100) COMMENT '地点',
    `appointment_time` DATETIME COMMENT '约定时间',
    `max_members` INT DEFAULT 2 COMMENT '最大人数',
    `current_members` INT DEFAULT 1 COMMENT '当前人数',
    `status` VARCHAR(20) DEFAULT 'open' COMMENT 'open/full/closed',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`creator_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX idx_category (`category`),
    INDEX idx_status (`status`),
    INDEX idx_appointment_time (`appointment_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动表';

-- 3. 队伍成员表
CREATE TABLE `team_member` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `activity_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    `role` VARCHAR(20) DEFAULT 'member' COMMENT 'leader/member',
    `joined_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_activity_user (`activity_id`, `user_id`),
    FOREIGN KEY (`activity_id`) REFERENCES `activity`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='队伍成员表';

-- 4. 申请表
CREATE TABLE `application` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `activity_id` BIGINT NOT NULL,
    `applicant_id` BIGINT NOT NULL,
    `status` VARCHAR(20) DEFAULT 'pending' COMMENT 'pending/approved/rejected',
    `apply_msg` VARCHAR(255) COMMENT '申请留言',
    `processed_at` TIMESTAMP NULL COMMENT '处理时间',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`activity_id`) REFERENCES `activity`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`applicant_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    INDEX idx_status (`status`),
    INDEX idx_activity_id (`activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申请表';

-- 5. 评价表
CREATE TABLE `review` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `from_user_id` BIGINT NOT NULL COMMENT '评价人',
    `to_user_id` BIGINT NOT NULL COMMENT '被评价人',
    `activity_id` BIGINT NOT NULL,
    `rating` TINYINT COMMENT '1-5星',
    `comment` VARCHAR(255) COMMENT '评语',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`from_user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`to_user_id`) REFERENCES `user`(`id`),
    FOREIGN KEY (`activity_id`) REFERENCES `activity`(`id`),
    UNIQUE KEY uk_review_unique (`from_user_id`, `to_user_id`, `activity_id`),
    INDEX idx_to_user_id (`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';
