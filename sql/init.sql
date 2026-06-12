-- ============================================
-- 健身房管理系统 - 数据库初始化脚本
-- 共7张表，精简设计
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

-- 9. 用户身体数据表
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

-- 10. 销售记录表
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

-- ============================================
-- 扩展功能：训练计划、饮食推荐、打卡、名言
-- ============================================

-- 用户表追加健身目标字段
ALTER TABLE `sys_user` ADD COLUMN `fitness_goal` VARCHAR(10) DEFAULT NULL COMMENT '健身目标: BUILD/LOSE';

-- 11. 训练计划模板表
DROP TABLE IF EXISTS `training_plan`;
CREATE TABLE `training_plan` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `goal_type` VARCHAR(10) NOT NULL COMMENT '目标: BUILD(增肌) / LOSE(减脂)',
    `day_of_week` INT NOT NULL COMMENT '星期几 1-7 (周一=1)',
    `focus` VARCHAR(100) NOT NULL COMMENT '训练重点',
    `exercises` TEXT NOT NULL COMMENT 'JSON格式训练动作',
    `warm_up` VARCHAR(255) DEFAULT NULL COMMENT '热身建议',
    `cardio` VARCHAR(255) DEFAULT NULL COMMENT '有氧建议（减脂用）',
    `notes` VARCHAR(255) DEFAULT NULL COMMENT '当日注意事项',
    UNIQUE KEY `uk_goal_day` (`goal_type`, `day_of_week`)
) COMMENT '训练计划模板表';

-- 12. 饮食推荐表
DROP TABLE IF EXISTS `diet_plan`;
CREATE TABLE `diet_plan` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `goal_type` VARCHAR(10) NOT NULL COMMENT '目标: BUILD / LOSE',
    `meal_type` VARCHAR(10) NOT NULL COMMENT '餐别: BREAKFAST/LUNCH/DINNER/SNACK',
    `content` TEXT NOT NULL COMMENT '推荐食物和数量',
    `calories_guide` VARCHAR(50) DEFAULT NULL COMMENT '热量参考',
    UNIQUE KEY `uk_goal_meal` (`goal_type`, `meal_type`)
) COMMENT '饮食推荐表';

-- 13. 用户打卡表
DROP TABLE IF EXISTS `user_checkin`;
CREATE TABLE `user_checkin` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `checkin_date` DATE NOT NULL COMMENT '打卡日期',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '打卡时间',
    UNIQUE KEY `uk_user_date` (`user_id`, `checkin_date`),
    INDEX `idx_date` (`checkin_date`)
) COMMENT '用户打卡记录';

-- 14. 励志名言表
DROP TABLE IF EXISTS `motivational_quote`;
CREATE TABLE `motivational_quote` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `content` VARCHAR(500) NOT NULL COMMENT '名言内容',
    `author` VARCHAR(100) DEFAULT NULL COMMENT '出处/作者',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) COMMENT '励志名言表';

-- ============================================
-- 训练计划种子数据
-- ============================================

