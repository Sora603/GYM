-- 场馆容量字段
ALTER TABLE `gym_daily`
ADD COLUMN `gym_capacity` INT DEFAULT 80 COMMENT '场馆器械容量';

-- 更新今天记录设置默认容量
UPDATE `gym_daily` SET `gym_capacity` = 80 WHERE `record_date` = CURDATE();
