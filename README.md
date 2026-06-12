# PowerFit 智能健身房管理系统

## 📦 项目结构

```
PowerFit交付/
├── src/                    # Java 源代码
│   └── main/
│       ├── java/com/gym/
│       │   ├── controller/ # 接口层
│       │   ├── service/    # 业务逻辑层
│       │   ├── mapper/     # 数据库访问层
│       │   ├── entity/     # 数据实体
│       │   └── config/     # 配置类
│       └── resources/
│           ├── static/      # 前端页面 (HTML/CSS/JS)
│           └── application.yml  # 应用配置
├── sql/
│   ├── init.sql            # 建库建表脚本（首次部署用）
│   └── gym_db_full.sql     # 完整数据库备份（含种子数据）
├── pom.xml                 # Maven 依赖配置
├── start.bat               # Windows 一键启动脚本
└── README.md               # 本文档
```

---

## 🚀 运行步骤

### 环境要求

| 软件 | 版本要求 | 说明 |
|------|----------|------|
| Java | JDK 8+ | 推荐 JDK 11 或 17 |
| MySQL/MariaDB | 5.7+ / 10.x | 端口默认 3306 |
| Maven | 3.6+ | 编译打包用（已随 IDEA 自带） |

### 第一步：准备数据库

方式一（全新部署）：
```sql
-- 用 MySQL 客户端执行 init.sql
mysql -u root -p < sql/init.sql
```

方式二（直接导入完整备份，含示例数据）：
```sql
mysql -u root -p < sql/gym_db_full.sql
```

### 第二步：修改配置

编辑 `src/main/resources/application.yml`，修改数据库连接：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/gym_db?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai
    username: root          # 改成你的MySQL用户名
    password: 你的密码       # 改成你的MySQL密码
```

### 第三步：启动

Windows 用户双击 `start.bat` 即可一键编译启动。

或者手动执行：
```bash
# 编译打包
mvn clean package -DskipTests

# 启动
java -jar target/gym-management-1.0.0.jar
```

### 第四步：访问

浏览器打开：**http://localhost:8765**

| 角色 | 账号 | 密码 |
|------|------|------|
| 管理员 | admin | admin123 |
| 会员测试 | test | test123 |

---

## 🔧 技术栈

- **后端**：Spring Boot 2.7 + MyBatis Plus 3.5 + MySQL
- **前端**：原生 HTML/CSS/JS + Chart.js
- **认证**：Session 登录
- **端口**：8765

---

## ✨ 功能清单

### 会员端
| 功能 | 说明 |
|------|------|
| 每日打卡 | 打卡环+撒花特效+心情选择+名言激励 |
| 训练计划 | 增肌/减脂双目标，周视图+动作清单+打卡追踪 |
| 饮食推荐 | 三层可视化（环形总览+瀑布流+食物清单+宏量素） |
| 肌肉热力图 | SVG人体正面/背面，训练部位高亮 |
| 健身卡购买 | 月卡/季卡/年卡/体验卡 |
| 教练团队 | 教练列表+详情 |
| 场馆状态 | 实时在场人数+峰值日历 |
| 身体数据 | BMI + 体脂率 + 趋势图 |
| 商品购买 | 蛋白粉/饮料/毛巾 |
| 通知公告 | 新闻跑马灯+列表 |

### 管理端
| 功能 | 说明 |
|------|------|
| 数据概览 | 会员数/营收/在场人数/饼图 |
| 会员入场 | 手机号刷卡入场→自动打卡 |
| 会员管理 | 增删改查+购卡记录 |
| 教练管理 | 增删改查+照片上传 |
| 商品管理 | 库存+价格管理 |
| 新闻管理 | 发布/编辑公告 |
| 打卡总览 | 今日打卡人数+趋势图+会员列表 |
| 销售记录 | 卡+商品销售明细 |

---

## 📝 管理员账号

默认：`admin / admin123`

如需修改密码，用 BCrypt 在线工具生成后替换数据库 `sys_user` 表中的 `password` 字段。

---

## 🖥️ 默认城市天气

天气数据来自免费 API（wttr.in），默认城市为北京。可在浏览器中点击天气条右侧城市名切换。

---

## ⚠️ 注意事项

1. 首次启动会自动建表（如果数据库为空）
2. 图片上传存储在 `gym-uploads/` 目录
3. 服务器端口 8765，如需修改请在 `application.yml` 中调整
4. 生产环境建议将 `application.yml` 中的 `mybatis-plus` 日志配置 `log-impl` 删除
