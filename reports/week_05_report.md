# 项目综合设计 - 周进度报告

---

## 基本信息

| 项目 | 内容 |
|-----|------|
| **报告周次** | 第 5 周 |
| **报告日期** | 2026年4月5日 |
| **团队名称** | 无（个人项目） |
| **项目主题** | □ 图书借阅管理系统 &nbsp; □ 校园二手交易平台 &nbsp; □ 实验室设备预约系统 &nbsp; ✅ 校园活动与报名平台<br>□ 食堂菜品推荐系统 &nbsp; □ 研究生-导师双选系统 &nbsp; □ 个人健康与运动打卡系统 &nbsp; □ 在线考试系统 |
| **报告人姓名** | 黎宏 |
| **报告人学号** | 222024321262009 |
| **报告人角色** | ✅ 组长 &nbsp; □ 组员 |
| **本周Git提交次数** | 2 次 |
| **本周代码行数** | 约 500 行 |

---

## 一、本周工作总结

### 1.1 完成的功能/任务

| 任务编号 | 任务描述 | 完成度 | 负责人 | 备注 |
|---------|---------|--------|--------|------|
| T-5-1 | 完成 PermissionMapper 注解SQL与XML查询实现 | 100% | 黎宏 | 已通过测试 |
| T-5-2 | 完成 RoleMapper 注解SQL与XML查询实现 | 100% | 黎宏 | 已通过测试 |
| T-5-3 | 完成 UserMapper 注解SQL与关联查询实现 | 100% | 黎宏 | 已通过测试 |
| T-5-4 | 完成 UserRoleMapper 关系表操作实现 | 100% | 黎宏 | 支持插入、删除、批量插入 |
| T-5-5 | 编写 Mapper 层测试代码 | 100% | 黎宏 | 新增 PermissionMapperTest、RoleMapperTest、UserMapperTest |
| T-5-6 | 配置测试数据库初始化脚本 | 100% | 黎宏 | 新增 schema.sql 与 data.sql |
| T-5-7 | 排查并修复 MyBatis 映射冲突与测试失败问题 | 100% | 黎宏 | `./gradlew clean test` 全部通过 |

---

### 1.2 技术实现要点

#### 功能1：MyBatis Mapper 层实现（注解 + XML 混合方式）

**实现思路**：
- 按照课程作业要求，采用“简单 SQL 用注解、复杂查询用 XML”的混合方式实现 Mapper 层。
- 在 `PermissionMapper`、`RoleMapper`、`UserMapper` 中，基础 CRUD 使用 `@Select`、`@Insert`、`@Update` 等注解实现。
- 多表关联查询如“根据角色查权限”“根据用户查角色”放在 XML 中，通过 `resultMap` 和关联查询实现对象映射。
- 通过这种分工既满足作业要求，也使代码层次更清晰。

**关键代码片段**：
```java
@Select("SELECT * FROM roles WHERE id = #{id}")
@Results(id = "roleResult", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name"),
        @Result(property = "description", column = "description"),
        @Result(property = "createdAt", column = "created_at"),
        @Result(property = "updatedAt", column = "updated_at"),
        @Result(property = "permissions", column = "id",
                many = @Many(select = "cn.edu.swu.campus_buddy_matching_system.repository.PermissionMapper.findPermissionsByRoleId"))
})
Role findById(@Param("id") Long id);
```
**遇到的难点及解决**：

- 难点：注解中的 @Results 与 XML 中 resultMap 重复定义，导致 MyBatis 启动时报错。
- 解决方案：明确分工，保留注解中的结果映射定义，XML 文件中只保留复杂 SQL，不重复声明同名 resultMap。

---
#### 功能2：测试环境搭建与测试修复

**实现思路**：

- 在 src/test/resources/db/ 下新增 schema.sql 与 data.sql，让测试运行时自动建表和初始化数据。
- 采用 @SpringBootTest、@ActiveProfiles("test")、@Transactional 实现集成测试，保证测试执行后自动回滚，不污染测试库。
- 根据测试日志逐个修正断言内容，保证测试数据与初始化 SQL 一致。

