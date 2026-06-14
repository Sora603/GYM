-- =====================================================
-- 修复训练计划 day_type 字段
-- 如果 day_type 列已存在，第一条 ALTER 会报错(可忽略)
-- =====================================================

-- 1. 添加 day_type 字段（如已存在请跳过此句）
ALTER TABLE `training_plan`
ADD COLUMN `day_type` VARCHAR(10) DEFAULT 'STRENGTH' COMMENT '训练日类型: STRENGTH/CARDIO/REST';

-- 2. 更新增肌计划(BUILD)
UPDATE `training_plan` SET `day_type` = 'STRENGTH' WHERE `goal_type` = 'BUILD' AND `day_of_week` IN (1,2,3,4,5);
UPDATE `training_plan` SET `day_type` = 'REST'     WHERE `goal_type` = 'BUILD' AND `day_of_week` IN (6,7);

-- 3. 更新减脂计划(LOSE)
UPDATE `training_plan` SET `day_type` = 'STRENGTH' WHERE `goal_type` = 'LOSE' AND `day_of_week` IN (1,2,4);
UPDATE `training_plan` SET `day_type` = 'CARDIO'   WHERE `goal_type` = 'LOSE' AND `day_of_week` IN (3,5);
UPDATE `training_plan` SET `day_type` = 'REST'     WHERE `goal_type` = 'LOSE' AND `day_of_week` IN (6,7);
