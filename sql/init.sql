-- ============================================
-- 健身房管理系统 - 数据库初始化脚本
-- 共11张表
-- ============================================

CREATE DATABASE IF NOT EXISTS gym_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gym_db;

-- 1. 用户表（含会员和管理员）
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（BCrypt加密）',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `photo` VARCHAR(255) DEFAULT NULL COMMENT '照片路径',
    `vip_card_no` VARCHAR(32) DEFAULT NULL UNIQUE COMMENT 'VIP卡号（唯一）',
    `role` VARCHAR(10) NOT NULL DEFAULT 'USER' COMMENT '角色: USER/ADMIN',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_vip_card` (`vip_card_no`),
    INDEX `idx_role` (`role`)
) COMMENT '用户表';

-- 2. 健身卡类型表
DROP TABLE IF EXISTS `membership_card`;
CREATE TABLE `membership_card` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '卡名称（月卡/季卡/年卡/次卡）',
    `type` VARCHAR(20) NOT NULL COMMENT '类型: MONTH/SEASON/YEAR/TIME',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `duration_days` INT NOT NULL DEFAULT 30 COMMENT '有效天数',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '健身卡类型表';

-- 3. 用户购卡记录
DROP TABLE IF EXISTS `user_card`;
CREATE TABLE `user_card` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `card_id` BIGINT NOT NULL COMMENT '卡类型ID',
    `card_name` VARCHAR(50) NOT NULL COMMENT '卡名称（冗余）',
    `buy_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '购买时间',
    `expire_time` DATETIME NOT NULL COMMENT '到期时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1有效 0已过期',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`)
) COMMENT '用户购卡记录';

-- 4. 教练表
DROP TABLE IF EXISTS `coach`;
CREATE TABLE `coach` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(30) NOT NULL COMMENT '姓名',
    `photo` VARCHAR(255) DEFAULT NULL COMMENT '照片URL',
    `specialty` VARCHAR(100) DEFAULT NULL COMMENT '擅长领域',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '电话',
    `intro` TEXT DEFAULT NULL COMMENT '简介',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1在职 0离职',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '教练表';

-- 5. 商品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(50) NOT NULL COMMENT '商品名称',
    `price` DECIMAL(10,2) NOT NULL COMMENT '价格',
    `stock` INT DEFAULT 0 COMMENT '库存',
    `image` VARCHAR(255) DEFAULT NULL COMMENT '图片URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1在售 0下架',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '商品表';

-- 6. 新闻通知表
DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(100) NOT NULL COMMENT '标题',
    `content` TEXT NOT NULL COMMENT '内容',
    `admin_id` BIGINT DEFAULT NULL COMMENT '发布人ID',
    `publish_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '新闻通知表';

-- 7. 健身房每日客流
DROP TABLE IF EXISTS `gym_daily`;
CREATE TABLE `gym_daily` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `record_date` DATE NOT NULL UNIQUE COMMENT '日期',
    `current_count` INT DEFAULT 0 COMMENT '当前在场人数',
    `total_in` INT DEFAULT 0 COMMENT '当日入场总数',
    `peak_count` INT DEFAULT 0 COMMENT '当日峰值人数',
    `total_revenue` DECIMAL(10,2) DEFAULT 0.00 COMMENT '当日销售额',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX `idx_date` (`record_date`)
) COMMENT '每日客流记录';

-- 8. 每日分时段峰值
DROP TABLE IF EXISTS `gym_timeslot`;
CREATE TABLE `gym_timeslot` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `record_date` DATE NOT NULL COMMENT '日期',
    `time_slot` VARCHAR(15) NOT NULL COMMENT '时间段 如 8-10',
    `peak_count` INT DEFAULT 0 COMMENT '该时段峰值人数',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE INDEX `uk_date_slot` (`record_date`, `time_slot`)
) COMMENT '每日分时段峰值';

