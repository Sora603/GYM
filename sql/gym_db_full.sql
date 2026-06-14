/*M!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.6.21-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: gym_db
-- ------------------------------------------------------
-- Server version	10.6.21-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `gym_db`
--

/*!40000 DROP DATABASE IF EXISTS `gym_db`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `gym_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;

USE `gym_db`;

--
-- Table structure for table `coach`
--

DROP TABLE IF EXISTS `coach`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `coach` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) NOT NULL COMMENT '姓名',
  `photo` varchar(255) DEFAULT NULL COMMENT '照片URL',
  `specialty` varchar(100) DEFAULT NULL COMMENT '擅长领域',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `intro` text DEFAULT NULL COMMENT '简介',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态: 1在职 0离职',
  `create_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教练表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `coach`
--

LOCK TABLES `coach` WRITE;
/*!40000 ALTER TABLE `coach` DISABLE KEYS */;
INSERT INTO `coach` VALUES (1,'张教练','/uploads/coaches/coach1.jpg','增肌训练','13900001111','国家认证健身教练，5年教学经验，专注增肌和力量训练。',1,'2026-06-08 22:10:22'),(2,'李教练','/uploads/coaches/coach2.jpg','减脂塑形','13900002222','AFAA认证教练，擅长功能性训练和减脂课程。',1,'2026-06-08 22:10:22'),(3,'王教练','/uploads/coaches/coach3.jpg','瑜伽普拉提','13900003333','RYT500认证瑜伽导师，教授哈他瑜伽和流瑜伽。',1,'2026-06-08 22:10:22');
/*!40000 ALTER TABLE `coach` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diet_plan`
--

DROP TABLE IF EXISTS `diet_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `diet_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_type` varchar(10) NOT NULL COMMENT '目标: BUILD / LOSE',
  `meal_type` varchar(10) NOT NULL COMMENT '餐别: BREAKFAST/LUNCH/DINNER/SNACK',
  `content` text NOT NULL COMMENT '推荐食物和数量',
  `calories_guide` varchar(50) DEFAULT NULL COMMENT '热量参考',
  `day_type` varchar(10) DEFAULT 'STRENGTH' COMMENT '适用日类型: STRENGTH/CARDIO/REST',
  `macro_ratio` varchar(20) DEFAULT NULL COMMENT '宏量素比例 蛋白质/碳水/脂肪',
  `food_items` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_goal_day_meal` (`goal_type`,`day_type`,`meal_type`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='饮食推荐表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diet_plan`
--

LOCK TABLES `diet_plan` WRITE;
/*!40000 ALTER TABLE `diet_plan` DISABLE KEYS */;
INSERT INTO `diet_plan` VALUES (1,'BUILD','BREAKFAST','燕麦片80g + 全脂牛奶250ml、水煮蛋×2、香蕉×1根、核桃一小把（约15g）','~700kcal','STRENGTH','30/50/20',NULL),(2,'BUILD','LUNCH','糙米饭200g（熟重）+ 鸡胸肉200g + 西兰花/菠菜一大份 + 橄榄油10ml','~800kcal','STRENGTH','30/50/20',NULL),(3,'BUILD','SNACK','全麦面包×2片 + 低脂花生酱 + 乳清蛋白粉1勺 + 香蕉×1（训练后30分钟内饮用最佳）','~400kcal','STRENGTH','30/50/20',NULL),(4,'BUILD','DINNER','红薯/土豆200g（熟重）+ 三文鱼/瘦牛肉150g + 芦笋/四季豆一大份 + 少量橄榄油','~700kcal','STRENGTH','30/50/20',NULL),(5,'BUILD','BEDTIME','希腊酸奶150g + 杏仁一小把（约10g）——睡前加餐，持续供能防夜间肌肉分解','~200kcal','STRENGTH','30/50/20',NULL),(6,'LOSE','BREAKFAST','全麦面包1片 + 蒸红薯50g + 水煮蛋×1 + 无糖豆浆200ml + 凉拌菠菜1小碗（少油）','~350kcal','STRENGTH','30/45/25',NULL),(7,'LOSE','LUNCH','杂粮饭100g（糙米+藜麦+燕麦）+ 鸡胸肉150g（少油煎/清蒸）+ 西兰花/彩椒200g + 番茄豆腐汤1碗','~450kcal','STRENGTH','30/45/25',NULL),(8,'LOSE','SNACK','原味坚果10g（杏仁6-8粒）/ 或无糖希腊酸奶100g + 蓝莓50g（下午饥饿时食用）','≤100kcal','STRENGTH','30/45/25',NULL),(9,'LOSE','DINNER','蒸南瓜80g + 清蒸鳕鱼120g + 凉拌木耳黄瓜150g + 蒜蓉西兰花大量（19:00前吃完）','~350kcal','STRENGTH','30/45/25',NULL),(10,'BUILD','BREAKFAST','燕麦片60g + 脱脂牛奶200ml、水煮蛋×2、蓝莓50g','~550kcal','REST','35/35/30',NULL),(11,'BUILD','LUNCH','杂粮饭150g + 鸡胸肉180g + 西兰花大量 + 橄榄油5ml','~650kcal','REST','35/35/30',NULL),(12,'BUILD','SNACK','希腊酸奶150g + 杏仁10g + 蓝莓少许','~250kcal','REST','35/35/30',NULL),(13,'BUILD','DINNER','蒸红薯150g + 清蒸鲈鱼150g + 凉拌蔬菜大量','~550kcal','REST','35/35/30',NULL),(14,'LOSE','BREAKFAST','全麦面包1片 + 水煮蛋×2 + 无糖豆浆200ml + 小番茄5颗','~320kcal','CARDIO','35/35/30',NULL),(15,'LOSE','LUNCH','杂粮饭80g + 鸡胸肉180g + 彩椒/西兰花200g（训练后吃，蛋白加量）','~430kcal','CARDIO','35/35/30',NULL),(16,'LOSE','SNACK','乳清蛋白粉1勺 + 水（有氧后30min补充，防肌肉分解）','~120kcal','CARDIO','35/35/30',NULL),(17,'LOSE','DINNER','蒸南瓜60g + 清蒸虾仁120g + 蒜蓉西兰花/冬瓜海带汤','~300kcal','CARDIO','35/35/30',NULL),(18,'LOSE','BREAKFAST','水煮蛋×2 + 无糖豆浆200ml + 凉拌黄瓜木耳（无主食）','~250kcal','REST','40/25/35',NULL),(19,'LOSE','LUNCH','鸡胸肉150g + 西兰花/菌菇/青菜大量（无主食，高蛋白+高纤维）','~350kcal','REST','40/25/35',NULL),(20,'LOSE','SNACK','原味坚果8g（杏仁4-5粒）+ 黄瓜半根','~70kcal','REST','40/25/35',NULL),(21,'LOSE','DINNER','清蒸鳕鱼120g + 紫甘蓝沙拉+凉拌菠菜大量（19:00前吃完）','~280kcal','REST','40/25/35',NULL);
/*!40000 ALTER TABLE `diet_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `entry_log`
--

DROP TABLE IF EXISTS `entry_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `entry_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `card_id` bigint(20) DEFAULT NULL COMMENT '入场使用的卡ID',
  `enter_time` datetime DEFAULT current_timestamp() COMMENT '入场时间',
  `exit_time` datetime DEFAULT NULL COMMENT '离场时间',
  `status` tinyint(4) DEFAULT 1 COMMENT '1=在场 0=已离场',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='入场日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `entry_log`
--

LOCK TABLES `entry_log` WRITE;
/*!40000 ALTER TABLE `entry_log` DISABLE KEYS */;
INSERT INTO `entry_log` VALUES (1,3,NULL,'2026-06-11 11:52:47','2026-06-11 14:57:07',0),(2,3,NULL,'2026-06-11 14:57:11','2026-06-11 20:39:44',0),(3,3,NULL,'2026-06-11 20:39:46','2026-06-11 20:50:56',0),(4,3,NULL,'2026-06-11 21:11:06','2026-06-11 21:11:34',0),(5,3,NULL,'2026-06-12 21:12:07','2026-06-12 21:14:40',0),(6,2,NULL,'2026-06-12 21:20:34',NULL,1),(7,16,NULL,'2026-06-12 21:24:03',NULL,1);
/*!40000 ALTER TABLE `entry_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fitness_tip`
--

DROP TABLE IF EXISTS `fitness_tip`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `fitness_tip` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL COMMENT '小贴士内容',
  `category` varchar(20) DEFAULT NULL COMMENT '分类: 营养/训练/恢复/心态',
  `create_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日健身小贴士';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fitness_tip`
--

LOCK TABLES `fitness_tip` WRITE;
/*!40000 ALTER TABLE `fitness_tip` DISABLE KEYS */;
INSERT INTO `fitness_tip` VALUES (1,'蛋白质的最佳摄入窗口是训练后30分钟内，此时肌肉合成效率最高。','营养','2026-06-12 20:20:13'),(2,'深蹲时膝盖不要超过脚尖？实际上只要膝盖与脚尖方向一致，略微超过是正常的。','训练','2026-06-12 20:20:13'),(3,'肌肉不是在训练中生长的，而是在休息和睡眠中。保证7-8小时睡眠比多练1小时更重要。','恢复','2026-06-12 20:20:13'),(4,'减脂不是饿出来的。过度节食会降低基础代谢，变成越吃越少却越来越胖的恶性循环。','营养','2026-06-12 20:20:13'),(5,'复合动作（深蹲、卧推、硬拉）能同时刺激多个肌群，燃脂效率是孤立动作的2-3倍。','训练','2026-06-12 20:20:13'),(6,'每天多喝1升水，基础代谢可提升约10%。肌肉中75%是水，脱水会严重影响训练表现。','营养','2026-06-12 20:20:13'),(7,'HIIT训练的后燃效应可持续24-48小时，意味着练完后的两天内你都在额外燃烧热量。','训练','2026-06-12 20:20:13'),(8,'练腹肌不能局部减脂。腹肌是在厨房里练出来的——体脂率降到15%以下才会显现。','营养','2026-06-12 20:20:13'),(9,'训练前1-2小时吃一顿含碳水的餐食，可以提升训练表现15-20%。','营养','2026-06-12 20:20:13'),(10,'泡沫轴放松不只是舒服，它能打破肌肉筋膜粘连，加速乳酸代谢，减少延迟性酸痛。','恢复','2026-06-12 20:20:13'),(11,'每周增加训练量不要超过10%，这是避免运动损伤的黄金法则。','训练','2026-06-12 20:20:13'),(12,'心情不好去健身？研究表明30分钟中等强度运动能显著提升情绪，效果持续数小时。','心态','2026-06-12 20:20:13'),(13,'蛋白质不是越多越好。单次摄入超过40g，多余的蛋白质可能被转化为脂肪储存。','营养','2026-06-12 20:20:13'),(14,'早上空腹有氧确实能燃烧更多脂肪，但也会增加肌肉流失风险。建议先吃一个水煮蛋。','训练','2026-06-12 20:20:13'),(15,'拉伸不能预防肌肉酸痛，但能改善关节活动度，长期坚持让动作更标准、受伤风险更低。','训练','2026-06-12 20:20:13'),(16,'只做有氧不练力量，减下来的体重有25%是肌肉。力量训练是保住代谢率的关键。','训练','2026-06-12 20:20:13'),(17,'睡前2小时内不建议高强度训练，体温升高会干扰入睡。可以做轻度拉伸或瑜伽。','恢复','2026-06-12 20:20:13'),(18,'增肌期体脂上升是正常的，但每月增长不超过1-2kg体重，否则脂肪占比过高。','营养','2026-06-12 20:20:13'),(19,'训练中喝含电解质的水（非含糖饮料），可延缓疲劳、减少抽筋风险。','营养','2026-06-12 20:20:13'),(20,'健身最大的敌人不是累，而是\"明天再开始\"。今天迈出第一步就是胜利。','心态','2026-06-12 20:20:13');
/*!40000 ALTER TABLE `fitness_tip` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gym_daily`
--

DROP TABLE IF EXISTS `gym_daily`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `gym_daily` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `record_date` date NOT NULL COMMENT '日期',
  `current_count` int(11) DEFAULT 0 COMMENT '当前在场人数',
  `total_in` int(11) DEFAULT 0 COMMENT '当日入场总数',
  `peak_count` int(11) DEFAULT 0 COMMENT '当日峰值人数',
  `total_revenue` decimal(10,2) DEFAULT 0.00 COMMENT '当日销售额',
  `gym_capacity` int(11) DEFAULT 80 COMMENT '场馆器械容量',
  `update_time` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `record_date` (`record_date`),
  UNIQUE KEY `idx_date` (`record_date`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日客流记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gym_daily`
--

LOCK TABLES `gym_daily` WRITE;
/*!40000 ALTER TABLE `gym_daily` DISABLE KEYS */;
INSERT INTO `gym_daily` VALUES (1,'2026-06-08',12,120,55,3200.00,80,'2026-06-09 00:06:44'),(2,'2026-06-02',0,87,35,1560.00,80,'2026-06-08 23:54:39'),(3,'2026-06-03',0,87,35,1560.00,80,'2026-06-09 00:06:44'),(4,'2026-06-04',0,95,42,1890.00,80,'2026-06-09 00:06:44'),(5,'2026-06-05',0,72,28,980.00,80,'2026-06-09 00:06:44'),(6,'2026-06-06',0,108,48,2350.00,80,'2026-06-09 00:06:44'),(7,'2026-06-07',0,63,22,760.00,80,'2026-06-09 00:06:44'),(9,'2026-06-09',19,63,24,1280.00,80,'2026-06-09 23:53:32'),(10,'2026-06-10',2,2,3,0.00,80,'2026-06-10 20:57:49'),(11,'2026-06-11',0,4,2,0.00,80,'2026-06-11 21:11:34'),(12,'2026-06-12',2,3,3,0.00,80,'2026-06-12 21:24:03');
/*!40000 ALTER TABLE `gym_daily` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gym_timeslot`
--

DROP TABLE IF EXISTS `gym_timeslot`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `gym_timeslot` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `record_date` date NOT NULL COMMENT '日期',
  `time_slot` varchar(15) NOT NULL COMMENT '时间段',
  `peak_count` int(11) DEFAULT 0 COMMENT '该时段峰值人数',
  `update_time` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_date_slot` (`record_date`,`time_slot`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='每日分时段峰值';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gym_timeslot`
--

LOCK TABLES `gym_timeslot` WRITE;
/*!40000 ALTER TABLE `gym_timeslot` DISABLE KEYS */;
INSERT INTO `gym_timeslot` VALUES (1,'2026-06-08','其他',1,'2026-06-08 22:19:40'),(44,'2026-06-02','8:00-10:00',8,'2026-06-08 23:55:15'),(45,'2026-06-02','10:00-12:00',18,'2026-06-08 23:55:15'),(46,'2026-06-02','12:00-14:00',12,'2026-06-08 23:55:15'),(47,'2026-06-02','14:00-16:00',25,'2026-06-08 23:55:15'),(48,'2026-06-02','16:00-20:00',35,'2026-06-08 23:55:15'),(49,'2026-06-02','20:00-22:00',20,'2026-06-08 23:55:15'),(50,'2026-06-03','8:00-10:00',10,'2026-06-08 23:55:15'),(51,'2026-06-03','10:00-12:00',15,'2026-06-08 23:55:15'),(52,'2026-06-03','12:00-14:00',10,'2026-06-08 23:55:15'),(53,'2026-06-03','14:00-16:00',22,'2026-06-08 23:55:15'),(54,'2026-06-03','16:00-20:00',42,'2026-06-08 23:55:15'),(55,'2026-06-03','20:00-22:00',28,'2026-06-08 23:55:15'),(56,'2026-06-04','8:00-10:00',6,'2026-06-08 23:55:15'),(57,'2026-06-04','10:00-12:00',12,'2026-06-08 23:55:15'),(58,'2026-06-04','12:00-14:00',8,'2026-06-08 23:55:15'),(59,'2026-06-04','14:00-16:00',20,'2026-06-08 23:55:15'),(60,'2026-06-04','16:00-20:00',28,'2026-06-08 23:55:15'),(61,'2026-06-04','20:00-22:00',18,'2026-06-08 23:55:15'),(62,'2026-06-05','8:00-10:00',12,'2026-06-08 23:55:15'),(63,'2026-06-05','10:00-12:00',20,'2026-06-08 23:55:15'),(64,'2026-06-05','12:00-14:00',15,'2026-06-08 23:55:15'),(65,'2026-06-05','14:00-16:00',30,'2026-06-08 23:55:15'),(66,'2026-06-05','16:00-20:00',48,'2026-06-08 23:55:15'),(67,'2026-06-05','20:00-22:00',32,'2026-06-08 23:55:15'),(68,'2026-06-06','8:00-10:00',5,'2026-06-08 23:55:15'),(69,'2026-06-06','10:00-12:00',10,'2026-06-08 23:55:15'),(70,'2026-06-06','12:00-14:00',8,'2026-06-08 23:55:15'),(71,'2026-06-06','14:00-16:00',16,'2026-06-08 23:55:15'),(72,'2026-06-06','16:00-20:00',22,'2026-06-08 23:55:15'),(73,'2026-06-06','20:00-22:00',14,'2026-06-08 23:55:15'),(74,'2026-06-07','8:00-10:00',15,'2026-06-08 23:55:15'),(75,'2026-06-07','10:00-12:00',22,'2026-06-08 23:55:15'),(76,'2026-06-07','12:00-14:00',18,'2026-06-08 23:55:15'),(77,'2026-06-07','14:00-16:00',35,'2026-06-08 23:55:15'),(78,'2026-06-07','16:00-20:00',55,'2026-06-08 23:55:15'),(79,'2026-06-07','20:00-22:00',38,'2026-06-08 23:55:15'),(80,'2026-06-08','8:00-10:00',10,'2026-06-08 23:55:15'),(81,'2026-06-08','10:00-12:00',16,'2026-06-08 23:55:15'),(82,'2026-06-08','12:00-14:00',12,'2026-06-08 23:55:15'),(83,'2026-06-08','14:00-16:00',18,'2026-06-08 23:55:15'),(84,'2026-06-08','16:00-20:00',15,'2026-06-08 23:55:15'),(85,'2026-06-08','20:00-22:00',0,'2026-06-08 23:55:15'),(86,'2026-06-09','其他',19,'2026-06-09 23:53:32'),(87,'2026-06-10','20:00-22:00',2,'2026-06-10 20:57:49'),(89,'2026-06-11','10:00-12:00',12,'2026-06-11 11:43:52'),(104,'2026-06-11','14:00-16:00',1,'2026-06-11 14:57:11'),(105,'2026-06-11','20:00-22:00',1,'2026-06-11 20:39:46'),(107,'2026-06-12','20:00-22:00',2,'2026-06-12 21:24:03');
/*!40000 ALTER TABLE `gym_timeslot` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membership_card`
--

DROP TABLE IF EXISTS `membership_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `membership_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '卡名称（月卡/季卡/年卡/次卡）',
  `type` varchar(20) NOT NULL COMMENT '类型: MONTH/SEASON/YEAR/TIME',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `duration_days` int(11) NOT NULL DEFAULT 30 COMMENT '有效天数',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='健身卡类型表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membership_card`
--

LOCK TABLES `membership_card` WRITE;
/*!40000 ALTER TABLE `membership_card` DISABLE KEYS */;
INSERT INTO `membership_card` VALUES (1,'月卡','MONTH',299.00,30,'30天无限次入场','2026-06-08 22:10:22'),(2,'季卡','SEASON',799.00,90,'90天无限次入场','2026-06-08 22:10:22'),(3,'年卡','YEAR',2399.00,365,'365天无限次入场，赠送2次私教课','2026-06-08 22:10:22'),(4,'10日体验卡','TIME',199.00,10,'10天无限次入场','2026-06-08 22:10:22');
/*!40000 ALTER TABLE `membership_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `motivational_quote`
--

DROP TABLE IF EXISTS `motivational_quote`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `motivational_quote` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(500) NOT NULL COMMENT '名言内容',
  `author` varchar(100) DEFAULT NULL COMMENT '出处/作者',
  `create_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='励志名言表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `motivational_quote`
--

LOCK TABLES `motivational_quote` WRITE;
/*!40000 ALTER TABLE `motivational_quote` DISABLE KEYS */;
INSERT INTO `motivational_quote` VALUES (1,'没有付出，就没有收获。','佚名','2026-06-12 19:44:17'),(2,'今天你感受到的疼痛，会变成明天你感受到的力量。','佚名','2026-06-12 19:44:17'),(3,'力量不是来自胜利，你的挣扎才会真正增长你的力量。','阿诺德·施瓦辛格','2026-06-12 19:44:17'),(4,'文明其精神，野蛮其体魄。','毛泽东','2026-06-12 19:44:17'),(5,'汗水就是脂肪的泪水。','佚名','2026-06-12 19:44:17'),(6,'放弃可以找到一万个理由，坚持只需一个信念！','佚名','2026-06-12 19:44:17'),(7,'如果杠铃杆没弯，你就是在装样子。','健身房谚语','2026-06-12 19:44:17'),(8,'成功的路上并不拥挤，因为坚持的人不多。','佚名','2026-06-12 19:44:17'),(9,'时间会证明一切，汗水从不会骗人。','佚名','2026-06-12 19:44:17'),(10,'今天去做别人不愿做的事，明天就可以做别人办不到的事。','佚名','2026-06-12 19:44:17'),(11,'动力让你开始，但习惯才让你一直继续。','佚名','2026-06-12 19:44:17'),(12,'好身材就像婚姻，你不可能欺骗它还指望一切都好。','佚名','2026-06-12 19:44:17'),(13,'世界上没有比结实的肌肉和新鲜的皮肤更美丽的衣裳。','马雅可夫斯基','2026-06-12 19:44:17'),(14,'如果你想强壮，跑步吧！如果你想健美，跑步吧！如果你想聪明，跑步吧！','古希腊谚语','2026-06-12 19:44:17'),(15,'生命在于运动。','伏尔泰','2026-06-12 19:44:17'),(16,'健全之精神，必寓于健全之肉体。','佚名','2026-06-12 19:44:17'),(17,'宝剑锋从磨砺出，梅花香自苦寒来。','佚名','2026-06-12 19:44:17'),(18,'现在流的口水，将成为明天的眼泪；现在流的汗水，将换来明天的微笑。','佚名','2026-06-12 19:44:17'),(19,'当你想放弃的时候，记住你为什么开始。','佚名','2026-06-12 19:44:17'),(20,'别只是渴望好身材，去为它奋斗。','佚名','2026-06-12 19:44:17'),(21,'从来不会变得更轻松，只是你变得更强了。','佚名','2026-06-12 19:44:17'),(22,'借口无用，结果无价。','佚名','2026-06-12 19:44:17'),(23,'你的头脑相信什么，你的身体就得到什么。','佚名','2026-06-12 19:44:17'),(24,'强壮的人比弱小的人更难被杀死，总体上也更有用。','Mark Rippetoe','2026-06-12 19:44:17'),(25,'钢铁永远不会对你撒谎。','Henry Rollins','2026-06-12 19:44:17'),(26,'太轻了！轻得跟花生一样！','罗尼·库尔曼','2026-06-12 19:44:17'),(27,'健身没有什么捷径可走。','Mark Rippetoe','2026-06-12 19:44:17'),(28,'自律给我自由。','佚名','2026-06-12 19:44:17'),(29,'每一次想放弃的瞬间，都是你突破极限的机会。','佚名','2026-06-12 19:44:17'),(30,'昨天的你，是今天的你的底线。','佚名','2026-06-12 19:44:17');
/*!40000 ALTER TABLE `motivational_quote` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `news`
--

DROP TABLE IF EXISTS `news`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL COMMENT '标题',
  `content` text NOT NULL COMMENT '内容',
  `admin_id` bigint(20) DEFAULT NULL COMMENT '发布人ID',
  `publish_time` datetime DEFAULT current_timestamp() COMMENT '发布时间',
  `create_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新闻通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `news`
--

LOCK TABLES `news` WRITE;
/*!40000 ALTER TABLE `news` DISABLE KEYS */;
INSERT INTO `news` VALUES (1,'欢迎来到PowerFit健身房！','我们提供最专业的健身设备和教练团队，期待您的到来！',1,'2026-06-08 22:10:22','2026-06-08 22:10:22'),(2,'本月团课安排已更新','新增普拉提和搏击操课程，详情请咨询前台或查看课表。',1,'2026-06-08 22:10:22','2026-06-08 22:10:22'),(3,'夏季减脂训练营火热报名中','6月15日正式开营，为期8周的减脂训练营现正火热报名！包含定制饮食方案+每日团课+每周体测追踪，前50名报名立减200元。详情请咨询前台或联系教练。',1,'2026-06-08 23:54:31','2026-06-08 23:54:31'),(4,'端午节营业时间调整通知','端午节期间（6月10日-6月12日）健身房正常营业，营业时间调整为8:00-20:00。团课暂停，自由训练区正常开放。祝大家端午安康！',1,'2026-06-08 23:54:31','2026-06-08 23:54:31'),(5,'新器械到店：高端跑步机全面升级','我们引进了10台最新款智能跑步机，配备27寸触摸屏、心率监测和虚拟跑步路线，已全部调试完毕投入使用。欢迎会员前来体验！',1,'2026-06-08 23:54:31','2026-06-08 23:54:31'),(6,'6月会员日活动回顾','上周六的会员日活动圆满结束！感谢80+位会员热情参与，现场举办了平板支撑挑战赛、卧推PK和幸运抽奖，气氛超燃！获奖名单已公布在公告栏。',1,'2026-06-08 23:54:31','2026-06-08 23:54:31'),(7,'暑期学生卡限时特惠','即日起至8月31日，凭有效学生证办理暑期卡享6折优惠，月卡仅需179元！不限次数入场，还可免费参加所有团课。叫上同学一起来练！',1,'2026-06-08 23:54:31','2026-06-08 23:54:31');
/*!40000 ALTER TABLE `news` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '商品名称',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int(11) DEFAULT 0 COMMENT '库存',
  `image` varchar(255) DEFAULT NULL COMMENT '图片URL',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态: 1在售 0下架',
  `create_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'矿泉水',3.00,200,'/uploads/1cf8b633-262d-4987-8c84-0d23fb72a27d.jpg',1,'2026-06-08 22:10:22'),(2,'蛋白粉(500g)',199.00,49,NULL,1,'2026-06-08 22:10:22'),(3,'能量饮料',8.00,100,NULL,1,'2026-06-08 22:10:22'),(4,'运动毛巾',29.00,80,NULL,1,'2026-06-08 22:10:22'),(5,'test',99.00,50,'/uploads/test.jpg',0,'2026-06-11 11:14:52');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_record`
--

DROP TABLE IF EXISTS `sales_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '购买用户ID',
  `item_type` varchar(20) NOT NULL COMMENT '类型: CARD/PRODUCT',
  `item_name` varchar(100) NOT NULL COMMENT '商品/卡名称',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `create_time` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='销售记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_record`
--

LOCK TABLES `sales_record` WRITE;
/*!40000 ALTER TABLE `sales_record` DISABLE KEYS */;
INSERT INTO `sales_record` VALUES (1,3,'CARD','年卡',2399.00,NULL),(2,3,'PRODUCT','蛋白粉(500g)',199.00,NULL),(3,3,'CARD','年卡',2399.00,80,'2026-05-15 09:35:00'),(4,4,'CARD','季卡',799.00,80,'2026-05-20 14:25:00'),(5,5,'CARD','月卡',299.00,80,'2026-05-22 11:15:00'),(6,5,'CARD','季卡',799.00,80,'2026-06-03 10:00:00'),(7,6,'CARD','年卡',2399.00,80,'2026-05-25 16:50:00'),(8,7,'CARD','月卡',299.00,80,'2026-05-28 08:35:00'),(9,8,'CARD','季卡',799.00,80,'2026-06-01 10:05:00'),(10,9,'CARD','次卡(10次)',199.00,80,'2026-06-02 15:35:00'),(11,10,'CARD','年卡',2399.00,80,'2026-06-03 09:05:00'),(12,11,'CARD','月卡',299.00,80,'2026-06-04 17:05:00'),(13,12,'CARD','季卡',799.00,80,'2026-06-05 11:25:00'),(14,14,'CARD','月卡',299.00,80,'2026-06-07 08:20:00'),(15,2,'CARD','季卡',799.00,80,'2026-06-01 12:00:00'),(16,3,'PRODUCT','蛋白粉(500g)',199.00,80,'2026-06-08 10:30:00'),(17,4,'PRODUCT','能量饮料',8.00,80,'2026-06-08 11:15:00'),(18,5,'PRODUCT','运动毛巾',29.00,80,'2026-06-08 14:20:00'),(19,6,'PRODUCT','矿泉水',3.00,80,'2026-06-08 15:00:00'),(20,7,'PRODUCT','蛋白粉(500g)',199.00,80,'2026-06-08 16:30:00'),(21,8,'PRODUCT','能量饮料',16.00,80,'2026-06-08 17:00:00'),(22,8,'PRODUCT','运动毛巾',29.00,80,'2026-06-08 17:10:00'),(23,9,'PRODUCT','矿泉水',6.00,80,'2026-06-08 18:30:00'),(24,10,'PRODUCT','蛋白粉(500g)',199.00,80,'2026-06-08 19:00:00'),(25,11,'PRODUCT','能量饮料',8.00,80,'2026-06-08 19:45:00'),(26,12,'PRODUCT','运动毛巾',29.00,80,'2026-06-08 20:00:00'),(27,2,'PRODUCT','矿泉水',3.00,80,'2026-06-08 20:30:00'),(28,3,'PRODUCT','能量饮料',8.00,80,'2026-06-08 21:00:00'),(29,4,'PRODUCT','蛋白粉(500g)',199.00,80,'2026-06-07 09:30:00'),(30,6,'PRODUCT','运动毛巾',29.00,80,'2026-06-07 10:00:00'),(31,7,'PRODUCT','矿泉水',6.00,80,'2026-06-07 14:00:00'),(32,10,'PRODUCT','能量饮料',8.00,80,'2026-06-07 16:30:00'),(33,5,'PRODUCT','蛋白粉(500g)',199.00,80,'2026-06-06 11:00:00'),(34,8,'PRODUCT','矿泉水',3.00,80,'2026-06-06 15:00:00'),(35,9,'PRODUCT','能量饮料',8.00,80,'2026-06-06 18:00:00'),(36,3,'CARD','年卡',2399.00,80,'2026-06-10 20:25:53'),(37,1,'CARD','年卡',2399.00,80,'2026-06-10 20:46:06'),(38,1,'CARD','年卡',2399.00,80,'2026-06-10 20:46:12'),(39,3,'CARD','月卡',299.00,80,'2026-06-11 10:32:35'),(40,3,'CARD','次卡(10次)',199.00,80,'2026-06-11 11:16:31'),(41,2,'CARD','月卡',299.00,80,'2026-06-11 11:43:52'),(42,16,'CARD','月卡',299.00,80,'2026-06-12 21:23:48');
/*!40000 ALTER TABLE `sales_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（BCrypt加密）',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `photo` varchar(255) DEFAULT NULL COMMENT '照片路径',
  `vip_card_no` varchar(32) DEFAULT NULL COMMENT 'VIP卡号（唯一）',
  `role` varchar(10) NOT NULL DEFAULT 'USER' COMMENT '角色: USER/ADMIN',
  `create_time` datetime DEFAULT current_timestamp() COMMENT '创建时间',
  `fitness_goal` varchar(10) DEFAULT NULL COMMENT '健身目标: BUILD/LOSE',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `vip_card_no` (`vip_card_no`),
  KEY `idx_vip_card` (`vip_card_no`),
  KEY `idx_role` (`role`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$5EgwNqP.Bq0A7HgIlqYXkOkKuqOZYAPlleAMaAM0r0YkdKnN/hu4a',NULL,NULL,NULL,'ADMIN','2026-06-08 22:10:22',NULL),(2,'test','$2a$10$ZWYxZ8LQN70SNQKShpl4KetA1djJ6n9XvO3Bo78eh5CIPoudBqsg.','13800138000',NULL,'VIP20260012','USER','2026-06-08 22:10:22',NULL),(3,'Jelly','$2a$10$exzUr4z7ZBCEcE359OXIsedYBNI.EBl6lfRYf91NVPQoq48CGrDAG','15314025300','/uploads/ea48237c-9a1c-46a4-be85-c85fb29561de.png','VIP15314025300','USER',NULL,'BUILD'),(4,'张三丰','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110001',NULL,'VIP20260001','USER','2026-05-15 09:30:00',NULL),(5,'李铁柱','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110002',NULL,'VIP20260002','USER','2026-05-20 14:20:00',NULL),(6,'王小美','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110003',NULL,'VIP20260003','USER','2026-05-22 11:10:00',NULL),(7,'赵大壮','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110004',NULL,'VIP20260004','USER','2026-05-25 16:45:00',NULL),(8,'陈美丽','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110005',NULL,'VIP20260005','USER','2026-05-28 08:30:00',NULL),(9,'刘浩然','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110006',NULL,'VIP20260006','USER','2026-06-01 10:00:00',NULL),(10,'周小雅','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110007',NULL,'VIP20260007','USER','2026-06-02 15:30:00',NULL),(11,'吴铁拳','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110008',NULL,'VIP20260008','USER','2026-06-03 09:00:00',NULL),(12,'郑小燕','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110009',NULL,'VIP20260009','USER','2026-06-04 17:00:00',NULL),(13,'孙志强','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110010',NULL,'VIP20260010','USER','2026-06-05 11:20:00',NULL),(14,'钱多福','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110011',NULL,NULL,'USER','2026-06-06 13:00:00',NULL),(15,'马薇薇','a\0.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi','13911110012',NULL,'VIP20260011','USER','2026-06-07 08:15:00',NULL),(16,'111111','$2a$10$/GqfmadCc/xhDAxjmIk1GOsyLv73/6UislW00XqgeM9mebo7jYaTK','111111',NULL,'VIP111111','USER','2026-06-12 21:22:45','LOSE');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `training_plan`
--

DROP TABLE IF EXISTS `training_plan`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `training_plan` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `goal_type` varchar(10) NOT NULL COMMENT '目标: BUILD(增肌) / LOSE(减脂)',
  `day_of_week` int(11) NOT NULL COMMENT '星期几 1-7 (周一=1)',
  `focus` varchar(100) NOT NULL COMMENT '训练重点',
  `exercises` text NOT NULL COMMENT 'JSON格式训练动作',
  `warm_up` varchar(255) DEFAULT NULL COMMENT '热身建议',
  `cardio` varchar(255) DEFAULT NULL COMMENT '有氧建议（减脂用）',
  `notes` varchar(255) DEFAULT NULL COMMENT '当日注意事项',
  `day_type` varchar(10) DEFAULT 'STRENGTH' COMMENT '训练日类型: STRENGTH/CARDIO/REST',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_goal_day` (`goal_type`,`day_of_week`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='训练计划模板表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `training_plan`
--

LOCK TABLES `training_plan` WRITE;
/*!40000 ALTER TABLE `training_plan` DISABLE KEYS */;
INSERT INTO `training_plan` VALUES (1,'BUILD',1,'胸部+三头肌','[{\"name\":\"杠铃卧推\",\"sets\":\"4\",\"reps\":\"8-12\",\"rest\":\"90s\",\"note\":\"核心收紧，背部贴凳\"},{\"name\":\"上斜哑铃卧推\",\"sets\":\"4\",\"reps\":\"10-12\",\"rest\":\"90s\",\"note\":\"上胸发力，顶峰收缩\"},{\"name\":\"哑铃飞鸟\",\"sets\":\"3\",\"reps\":\"12-15\",\"rest\":\"60s\",\"note\":\"控制离心，拉伸胸肌\"},{\"name\":\"绳索下压\",\"sets\":\"3\",\"reps\":\"12-15\",\"rest\":\"60s\",\"note\":\"肘部锁定，三头孤立发力\"},{\"name\":\"双杠臂屈伸\",\"sets\":\"3\",\"reps\":\"力竭\",\"rest\":\"90s\",\"note\":\"身体前倾刺激下胸\"}]','肩关节环绕2min + 弹力带推胸激活3min',NULL,'增肌黄金日，大重量复合动作为主','STRENGTH'),(2,'BUILD',2,'背部+二头肌','[{\"name\":\"引体向上\",\"sets\":\"4\",\"reps\":\"力竭\",\"rest\":\"90s\",\"note\":\"宽握，背部发力启动\"},{\"name\":\"杠铃划船\",\"sets\":\"4\",\"reps\":\"8-12\",\"rest\":\"90s\",\"note\":\"俯身45°，杠铃沿大腿上拉\"},{\"name\":\"高位下拉\",\"sets\":\"3\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"沉肩，挤压背阔肌\"},{\"name\":\"单臂哑铃划船\",\"sets\":\"3\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"每侧独立，拉伸与收缩并重\"},{\"name\":\"杠铃弯举\",\"sets\":\"3\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"大臂固定，二头孤立发力\"}]','肩胛激活5min + 弹力带划船热身',NULL,'感受背阔肌的拉伸与收缩','STRENGTH'),(3,'BUILD',3,'肩部+腹肌','[{\"name\":\"坐姿哑铃推举\",\"sets\":\"4\",\"reps\":\"8-12\",\"rest\":\"90s\",\"note\":\"核心收紧，全程控制\"},{\"name\":\"哑铃侧平举\",\"sets\":\"4\",\"reps\":\"12-15\",\"rest\":\"60s\",\"note\":\"轻重量，控制离心，不要借力\"},{\"name\":\"面拉\",\"sets\":\"3\",\"reps\":\"15\",\"rest\":\"60s\",\"note\":\"绳索拉向面部，刺激后束\"},{\"name\":\"卷腹\",\"sets\":\"3\",\"reps\":\"20\",\"rest\":\"45s\",\"note\":\"上背部离地即可，腰部贴地\"},{\"name\":\"悬垂举腿\",\"sets\":\"3\",\"reps\":\"15\",\"rest\":\"60s\",\"note\":\"控制摆动，腹肌发力\"}]','肩袖激活5min + 弹力带内外旋',NULL,'肩部是门面，侧平举质量大于重量','STRENGTH'),(4,'BUILD',4,'腿部（股四头肌主导）','[{\"name\":\"杠铃深蹲\",\"sets\":\"4\",\"reps\":\"6-10\",\"rest\":\"120s\",\"note\":\"核心收紧，膝盖不内扣，大腿平行地面\"},{\"name\":\"腿举\",\"sets\":\"4\",\"reps\":\"10-12\",\"rest\":\"90s\",\"note\":\"控制深度，不要锁死膝盖\"},{\"name\":\"腿屈伸\",\"sets\":\"3\",\"reps\":\"12-15\",\"rest\":\"60s\",\"note\":\"顶峰收缩1秒，孤立股四头肌\"},{\"name\":\"坐姿提踵\",\"sets\":\"4\",\"reps\":\"15-20\",\"rest\":\"60s\",\"note\":\"全程拉伸与收缩小腿\"}]','髋关节环绕3min + 空蹲激活2min',NULL,'深蹲是增肌之王，动作质量第一','STRENGTH'),(5,'BUILD',5,'全身复合+手臂强化','[{\"name\":\"罗马尼亚硬拉\",\"sets\":\"4\",\"reps\":\"8-10\",\"rest\":\"90s\",\"note\":\"屈髋主导，背平直，腘绳肌拉伸\"},{\"name\":\"上斜哑铃推举\",\"sets\":\"3\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"复合推，肩胸协同\"},{\"name\":\"锤式弯举\",\"sets\":\"3\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"肱肌+二头，握力训练\"},{\"name\":\"绳索下压\",\"sets\":\"3\",\"reps\":\"12-15\",\"rest\":\"60s\",\"note\":\"三头肌外侧头\"},{\"name\":\"臀推\",\"sets\":\"4\",\"reps\":\"10-12\",\"rest\":\"90s\",\"note\":\"顶峰夹紧臀部1-2秒\"}]','全身动态拉伸5min',NULL,'周五补齐本周薄弱部位','STRENGTH'),(6,'BUILD',6,'主动恢复日','[{\"name\":\"泡沫轴放松\",\"sets\":\"1\",\"reps\":\"全身\",\"rest\":\"-\",\"note\":\"每个部位滚压30-60秒\"},{\"name\":\"静态拉伸\",\"sets\":\"1\",\"reps\":\"全身\",\"rest\":\"-\",\"note\":\"每个拉伸保持20-30秒\"}]',NULL,NULL,'恢复是增肌的一部分，不要忽视','REST'),(7,'BUILD',7,'休息日','[{\"name\":\"完全休息\",\"sets\":\"-\",\"reps\":\"-\",\"rest\":\"-\",\"note\":\"保证充足睡眠，蛋白质摄入不要断\"}]',NULL,NULL,'肌肉在休息时生长，睡眠7-8小时','REST'),(8,'LOSE',1,'下肢力量+有氧','[{\"name\":\"杠铃深蹲\",\"sets\":\"3-4\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"中等重量，动作标准优先\"},{\"name\":\"罗马尼亚硬拉\",\"sets\":\"3-4\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"控制离心，感受后链拉伸\"},{\"name\":\"箭步蹲\",\"sets\":\"3\",\"reps\":\"12-15/侧\",\"rest\":\"60s\",\"note\":\"核心收紧，膝盖稳定\"},{\"name\":\"臀桥\",\"sets\":\"3\",\"reps\":\"15\",\"rest\":\"45s\",\"note\":\"顶峰夹紧停留1秒\"}]','动态拉伸+高抬腿 5min','跑步机坡度走20-30min（坡度10-12，速度4.5-5.0）','先力量后有氧——糖原消耗后直接燃脂','STRENGTH'),(9,'LOSE',2,'上肢推+有氧','[{\"name\":\"杠铃卧推\",\"sets\":\"3-4\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"中等重量，控制节奏\"},{\"name\":\"哑铃肩推\",\"sets\":\"3-4\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"核心收紧，全程稳定\"},{\"name\":\"上斜哑铃飞鸟\",\"sets\":\"3\",\"reps\":\"12-15\",\"rest\":\"45s\",\"note\":\"轻重量，感受拉伸\"},{\"name\":\"绳索下压\",\"sets\":\"3\",\"reps\":\"12-15\",\"rest\":\"45s\",\"note\":\"三头肌燃烧感\"}]','肩关节环绕 5min','慢跑/动感单车 20-30min','力量训练保持肌肉量，有氧加速燃脂','STRENGTH'),(10,'LOSE',3,'纯有氧燃脂日','[{\"name\":\"跑步机坡度走\",\"sets\":\"1\",\"reps\":\"40-50min\",\"rest\":\"-\",\"note\":\"心率保持130-140，中等强度稳态\"},{\"name\":\"或HIIT跳绳\",\"sets\":\"8\",\"reps\":\"30s快+15s慢\",\"rest\":\"循环\",\"note\":\"高强度间歇，后燃效应强\"}]','5min 动态拉伸',NULL,'纯有氧日，给肌肉恢复时间，专注脂肪燃烧','CARDIO'),(11,'LOSE',4,'背部+二头+有氧','[{\"name\":\"高位下拉\",\"sets\":\"3-4\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"沉肩，背阔肌发力\"},{\"name\":\"俯身哑铃划船\",\"sets\":\"3-4\",\"reps\":\"10-12\",\"rest\":\"60s\",\"note\":\"每侧独立，拉伸充分\"},{\"name\":\"坐姿绳索划船\",\"sets\":\"3\",\"reps\":\"12-15\",\"rest\":\"45s\",\"note\":\"挤压背部中央\"},{\"name\":\"哑铃弯举\",\"sets\":\"3\",\"reps\":\"12-15\",\"rest\":\"45s\",\"note\":\"控制离心3秒\"}]','肩胛激活 5min','椭圆机 20min','背部大肌群训练消耗可观热量','STRENGTH'),(12,'LOSE',5,'HIIT全身燃脂','[{\"name\":\"波比跳\",\"sets\":\"4\",\"reps\":\"30s\",\"rest\":\"15s\",\"note\":\"全力输出，不保留\"},{\"name\":\"开合跳\",\"sets\":\"4\",\"reps\":\"30s\",\"rest\":\"15s\",\"note\":\"节奏稳定\"},{\"name\":\"深蹲跳\",\"sets\":\"4\",\"reps\":\"30s\",\"rest\":\"15s\",\"note\":\"落地轻盈，保护膝盖\"},{\"name\":\"登山跑\",\"sets\":\"4\",\"reps\":\"30s\",\"rest\":\"15s\",\"note\":\"核心收紧，快速交替\"},{\"name\":\"高抬腿\",\"sets\":\"4\",\"reps\":\"30s\",\"rest\":\"15s\",\"note\":\"大腿抬至水平\"}]','全身动态拉伸 5min',NULL,'HIIT总时长20-25min，后燃效应持续24h+','CARDIO'),(13,'LOSE',6,'可选轻度运动','[{\"name\":\"户外慢跑/游泳/骑行\",\"sets\":\"1\",\"reps\":\"40-60min\",\"rest\":\"-\",\"note\":\"低强度稳态有氧，享受运动\"},{\"name\":\"或瑜伽/拉伸课\",\"sets\":\"1\",\"reps\":\"45-60min\",\"rest\":\"-\",\"note\":\"提升柔韧性，促进恢复\"}]',NULL,NULL,'根据身体状态灵活选择，不要勉强','REST'),(14,'LOSE',7,'休息日','[{\"name\":\"完全休息\",\"sets\":\"-\",\"reps\":\"-\",\"rest\":\"-\",\"note\":\"保证睡眠7-8小时，多喝水帮助代谢\"}]',NULL,NULL,'休息是为了更好的燃烧！睡眠不足会阻碍减脂','REST');
/*!40000 ALTER TABLE `training_plan` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_body_info`
--

DROP TABLE IF EXISTS `user_body_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_body_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `height` decimal(5,1) NOT NULL COMMENT '身高(cm)',
  `weight` decimal(5,1) NOT NULL COMMENT '体重(kg)',
  `gender` tinyint(4) NOT NULL DEFAULT 1 COMMENT '性别: 1男 0女',
  `age` int(11) NOT NULL DEFAULT 25 COMMENT '年龄',
  `bmi` decimal(5,1) NOT NULL COMMENT 'BMI值',
  `body_fat` decimal(5,1) NOT NULL COMMENT '体脂率%',
  `record_time` datetime DEFAULT current_timestamp() COMMENT '记录时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_record_time` (`user_id`,`record_time`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户身体数据记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_body_info`
--

LOCK TABLES `user_body_info` WRITE;
/*!40000 ALTER TABLE `user_body_info` DISABLE KEYS */;
INSERT INTO `user_body_info` VALUES (1,3,183.0,75.0,1,18,22.4,14.8,'2026-06-08 23:53:10'),(4,4,170.0,80.0,1,35,27.7,30.3,'2026-06-01 11:00:00'),(5,4,170.0,78.5,1,35,27.2,29.7,'2026-06-08 11:00:00'),(6,5,163.0,55.0,0,24,20.7,24.6,'2026-06-02 09:00:00'),(7,5,163.0,53.5,0,24,20.1,23.9,'2026-06-08 09:00:00'),(8,6,182.0,90.0,1,30,27.2,27.7,'2026-06-03 14:00:00'),(9,6,182.0,88.5,1,30,26.7,27.1,'2026-06-08 14:00:00'),(10,7,165.0,48.0,0,22,17.6,20.4,'2026-06-04 08:00:00'),(11,7,165.0,49.5,0,22,18.2,21.1,'2026-06-08 08:00:00'),(12,8,178.0,72.0,1,26,22.7,21.8,'2026-06-05 16:00:00'),(13,8,178.0,73.5,1,26,23.2,22.4,'2026-06-08 16:00:00'),(14,9,160.0,52.0,0,21,20.3,23.6,'2026-06-05 10:00:00'),(15,9,160.0,51.0,0,21,19.9,23.1,'2026-06-08 10:00:00'),(16,2,178.0,72.0,1,25,22.7,21.6,'2026-06-06 12:00:00'),(17,2,178.0,71.0,1,25,22.4,21.3,'2026-06-08 12:00:00'),(18,3,183.0,70.0,1,18,20.9,13.0,'2026-06-09 23:50:28'),(19,3,183.0,71.0,1,20,21.2,13.8,'2026-06-10 20:35:41');
/*!40000 ALTER TABLE `user_body_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_card`
--

DROP TABLE IF EXISTS `user_card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_card` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `card_id` bigint(20) NOT NULL COMMENT '卡类型ID',
  `card_name` varchar(50) NOT NULL COMMENT '卡名称（冗余）',
  `buy_time` datetime DEFAULT current_timestamp() COMMENT '购买时间',
  `expire_time` datetime NOT NULL COMMENT '到期时间',
  `status` tinyint(4) DEFAULT 1 COMMENT '状态: 1有效 0已过期',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户购卡记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_card`
--

LOCK TABLES `user_card` WRITE;
/*!40000 ALTER TABLE `user_card` DISABLE KEYS */;
INSERT INTO `user_card` VALUES (1,3,3,'年卡','2027-05-15 09:35:00','2028-05-14 09:35:00',1),(2,3,3,'年卡','2026-05-15 09:35:00','2027-05-15 09:35:00',1),(3,4,2,'季卡','2026-05-20 14:25:00','2026-08-18 14:25:00',1),(4,5,1,'月卡','2026-05-22 11:15:00','2026-06-21 11:15:00',0),(5,5,2,'季卡','2026-06-03 10:00:00','2026-09-01 10:00:00',1),(6,6,3,'年卡','2026-05-25 16:50:00','2027-05-25 16:50:00',1),(7,7,1,'月卡','2026-05-28 08:35:00','2026-06-27 08:35:00',1),(8,8,2,'季卡','2026-06-01 10:05:00','2026-08-30 10:05:00',1),(9,9,4,'10日体验卡','2026-06-02 15:35:00','2026-08-31 15:35:00',1),(10,10,3,'年卡','2026-06-03 09:05:00','2027-06-03 09:05:00',1),(11,11,1,'月卡','2026-06-04 17:05:00','2026-07-04 17:05:00',1),(12,12,2,'季卡','2026-06-05 11:25:00','2026-09-03 11:25:00',1),(13,14,1,'月卡','2026-06-07 08:20:00','2026-07-07 08:20:00',1),(14,2,2,'季卡','2026-06-01 12:00:00','2026-08-30 12:00:00',1),(15,3,3,'年卡','2028-05-14 09:35:00','2029-05-14 09:35:00',1),(16,1,3,'年卡','2026-06-10 20:46:06','2027-06-10 20:46:06',1),(17,1,3,'年卡','2026-06-10 20:46:12','2028-06-09 20:46:06',1),(18,3,1,'月卡','2029-05-14 09:35:00','2029-06-13 09:35:00',1),(19,3,4,'10日体验卡','2029-06-13 09:35:00','2029-09-11 09:35:00',1),(21,16,1,'月卡','2026-06-12 21:23:48','2026-07-12 21:23:48',1);
/*!40000 ALTER TABLE `user_card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_checkin`
--

DROP TABLE IF EXISTS `user_checkin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_checkin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `checkin_date` date NOT NULL COMMENT '打卡日期',
  `create_time` datetime DEFAULT current_timestamp() COMMENT '打卡时间',
  `mood` varchar(10) DEFAULT NULL COMMENT '打卡心情emoji',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`,`checkin_date`),
  KEY `idx_date` (`checkin_date`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户打卡记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_checkin`
--

LOCK TABLES `user_checkin` WRITE;
/*!40000 ALTER TABLE `user_checkin` DISABLE KEYS */;
INSERT INTO `user_checkin` VALUES (1,3,'2026-06-12','2026-06-12 19:49:37',NULL),(2,2,'2026-06-12','2026-06-12 20:13:15','??'),(3,16,'2026-06-12','2026-06-12 21:24:03','💪');
/*!40000 ALTER TABLE `user_checkin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'gym_db'
--

--
-- Dumping routines for database 'gym_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-06-12 21:28:11