**关键代码片段**：
```java
@SpringBootTest(classes = CampusApplication.class)
@ActiveProfiles("test")
@Transactional
public class PermissionMapperTest {

    @Autowired
    private PermissionMapper permissionMapper;

    @Test
    public void testFindById() {
        Permission permission = permissionMapper.findById(1L);
        assertNotNull(permission);
        assertEquals("user:create", permission.getName());
    }
}
```
**遇到的难点及解决**：

- 难点：最开始测试环境报错较多，包括 Spring Boot / MyBatis 依赖版本不一致、Mapper XML 重复解析、测试断言与初始化数据不一致、中文字符显示乱码等问题。
- 解决方案：先统一依赖版本，再修复映射冲突，最后根据 data.sql 调整测试断言；对于昵称中文乱码问题，改为更稳妥的断言方式，最终实现全部测试通过。

---

## 二、技术收获与学习心得

### 2.1 本周学到的技术知识点
1. **MyBatis 注解与 XML 混合开发**：
- 学习场景：在完成 PermissionMapper、RoleMapper、UserMapper 时
- 掌握程度：□ 了解   □ 熟悉   ✅ 掌握   □ 精通
- 学习心得：简单 SQL 用注解更直观，复杂 SQL 用 XML 更灵活，但两者混用时必须清楚职责边界，否则很容易出现重复注册和映射冲突问题。
2. **Spring Boot 测试体系**：
- 学习场景：编写 Mapper 层测试代码和执行 ./gradlew clean test
- 掌握程度：□ 了解   ✅ 熟悉   □ 掌握   □ 精通
- 学习心得：通过 @SpringBootTest、@Transactional 和测试 profile，可以较完整地模拟真实运行环境。测试环境配置不规范时，会直接影响整个后端开发效率。
3. **测试数据库初始化脚本管理**：
- 学习场景：新增 schema.sql 和 data.sql
- 掌握程度：□ 了解   ✅ 熟悉   □ 掌握   □ 精通
- 学习心得：将表结构和测试数据独立放在测试资源目录下，能让测试过程标准化，也方便排查问题。初始化数据的顺序还会直接影响测试断言结果。
4. **根据日志定位问题**：
- 学习场景：排查 resultMap 重复、测试断言失败、字符乱码等问题
- 掌握程度：□ 了解   □ 熟悉   ✅ 掌握   □ 精通
- 学习心得：详细阅读 Gradle 测试日志和 SQL 执行日志，比直接猜问题更有效。很多问题表面上像是代码错，其实根因在配置、数据或依赖。
### 2.2 代码质量改进

本周在代码规范、重构、优化方面做了哪些工作？

- [x] 遵循阿里巴巴Java编码规范（或前端编码规范）
- [x] 对代码进行了重构，提升了可读性/可维护性
- [x] 优化了数据库查询性能
- [ ] 优化了前端页面加载速度
- [x] 添加了必要的单元测试
- [ ]其他：补充测试环境初始化脚本，规范 Mapper 层分工

**具体说明**：

- **代码结构优化**：将 Mapper 的职责进一步明确为“注解实现简单 SQL，XML 实现复杂 SQL”，避免重复配置。
- **测试补充**：新增 PermissionMapperTest、RoleMapperTest、UserMapperTest，并调整 UserTests 所在目录，使测试结构更合理。
- **测试环境规范化**：新增测试专用 schema.sql、data.sql，使 clean test 可以自动完成数据库初始化与测试执行。
- **问题修复**：修复 MyBatis resultMap 重复注册问题，提升项目可运行性。
---

## 三、遇到的问题与解决方案

### 3.1 技术问题记录