-- 增肌计划（BUILD）
INSERT INTO `training_plan` (`goal_type`, `day_of_week`, `focus`, `exercises`, `warm_up`, `cardio`, `notes`) VALUES
('BUILD', 1, '胸部+三头肌', '[{"name":"杠铃卧推","sets":"4","reps":"8-12","rest":"90s","note":"核心收紧，背部贴凳"},{"name":"上斜哑铃卧推","sets":"4","reps":"10-12","rest":"90s","note":"上胸发力，顶峰收缩"},{"name":"哑铃飞鸟","sets":"3","reps":"12-15","rest":"60s","note":"控制离心，拉伸胸肌"},{"name":"绳索下压","sets":"3","reps":"12-15","rest":"60s","note":"肘部锁定，三头孤立发力"},{"name":"双杠臂屈伸","sets":"3","reps":"力竭","rest":"90s","note":"身体前倾刺激下胸"}]', '肩关节环绕2min + 弹力带推胸激活3min', NULL, '增肌黄金日，大重量复合动作为主'),
('BUILD', 2, '背部+二头肌', '[{"name":"引体向上","sets":"4","reps":"力竭","rest":"90s","note":"宽握，背部发力启动"},{"name":"杠铃划船","sets":"4","reps":"8-12","rest":"90s","note":"俯身45°，杠铃沿大腿上拉"},{"name":"高位下拉","sets":"3","reps":"10-12","rest":"60s","note":"沉肩，挤压背阔肌"},{"name":"单臂哑铃划船","sets":"3","reps":"10-12","rest":"60s","note":"每侧独立，拉伸与收缩并重"},{"name":"杠铃弯举","sets":"3","reps":"10-12","rest":"60s","note":"大臂固定，二头孤立发力"}]', '肩胛激活5min + 弹力带划船热身', NULL, '感受背阔肌的拉伸与收缩'),
('BUILD', 3, '肩部+腹肌', '[{"name":"坐姿哑铃推举","sets":"4","reps":"8-12","rest":"90s","note":"核心收紧，全程控制"},{"name":"哑铃侧平举","sets":"4","reps":"12-15","rest":"60s","note":"轻重量，控制离心，不要借力"},{"name":"面拉","sets":"3","reps":"15","rest":"60s","note":"绳索拉向面部，刺激后束"},{"name":"卷腹","sets":"3","reps":"20","rest":"45s","note":"上背部离地即可，腰部贴地"},{"name":"悬垂举腿","sets":"3","reps":"15","rest":"60s","note":"控制摆动，腹肌发力"}]', '肩袖激活5min + 弹力带内外旋', NULL, '肩部是门面，侧平举质量大于重量'),
('BUILD', 4, '腿部（股四头肌主导）', '[{"name":"杠铃深蹲","sets":"4","reps":"6-10","rest":"120s","note":"核心收紧，膝盖不内扣，大腿平行地面"},{"name":"腿举","sets":"4","reps":"10-12","rest":"90s","note":"控制深度，不要锁死膝盖"},{"name":"腿屈伸","sets":"3","reps":"12-15","rest":"60s","note":"顶峰收缩1秒，孤立股四头肌"},{"name":"坐姿提踵","sets":"4","reps":"15-20","rest":"60s","note":"全程拉伸与收缩小腿"}]', '髋关节环绕3min + 空蹲激活2min', NULL, '深蹲是增肌之王，动作质量第一'),
('BUILD', 5, '全身复合+手臂强化', '[{"name":"罗马尼亚硬拉","sets":"4","reps":"8-10","rest":"90s","note":"屈髋主导，背平直，腘绳肌拉伸"},{"name":"上斜哑铃推举","sets":"3","reps":"10-12","rest":"60s","note":"复合推，肩胸协同"},{"name":"锤式弯举","sets":"3","reps":"10-12","rest":"60s","note":"肱肌+二头，握力训练"},{"name":"绳索下压","sets":"3","reps":"12-15","rest":"60s","note":"三头肌外侧头"},{"name":"臀推","sets":"4","reps":"10-12","rest":"90s","note":"顶峰夹紧臀部1-2秒"}]', '全身动态拉伸5min', NULL, '周五补齐本周薄弱部位'),
('BUILD', 6, '主动恢复日', '[{"name":"泡沫轴放松","sets":"1","reps":"全身","rest":"-","note":"每个部位滚压30-60秒"},{"name":"静态拉伸","sets":"1","reps":"全身","rest":"-","note":"每个拉伸保持20-30秒"}]', NULL, NULL, '恢复是增肌的一部分，不要忽视'),
('BUILD', 7, '休息日', '[{"name":"完全休息","sets":"-","reps":"-","rest":"-","note":"保证充足睡眠，蛋白质摄入不要断"}]', NULL, NULL, '肌肉在休息时生长，睡眠7-8小时');