-- 9. 入场记录表
DROP TABLE IF EXISTS `entry_log`;
CREATE TABLE `entry_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `card_id` BIGINT DEFAULT NULL COMMENT '使用的会员卡ID',
    `enter_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '入场时间',
    `exit_time` DATETIME DEFAULT NULL COMMENT '离场时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态: 1在场 0已离场',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_status` (`status`)
) COMMENT '入场记录表';

-- 10. 用户身体数据表
DROP TABLE IF EXISTS `user_body_info`;
CREATE TABLE `user_body_info` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `height` DECIMAL(5,1) NOT NULL COMMENT '身高(cm)',
    `weight` DECIMAL(5,1) NOT NULL COMMENT '体重(kg)',
    `gender` TINYINT NOT NULL DEFAULT 1 COMMENT '性别: 1男 0女',
    `age` INT NOT NULL DEFAULT 25 COMMENT '年龄',
    `bmi` DECIMAL(5,1) NOT NULL COMMENT 'BMI值（自动计算）',
    `body_fat` DECIMAL(5,1) NOT NULL COMMENT '体脂率% (Deurenberg公式)',
    `record_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_record_time` (`user_id`, `record_time`)
) COMMENT '用户身体数据记录';

-- 11. 销售记录表
DROP TABLE IF EXISTS `sales_record`;
CREATE TABLE `sales_record` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT DEFAULT NULL COMMENT '购买用户ID',
    `item_type` VARCHAR(20) NOT NULL COMMENT '类型: CARD/PRODUCT',
    `item_name` VARCHAR(100) NOT NULL COMMENT '商品/卡名称',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_create_time` (`create_time`)
) COMMENT '销售记录表';

-- ============================================
-- 初始数据
-- ============================================

-- 管理员账号: admin / admin123
INSERT INTO `sys_user` (`username`, `password`, `role`) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ADMIN');

-- 测试会员: test / test123 (无VIP卡号，购卡后自动生成)
INSERT INTO `sys_user` (`username`, `password`, `phone`, `vip_card_no`, `role`) VALUES
('test', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '13800138000', NULL, 'USER');

-- 健身卡类型
INSERT INTO `membership_card` (`name`, `type`, `price`, `duration_days`, `description`) VALUES
('月卡', 'MONTH', 299.00, 30, '30天无限次入场'),
('季卡', 'SEASON', 799.00, 90, '90天无限次入场'),
('年卡', 'YEAR', 2399.00, 365, '365天无限次入场，赠送2次私教课'),
('10日体验卡', 'TIME', 199.00, 10, '10天无限次入场');

-- 教练
INSERT INTO `coach` (`name`, `photo`, `specialty`, `phone`, `intro`) VALUES
('张教练', NULL, '增肌训练', '13900001111', '国家认证健身教练，5年教学经验，专注增肌和力量训练。'),
('李教练', NULL, '减脂塑形', '13900002222', 'AFAA认证教练，擅长功能性训练和减脂课程。'),
('王教练', NULL, '瑜伽普拉提', '13900003333', 'RYT500认证瑜伽导师，教授哈他瑜伽和流瑜伽。');

-- 商品
INSERT INTO `product` (`name`, `price`, `stock`) VALUES
('矿泉水', 3.00, 200),
('蛋白粉(500g)', 199.00, 50),
('能量饮料', 8.00, 100),
('运动毛巾', 29.00, 80);

-- 今日客流初始
INSERT INTO `gym_daily` (`record_date`, `current_count`, `total_in`, `peak_count`, `total_revenue`) VALUES
(CURDATE(), 0, 0, 0, 0.00);

-- 新闻
INSERT INTO `news` (`title`, `content`, `admin_id`) VALUES
('欢迎来到PowerFit健身房！', '我们提供最专业的健身设备和教练团队，期待您的到来！', 1),
('本月团课安排已更新', '新增普拉提和搏击操课程，详情请咨询前台或查看课表。', 1);