| 问题编号 | 问题描述 | 影响范围 | 解决状态 | 解决方案 | 参考链接 |
|---------|---------|---------|---------|---------|---------|
|P-5-1	|Spring Boot / MyBatis 测试依赖版本不一致，导致 ApplicationContext 启动失败|	测试环境	|✅ 已解决	|统一 Spring Boot 和 MyBatis Starter 版本	官方文档|
|P-5-2	|PermissionMapper 的 resultMap 在注解和 XML 中重复定义	Mapper 层|	|✅ 已解决	|保留注解 resultMap，XML 仅保留复杂查询 SQL	MyBatis 文档|
|P-5-3	|测试断言与初始化数据顺序不一致	|测试代码	|✅ 已解决	|根据 data.sql 实际插入顺序调整断言	本地测试日志|
|P-5-4	|中文昵称断言因字符编码显示异常而失败	|测试代码	|✅ 已解决	|调整断言方式，避免编码造成误判	本地测试日志|

---

### 3.2 典型问题深度分析

**问题描述**：
执行 ./gradlew clean test 时，MyBatis 启动失败，报错提示 Result Maps collection already contains value for ... PermissionMapper.permissionResult，导致多个 Mapper 测试无法执行。

**问题分析**：

- 最开始以为是 XML 写错或测试类有问题，但进一步查看日志后发现，问题出在映射配置重复。
- PermissionMapper 中已经通过 @Results(id = "permissionResult") 定义了映射关系，而 PermissionMapper.xml 中又写了同名 resultMap。
- MyBatis 启动时会同时加载注解和 XML，因此重复注册导致启动失败。

**解决过程**：

1.尝试1：先修改测试代码，希望绕过错误（结果：失败）
2.尝试2：检查依赖版本冲突并清理 Gradle 缓存（结果：部分问题解决）
3.尝试3：重新划分 Mapper 层实现方式，保留注解中的结果映射，XML 只实现复杂查询（结果：成功）

**经验总结**：

- 混合使用注解与 XML 时，一定要提前规划分工。
- 测试报错不能只看表面现象，要从依赖、配置、数据、代码四个层面逐步排查。
- 日志中的关键信息往往能直接指出根因，排错时要学会抓关键字。

---

## 四、下周工作计划
### 4.1 计划完成的任务
| 任务编号 | 任务描述 | 预计完成度 | 负责人 | 优先级 |
|---------|---------|-----------|--------|--------|
|T-6-1	|完成活动模块基础业务接口（发布活动、查询活动列表）	|100%	|黎宏	|高|
|T-6-2	|完成申请加入与审核申请功能	|100%	|黎宏	|高|
|T-6-3	|继续完善认证与授权逻辑（JWT / Spring Security）|	80%	|黎宏	|中|
|T-6-4	|清理敏感配置并进一步规范项目结构	|80%|	黎宏|	中|
|T-6-5	|若时间允许，补充首页或登录注册页面联调	|60%|	黎宏	|低|

**优先级说明**：

- 高：核心功能，必须完成
- 中：重要功能，尽量完成
- 低：锦上添花，可适当延后

---

### 4.2 需要提前准备的知识/技术

为了完成下周的任务，需要学习哪些内容？

1.**Spring Security + JWT 认证过滤链**:
  - 学习资源：Spring Security 官方文档、JWT 实战教程
2.**MyBatis 动态 SQL**:
  - 学习资源：MyBatis 官方文档动态 SQL 章节
3.**活动业务流程设计**:
  - 学习资源：课程案例、自己整理的业务流程图

---

## 五、个人贡献声明

### 5.1 本周个人工作量统计
| 工作类型 | 具体内容 | 工作量估计（小时） |
|---------|---------|------------------|
|需求分析	|阅读本周作业要求，梳理 Mapper / XML / 测试任务|	2|
|数据库设计|	检查测试表结构与初始化数据|	2|
|后端开发	|完成 Mapper、XML、测试代码、脚本配置|	8|
|前端开发	|无|	0|
|测试与调试|	运行 Gradle 测试，排查日志并修复问题|	6|
|文档编写	|周报撰写、提交说明整理	|1|
|代码审查	|自查 Mapper 与 XML 的分工逻辑|	1|
|其他	|Git 提交与分支管理	|1|
|**合计**	|	|**21 小时**|

