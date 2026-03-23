# 项目综合设计 - 周进度报告

> **填写说明**：
> - 本报告每周填写一次，作为过程评价的重要依据
> - 报告内容应真实反映项目进展和个人贡献
> - 请在每周日晚 22:00 前提交至组长处，由组长汇总后提交
> - 提交地址：在个人的GIT项目中创建一个 reports 目录，每周的报告都提交到该目录下。
> - 提交格式：Markdown，文件名格式：`week_xx_report.md`

---

## 基本信息

| 项目 | 内容 |
|-----|------|
| **报告周次** | 第 3 周（填写具体周数，如"第3周"） |
| **报告日期** | 2026年3月22日 |
| **团队名称** | ________无_______________ |
| **项目主题** | □ 图书借阅管理系统 &nbsp; □ 校园二手交易平台 &nbsp; □ 实验室设备预约系统 &nbsp; □ 校园活动与报名平台<br>□ 食堂菜品推荐系统 &nbsp; □ 研究生-导师双选系统 &nbsp; □ 个人健康与运动打卡系统 &nbsp; □ 在线考试系统 |
| **报告人姓名** | _________黎宏______________ |
| **报告人学号** | ___222024321262009____________________ |
| **报告人角色** | □ 组长 &nbsp; □ 组员 |
| **本周Git提交次数** | __15_ 次 |
| **本周代码行数** | 约 ___813___ 行（可使用Git统计：`git diff --stat HEAD~1`） |

---

## 一、本周工作总结

### 1.1 完成的功能/任务

请列出本周完成的主要功能模块或任务，建议使用表格形式：

| 任务编号 | 任务描述 | 完成度 | 负责人 | 备注 |
|---------|---------|--------|--------|------|
| T-3-1 | 项目初始化：Spring Boot + Gradle 项目配置 | 100% | 黎宏 | 已完成，成功运行 |
| T-3-2 | 数据库配置：MySQL 连接池（HikariCP）配置 | 100% | 黎宏 | 已测试连接成功 |
| T-3-3 | 系统规划文档编写：可行性分析、技术选型、Sprint规划|100% |黎宏 | 已提交到git|
| T-3-4 | 系统分析启动：用户画像、用户故事列表| 80%|黎宏 | 已完成初稿，待细化|
| T-3-5 | GitHub 仓库初始化与文档结构搭建 | 100% | 黎宏 | 含 reports 目录 |
| **报告人姓名** | __________黎宏_____________ |
| **报告人学号** | __________222024321262009_____________ |
| **报告人角色** | □ 组长 &nbsp; □ 组员 |
| **本周Git提交次数** | _15__ 次 |
| **本周代码行数** | 约 __813____ 行（可使用Git统计：`git diff --stat HEAD~1`） |

---

### 1.2 技术实现要点

针对本周完成的主要功能，描述其技术实现方式（至少填写2项）：

#### 功能1：[Spring Boot + Gradle 项目初始化]

**实现思路**：
- 使用 Spring Initializr 生成基础项目骨架，选择 Gradle 作为构建工具
- 配置 `build.gradle`，添加 Spring Boot Starter Web、MyBatis、MySQL Driver、Spring Security 等依赖
- 配置 `application.yaml` 数据源，使用 HikariCP 连接池
- 编写测试类验证项目能正常启动