-- 减脂计划（LOSE）
INSERT INTO `training_plan` (`goal_type`, `day_of_week`, `focus`, `exercises`, `warm_up`, `cardio`, `notes`) VALUES
('LOSE', 1, '下肢力量+有氧', '[{"name":"杠铃深蹲","sets":"3-4","reps":"10-12","rest":"60s","note":"中等重量，动作标准优先"},{"name":"罗马尼亚硬拉","sets":"3-4","reps":"10-12","rest":"60s","note":"控制离心，感受后链拉伸"},{"name":"箭步蹲","sets":"3","reps":"12-15/侧","rest":"60s","note":"核心收紧，膝盖稳定"},{"name":"臀桥","sets":"3","reps":"15","rest":"45s","note":"顶峰夹紧停留1秒"}]', '动态拉伸+高抬腿 5min', '跑步机坡度走20-30min（坡度10-12，速度4.5-5.0）', '先力量后有氧——糖原消耗后直接燃脂'),
('LOSE', 2, '上肢推+有氧', '[{"name":"杠铃卧推","sets":"3-4","reps":"10-12","rest":"60s","note":"中等重量，控制节奏"},{"name":"哑铃肩推","sets":"3-4","reps":"10-12","rest":"60s","note":"核心收紧，全程稳定"},{"name":"上斜哑铃飞鸟","sets":"3","reps":"12-15","rest":"45s","note":"轻重量，感受拉伸"},{"name":"绳索下压","sets":"3","reps":"12-15","rest":"45s","note":"三头肌燃烧感"}]', '肩关节环绕 5min', '慢跑/动感单车 20-30min', '力量训练保持肌肉量，有氧加速燃脂'),
('LOSE', 3, '纯有氧燃脂日', '[{"name":"跑步机坡度走","sets":"1","reps":"40-50min","rest":"-","note":"心率保持130-140，中等强度稳态"},{"name":"或HIIT跳绳","sets":"8","reps":"30s快+15s慢","rest":"循环","note":"高强度间歇，后燃效应强"}]', '5min 动态拉伸', NULL, '纯有氧日，给肌肉恢复时间，专注脂肪燃烧'),
('LOSE', 4, '背部+二头+有氧', '[{"name":"高位下拉","sets":"3-4","reps":"10-12","rest":"60s","note":"沉肩，背阔肌发力"},{"name":"俯身哑铃划船","sets":"3-4","reps":"10-12","rest":"60s","note":"每侧独立，拉伸充分"},{"name":"坐姿绳索划船","sets":"3","reps":"12-15","rest":"45s","note":"挤压背部中央"},{"name":"哑铃弯举","sets":"3","reps":"12-15","rest":"45s","note":"控制离心3秒"}]', '肩胛激活 5min', '椭圆机 20min', '背部大肌群训练消耗可观热量'),
('LOSE', 5, 'HIIT全身燃脂', '[{"name":"波比跳","sets":"4","reps":"30s","rest":"15s","note":"全力输出，不保留"},{"name":"开合跳","sets":"4","reps":"30s","rest":"15s","note":"节奏稳定"},{"name":"深蹲跳","sets":"4","reps":"30s","rest":"15s","note":"落地轻盈，保护膝盖"},{"name":"登山跑","sets":"4","reps":"30s","rest":"15s","note":"核心收紧，快速交替"},{"name":"高抬腿","sets":"4","reps":"30s","rest":"15s","note":"大腿抬至水平"}]', '全身动态拉伸 5min', NULL, 'HIIT总时长20-25min，后燃效应持续24h+'),
('LOSE', 6, '可选轻度运动', '[{"name":"户外慢跑/游泳/骑行","sets":"1","reps":"40-60min","rest":"-","note":"低强度稳态有氧，享受运动"},{"name":"或瑜伽/拉伸课","sets":"1","reps":"45-60min","rest":"-","note":"提升柔韧性，促进恢复"}]', NULL, NULL, '根据身体状态灵活选择，不要勉强'),
('LOSE', 7, '休息日', '[{"name":"完全休息","sets":"-","reps":"-","rest":"-","note":"保证睡眠7-8小时，多喝水帮助代谢"}]', NULL, NULL, '休息是为了更好的燃烧！睡眠不足会阻碍减脂');

-- ============================================
-- 饮食推荐种子数据
-- ============================================