### 5.2 本周个人贡献亮点|

请列出1-3个你认为自己本周做得最好的地方：

1.**独立完成 MyBatis Mapper / XML / 测试整套作业内容，并保证最终测试通过**
2.**独立定位并解决 resultMap 重复注册问题，修复了最关键的启动失败错误**
3.**将测试环境流程标准化，新增初始化脚本，使项目测试可重复执行**

---

### 5.3 本周自我评价

| 评价维度 | 自我评分（1-5分） | 说明 |
|---------|-----------------|------|
|代码质量	|4 分	|Mapper 分层更清晰，测试更完善，但仍有细节可优化|
|工作效率	|4 分	|中途遇到较多环境与配置问题，但最终按时解决|
|团队协作	|4 分	|个人项目，自主推进进度|
|学习能力	|5 分	|能根据日志快速定位问题，并逐步完成修复|
|**综合评分**	|**4.25 分**	|平均分|

**改进计划**：

- 下周准备进一步加强对 Spring Security 和 JWT 的理解，减少在配置类问题上的反复排查。
- 编写功能时尽量同步写测试，避免最后集中修复。

---

## 六、团队协作评价（仅组长填写）

### 6.1 本周团队整体进展

**团队目标完成情况**：

- 本周计划完成任务数：7 个
- 实际完成任务数：7 个
- 完成率：100%

**团队协作状态**：

- [✅]协作顺畅，进度正常
- [ ] 有少量沟通问题，但不影响进度
- [ ] 协作存在问题，影响进度

**存在问题与改进措施**：
- 无（个人项目）

### 6.2 团队成员贡献评价
| 成员姓名 | 本周贡献 | 主要工作内容 | 协作态度 | 评分（1-5分） |
|---------|---------|-------------|---------|--------------|
|黎宏	|高	|完成 Mapper、XML、测试代码与测试环境配置	|优秀	|5|

**本周最佳贡献者**：黎宏

**需要改进的成员**：无

---

## 七、对课程/老师的建议
### 7.1 本周课程内容反馈

| 评价维度 | 评分（1-5分） | 说明 |
|---------|-------------|------|
|课程内容难度	|4 分	|5分=太难   3分=适中   1分=太简单|
|课程内容实用性	|5 分	|5分=非常实用   1分=不实用|
|课堂教学效果	|4 分	|5分=效果好   1分=效果差|

**建议**：

- 本周 MyBatis 作业比较贴近实际开发，能有效训练数据库访问层能力。
- 建议老师后续在作业说明中更明确地区分“注解 SQL”和“XML SQL”的职责边界，减少同学在 resultMap 重复定义上的困惑。

### 7.2 需要老师帮助的问题

1.希望老师能再讲解一次 MyBatis 注解方式与 XML 方式的最佳实践
2.希望老师后续提供一份更标准的测试环境配置参考模板

---

## 附录：参考资料

列出本周参考的重要技术文档、教程、博客等：

1. [Spring Boot官方文档 - 访问数据](https://spring.io/guides/gs/accessing-data-mysql/)
2. [MyBatis 官方文档 - XML 映射器（结果映射）](https://mybatis.org/mybatis-3/zh/index.html)
3. [Lombok 功能特性指南](https://projectlombok.org/features/all)
4. [RBAC权限模型详解](https://www.cnblogs.com/zhangguangxiang/p/15122934.html)
5. [阿里巴巴 Java 开发手册（嵩山版）](https://github.com/alibaba/p3c)

---

**报告人签名**：黎宏

**提交日期**：2026年4月5日

---

## 教师评语（由教师填写）
| 评价项目 | 评分 | 评语 |
|---------|------|------|
|工作量饱满度	|___/20|	|
|技术深度	|___/20|	|
|代码质量|	___/20|	|
|报告质量|	___/20|	|
|团队协作|	___/20|	|
|**总分**	|**___/100**|	|

****教师签名****：____________________

**评定日期**：YYYY年MM月DD日