**关键代码片段**：
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-web'
    implementation 'org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.2'
    implementation 'org.springframework.boot:spring-boot-starter-security'
    implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
    runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
    runtimeOnly 'com.mysql:mysql-connector-j'
    testImplementation 'org.springframework.boot:spring-boot-starter-test'
}
```

**遇到的难点及解决**：
- 难点：_______Gradle 首次使用，与 Maven 的依赖语法不同______
- 解决方案：______查阅 Gradle 官方文档，学习 implementation 和 runtimeOnly 的区别______
---

#### 功能2：[项目文档体系建设（GitHub 文档管理）]

**实现思路**：
- 在 GitHub 仓库中创建 docs/01-系统规划/ 目录
- 按照软件工程标准流程，编写项目背景与目标、可行性分析报告、技术选型说明、Sprint 规划、团队分工 5 个文档
- 创建 reports/ 目录用于存放周报
- 编写 README.md 作为项目入口

**关键代码片段**：
```java
docs/
├── 01-系统规划/
│   ├── 项目背景与目标.md
│   ├── 可行性分析报告.md
│   ├── 技术选型说明.md
│   ├── 项目计划-Sprint规划.md
│   └── 团队分工.md
reports/
└── week_03_report.md
```

**遇到的难点及解决**：
- 难点：如何组织文档结构既符合课程要求又贴近实战

- 解决方案：参考软件工程教材章节顺序，将文档按开发阶段（规划→分析→设计→实施）分层组织

---

## 二、技术收获与学习心得

### 2.1 本周学到的技术知识点

请列出本周通过项目实践学到的新技术、新工具、新概念（至少3项）：

1. **[Gradle 构建工具]**：
   - 学习场景：项目初始化时，需要配置 build.gradle 管理依赖
   - 掌握程度：□ 了解 &nbsp; □ 熟悉 &nbsp; □ 掌握 &nbsp; □ 精通
   - 学习心得：Gradle 相比 Maven 更灵活，使用 Groovy/Kotlin DSL 配置，依赖声明更简洁。implementation 是编译时依赖，runtimeOnly 是运行时依赖，testImplementation 是测试依赖，这种细粒度划分有助于减少打包体积。

2. **[Spring Boot 配置管理（YAML）]**：
   - 学习场景：配置 MySQL 数据源、HikariCP 连接池
   - 掌握程度：□ 了解 &nbsp; □ 熟悉 &nbsp; □ 掌握 &nbsp; □ 精通
   - 学习心得：YAML 格式比 properties 更直观，支持层级结构。HikariCP 连接池的关键参数：maximum-pool-size 控制最大连接数，connection-timeout 控制超时时间，合理配置可以提升数据库访问性能。
3. **[Git 分支管理与文档协作]**:
   - 学习场景：在 GitHub 上管理项目文档，使用 main 分支作为稳定版本
   - 掌握程度：□ 了解 &nbsp; □ 熟悉 &nbsp; □ 掌握 &nbsp; □ 精通
   - 学习心得：Git 不只是代码管理工具，也可以管理文档。通过规范的提交信息（如 docs(规划): 添加可行性分析报告）可以让项目历史更清晰。Markdown 文件在 GitHub 上直接渲染，非常方便文档分享。

---

### 2.2 代码质量改进

本周在代码规范、重构、优化方面做了哪些工作？

- [ ] 遵循阿里巴巴Java编码规范（或前端编码规范）
- [ ] 对代码进行了重构，提升了可读性/可维护性
- [ ] 优化了数据库查询性能
- [ ] 优化了前端页面加载速度
- [ ] 添加了必要的单元测试
- [ ] 其他：_______完善项目文档体系______
 
- 具体说明：
编码规范：在 application.yaml 中使用了规范的缩进和注释，遵循 Spring Boot 官方推荐配置结构
重构：对 build.gradle 中的依赖按功能分组（Web、数据库、安全、测试），提升可读性
文档体系：按照软件工程标准流程组织文档，便于后续开发时查阅
---

## 三、遇到的问题与解决方案

### 3.1 技术问题记录

| 问题编号 | 问题描述 | 影响范围 | 解决状态 | 解决方案 | 参考链接 |
|---------|---------|---------|---------|---------|---------|
| P-3-1 | Gradle 下载依赖缓慢 | 项目构建 | ✅ 已解决 | 配置阿里云镜像仓库 |[ https://... ](https://developer.aliyun.com/mirror/gradle)|
| P-3-2 | MySQL 时区报错：ServerTimeZone 异常 | 数据库连接 | ✅ 已解决 | URL 添加 serverTimezone=Asia/Shanghai | - |
| P-3-3 | MyBatis 与 Spring Boot 4.04 版本兼容性问题|数据访问层	 |✅ 已解决 |使用 mybatis-spring-boot-starter:3.0.2 | |
| P-3-4 | | | | | |

**解决状态说明**：
- ✅ 已解决：问题已完全解决
- ⏳ 进行中：正在解决，尚未完成
- ❌ 未解决：暂时搁置，需要后续处理
- ❓ 待确认：需要请教老师或查阅资料
**具体说明**：（如果有上述改进，请简要说明）

---

### 3.2 典型问题深度分析（至少1个）

选择本周遇到的1个典型问题，进行深度分析：

**问题描述**：
问题描述：
MySQL 连接时报错 The server time zone value '�й���ʱ��' is unrecognized or represents more than one time zone。项目启动时无法连接数据库，Spring Boot 启动失败。

**问题分析**：
MySQL 8.0 之后对时区要求更严格，服务器默认时区是系统时区（如 CST），但 JDBC 驱动无法正确识别。这导致连接建立时抛出异常。

**解决过程**：
1. 尝试1：_______在 MySQL 命令行执行 SET GLOBAL time_zone = '+8:00'_________________（结果：临时生效，但重启后失效）
2. 尝试2：_________修改 MySQL 配置文件 my.cnf，添加 default-time-zone='+8:00'_______________（结果：有效，但需要重启数据库服务）
3. 最终方案：__________在 JDBC URL 中添加参数 serverTimezone=Asia/Shanghai，这是最简便、无侵入的解决方案____________

**经验总结**：

数据库连接问题优先检查 URL 参数，很多问题可以通过连接字符串解决
遇到时区问题时，明确指定 serverTimezone 比依赖数据库默认值更可靠
在 application.yaml 中把数据库配置写清楚，可以避免很多环境不一致导致的问题

---