-- 增肌饮食（BUILD）
INSERT INTO `diet_plan` (`goal_type`, `meal_type`, `content`, `calories_guide`) VALUES
('BUILD', 'BREAKFAST', '燕麦片80g + 全脂牛奶250ml、水煮蛋×2、香蕉×1根、核桃一小把（约15g）', '~700kcal'),
('BUILD', 'LUNCH', '糙米饭200g（熟重）+ 鸡胸肉200g + 西兰花/菠菜一大份 + 橄榄油10ml', '~800kcal'),
('BUILD', 'SNACK', '全麦面包×2片 + 低脂花生酱 + 乳清蛋白粉1勺 + 香蕉×1（训练后30分钟内饮用最佳）', '~400kcal'),
('BUILD', 'DINNER', '红薯/土豆200g（熟重）+ 三文鱼/瘦牛肉150g + 芦笋/四季豆一大份 + 少量橄榄油', '~700kcal'),
('BUILD', 'BEDTIME', '希腊酸奶150g + 杏仁一小把（约10g）——睡前加餐，持续供能防夜间肌肉分解', '~200kcal');

-- 减脂饮食（LOSE）
INSERT INTO `diet_plan` (`goal_type`, `meal_type`, `content`, `calories_guide`) VALUES
('LOSE', 'BREAKFAST', '全麦面包1片 + 蒸红薯50g + 水煮蛋×1 + 无糖豆浆200ml + 凉拌菠菜1小碗（少油）', '~350kcal'),
('LOSE', 'LUNCH', '杂粮饭100g（糙米+藜麦+燕麦）+ 鸡胸肉150g（少油煎/清蒸）+ 西兰花/彩椒200g + 番茄豆腐汤1碗', '~450kcal'),
('LOSE', 'SNACK', '原味坚果10g（杏仁6-8粒）/ 或无糖希腊酸奶100g + 蓝莓50g（下午饥饿时食用）', '≤100kcal'),
('LOSE', 'DINNER', '蒸南瓜80g + 清蒸鳕鱼120g + 凉拌木耳黄瓜150g + 蒜蓉西兰花大量（19:00前吃完）', '~350kcal');

-- ============================================
-- 励志名言种子数据（30条）
-- ============================================
INSERT INTO `motivational_quote` (`content`, `author`) VALUES
('没有付出，就没有收获。', '佚名'),
('今天你感受到的疼痛，会变成明天你感受到的力量。', '佚名'),
('力量不是来自胜利，你的挣扎才会真正增长你的力量。', '阿诺德·施瓦辛格'),
('文明其精神，野蛮其体魄。', '毛泽东'),
('汗水就是脂肪的泪水。', '佚名'),
('放弃可以找到一万个理由，坚持只需一个信念！', '佚名'),
('如果杠铃杆没弯，你就是在装样子。', '健身房谚语'),
('成功的路上并不拥挤，因为坚持的人不多。', '佚名'),
('时间会证明一切，汗水从不会骗人。', '佚名'),
('今天去做别人不愿做的事，明天就可以做别人办不到的事。', '佚名'),
('动力让你开始，但习惯才让你一直继续。', '佚名'),
('好身材就像婚姻，你不可能欺骗它还指望一切都好。', '佚名'),
('世界上没有比结实的肌肉和新鲜的皮肤更美丽的衣裳。', '马雅可夫斯基'),
('如果你想强壮，跑步吧！如果你想健美，跑步吧！如果你想聪明，跑步吧！', '古希腊谚语'),
('生命在于运动。', '伏尔泰'),
('健全之精神，必寓于健全之肉体。', '佚名'),
('宝剑锋从磨砺出，梅花香自苦寒来。', '佚名'),
('现在流的口水，将成为明天的眼泪；现在流的汗水，将换来明天的微笑。', '佚名'),
('当你想放弃的时候，记住你为什么开始。', '佚名'),
('别只是渴望好身材，去为它奋斗。', '佚名'),
('从来不会变得更轻松，只是你变得更强了。', '佚名'),
('借口无用，结果无价。', '佚名'),
('你的头脑相信什么，你的身体就得到什么。', '佚名'),
('强壮的人比弱小的人更难被杀死，总体上也更有用。', 'Mark Rippetoe'),
('钢铁永远不会对你撒谎。', 'Henry Rollins'),
('太轻了！轻得跟花生一样！', '罗尼·库尔曼'),
('健身没有什么捷径可走。', 'Mark Rippetoe'),
('自律给我自由。', '佚名'),
('每一次想放弃的瞬间，都是你突破极限的机会。', '佚名'),
('昨天的你，是今天的你的底线。', '佚名');
