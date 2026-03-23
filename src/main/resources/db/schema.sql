-- 校园搭子匹配系统数据库表结构
-- 数据库: campus_buddy_matching_system
-- 字符集: utf8mb4
-- ============================================
-- 1. 用户表 (users)
-- ============================================
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '加密密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(500) COMMENT '头像URL',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    gender TINYINT DEFAULT 0 COMMENT '性别: 0-保密, 1-男, 2-女',
    age INT COMMENT '年龄',
    school VARCHAR(100) COMMENT '学校名称',
    major VARCHAR(100) COMMENT '专业',
    grade VARCHAR(20) COMMENT '年级',
    bio TEXT COMMENT '个人简介',
    interests VARCHAR(500) COMMENT '兴趣爱好(逗号分隔)',
    status TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-正常',
    last_login_time DATETIME COMMENT '最后登录时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_school (school),
    INDEX idx_status (status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表';
-- ============================================
-- 2. 搭子需求表 (buddy_requests)
-- ============================================
CREATE TABLE IF NOT EXISTS buddy_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '需求ID',
    user_id BIGINT NOT NULL COMMENT '发布用户ID',
    title VARCHAR(100) NOT NULL COMMENT '需求标题',
    content TEXT NOT NULL COMMENT '需求详情',
    category VARCHAR(50) NOT NULL COMMENT '搭子类型: 学习/运动/游戏/吃饭/旅行/其他',
    tags VARCHAR(200) COMMENT '标签(逗号分隔)',
    location VARCHAR(200) COMMENT '地点/范围',
    expected_time VARCHAR(100) COMMENT '期望时间',
    required_gender TINYINT DEFAULT 0 COMMENT '性别要求: 0-不限, 1-男, 2-女',
    required_age_min INT COMMENT '最小年龄要求',
    required_age_max INT COMMENT '最大年龄要求',
    max_buddies INT DEFAULT 1 COMMENT '需要搭子数量',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-招募中, 1-已匹配, 2-已结束, 3-已取消',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    apply_count INT DEFAULT 0 COMMENT '申请次数',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_category (category),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '搭子需求表';
-- ============================================
-- 3. 匹配申请表 (match_applications)
-- ============================================
CREATE TABLE IF NOT EXISTS match_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '申请ID',
    request_id BIGINT NOT NULL COMMENT '需求ID',
    applicant_id BIGINT NOT NULL COMMENT '申请人ID',
    message TEXT COMMENT '申请留言',
    status TINYINT DEFAULT 0 COMMENT '状态: 0-待处理, 1-已同意, 2-已拒绝, 3-已取消',
    response_message TEXT COMMENT '回复留言',
    handled_at DATETIME COMMENT '处理时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (request_id) REFERENCES buddy_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (applicant_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_request_applicant (request_id, applicant_id),
    INDEX idx_request_id (request_id),
    INDEX idx_applicant_id (applicant_id),
    INDEX idx_status (status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '匹配申请表';
-- ============================================
-- 4. 匹配记录表 (match_records)
-- ============================================
CREATE TABLE IF NOT EXISTS match_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    request_id BIGINT NOT NULL COMMENT '需求ID',
    requester_id BIGINT NOT NULL COMMENT '需求发布者ID',
    buddy_id BIGINT NOT NULL COMMENT '搭子用户ID',
    status TINYINT DEFAULT 1 COMMENT '状态: 1-匹配成功, 2-已结束, 3-已取消',
    remark TEXT COMMENT '备注',
    ended_at DATETIME COMMENT '结束时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (request_id) REFERENCES buddy_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (buddy_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_request_id (request_id),
    INDEX idx_requester_id (requester_id),
    INDEX idx_buddy_id (buddy_id),
    INDEX idx_status (status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '匹配记录表';
-- ============================================
-- 5. 消息通知表 (notifications)
-- ============================================
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '接收用户ID',
    type VARCHAR(50) NOT NULL COMMENT '通知类型: MATCH_APPLY-匹配申请, MATCH_AGREE-匹配同意, MATCH_REJECT-匹配拒绝, SYSTEM-系统通知',
    title VARCHAR(100) NOT NULL COMMENT '通知标题',
    content TEXT COMMENT '通知内容',
    related_id BIGINT COMMENT '关联ID(如申请ID、匹配ID等)',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读: 0-未读, 1-已读',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_is_read (is_read),
    INDEX idx_created_at (created_at)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '消息通知表';
-- ============================================
-- 6. 用户关注表 (user_follows)
-- ============================================
CREATE TABLE IF NOT EXISTS user_follows (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '关注ID',
    follower_id BIGINT NOT NULL COMMENT '关注者ID',
    following_id BIGINT NOT NULL COMMENT '被关注者ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    FOREIGN KEY (follower_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (following_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_follow (follower_id, following_id),
    INDEX idx_follower_id (follower_id),
    INDEX idx_following_id (following_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户关注表';
-- ============================================
-- 7. 用户评分表 (user_ratings)
-- ============================================
CREATE TABLE IF NOT EXISTS user_ratings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评分ID',
    match_id BIGINT NOT NULL COMMENT '匹配记录ID',
    rater_id BIGINT NOT NULL COMMENT '评分者ID',
    rated_id BIGINT NOT NULL COMMENT '被评分者ID',
    rating INT NOT NULL COMMENT '评分: 1-5星',
    comment TEXT COMMENT '评价内容',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    FOREIGN KEY (match_id) REFERENCES match_records(id) ON DELETE CASCADE,
    FOREIGN KEY (rater_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (rated_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY uk_match_rater (match_id, rater_id),
    INDEX idx_match_id (match_id),
    INDEX idx_rated_id (rated_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户评分表';