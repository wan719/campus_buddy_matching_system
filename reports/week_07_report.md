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
| **报告周次** | 第 7 周 |
| **报告日期** | 2026年04月14日 |
| **团队名称** | 校园搭子匹配系统 2.0 |
| **项目主题** | □ 图书借阅管理系统 &nbsp; □ 校园二手交易平台 &nbsp; □ 实验室设备预约系统 &nbsp; □ 校园活动与报名平台<br>□ 食堂菜品推荐系统 &nbsp; □ 研究生-导师双选系统 &nbsp; □ 个人健康与运动打卡系统 &nbsp; □ 在线考试系统<br>☑ 其他：校园搭子匹配系统 |
| **报告人姓名** | 黎宏 |
| **报告人学号** | 222024321262009 |
| **报告人角色** | □ 组长 &nbsp; ☑ 组员 |
| **本周Git提交次数** | 12 |
| **本周代码行数** | 约 620 行 |

---

## 一、本周工作总结

### 1.1 完成的功能/任务

| 任务编号 | 任务描述 | 完成度 | 负责人 | 备注 |
|---------|---------|--------|--------|------|
| T-7-1 | 完成 JWT 登录接口 `POST /api/auth/login` | 100% | 黎宏 | 已返回 Bearer Token |
| T-7-2 | 完成 `CustomUserDetails` 与 `CustomUserDetailsService` | 100% | 黎宏 | 用户、角色、权限可正常加载 |
| T-7-3 | 完成 `JwtTokenProvider`、`JwtAuthenticationFilter` | 100% | 黎宏 | 已接入过滤器链 |
| T-7-4 | 完成 401 未认证与 403 权限不足处理器 | 100% | 黎宏 | 返回统一 JSON 结构 |
| T-7-5 | 完成 `SecurityConfig` 的无状态会话与 JWT 鉴权配置 | 100% | 黎宏 | 非公开接口默认需要认证 |
| T-7-6 | 完成管理员接口 `/api/admin/hello` 的角色访问控制 | 100% | 黎宏 | 普通用户访问返回 403 |
| T-7-7 | 完成权限测试接口 `/api/test/user-read` | 100% | 黎宏 | 具备 `user:read` 权限时可访问 |
| T-7-8 | 修复 JJWT 版本 API 不兼容问题 | 100% | 黎宏 | 改为兼容当前依赖的写法 |
| T-7-9 | 修复 PowerShell 下接口测试与 JSON 传参问题 | 100% | 黎宏 | 已能稳定完成本地联调 |
| T-7-10 | 完成 JWT、Controller、Security 集成测试 | 100% | 黎宏 | 测试全部通过 |

**完成度说明**：
- `100%`：已完成并测试通过
- `80%`：基本完成，有细节待优化
- `50%`：完成一半，仍在开发中
- `20%`：刚起步，遇到问题较多

---

### 1.2 技术实现要点

#### 功能1：JWT 登录认证与过滤器鉴权

**实现思路**：
- 在原有 RBAC 项目基础上，新增登录接口，由 `AuthController -> AuthService -> AuthenticationManager` 完成用户名密码认证。
- 认证成功后，使用 `JwtTokenProvider` 生成 JWT，将用户名和 `authorities` 写入 token。
- 后续请求通过 `JwtAuthenticationFilter` 从 `Authorization: Bearer xxx` 请求头中提取 token，并将认证信息写入 `SecurityContextHolder`。
- 结合 Spring Security 的无状态会话模式，避免服务端保存 Session。

**关键代码片段**：
```java
@Override
public String login(UserLoginRequest request) {
    Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
    );

    Authentication authentication = authenticationManager.authenticate(authenticationToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    return jwtTokenProvider.generateToken(authentication);
}
```

**遇到的难点及解决**：
- 难点：JJWT 版本与示例代码 API 不一致，`subject()`、`verifyWith()` 等方法报错。
- 解决方案：根据当前项目依赖改为兼容写法，使用 `setSubject()` 与 `parserBuilder().setSigningKey(...).build().parseClaimsJws(...)` 完成生成与解析。

---

#### 功能2：基于角色与权限的接口访问控制

**实现思路**：
- 在 `SecurityConfig` 中配置 `/api/admin/**` 需要管理员角色访问。
- 开启 `@EnableMethodSecurity`，在控制器方法上使用 `@PreAuthorize("hasRole('ADMIN')")` 和 `@PreAuthorize("hasAuthority('user:read')")` 进行细粒度控制。
- 在 `CustomUserDetails` 中，将数据库中的角色和权限统一转换为 Spring Security 可识别的 `GrantedAuthority`。
- 补充 `JwtAuthenticationEntryPoint` 与 `JwtAccessDeniedHandler`，统一处理 401 和 403 场景。

**关键代码片段**：
```java
@GetMapping("/api/test/user-read")
@PreAuthorize("hasAuthority('user:read')")
public ApiResponse<String> userReadTest() {
    return ApiResponse.success("访问成功", "you have user:read authority");
}
```

**遇到的难点及解决**：
- 难点：最开始角色名前缀重复拼接，导致 `hasRole("ADMIN")` 无法匹配成功。
- 解决方案：确认数据库角色名已经是 `ROLE_ADMIN`、`ROLE_USER` 后，在 `CustomUserDetails` 中直接使用 `role.getName()` 作为角色 authority，避免出现 `ROLE_ROLE_ADMIN`。

---

## 二、技术收获与学习心得

### 2.1 本周学到的技术知识点

1. **Spring Security 认证链**：
   - 学习场景：在完成 JWT 登录接口和过滤器接入时
   - 掌握程度：☑ 掌握
   - 学习心得：这周真正把 `AuthenticationManager`、`UserDetailsService`、`SecurityContextHolder` 串起来后，我对 Spring Security 的认证流程有了更清晰的理解，不再只是停留在配置层面。

2. **JWT 令牌生成、解析与无状态会话**：
   - 学习场景：在实现 `JwtTokenProvider` 和 `JwtAuthenticationFilter` 时
   - 掌握程度：☑ 掌握
   - 学习心得：我理解了 JWT 不只是一个字符串，而是前后端分离项目里承载身份信息和权限信息的重要载体。也进一步理解了为什么前后端分离场景更适合无状态认证。

3. **基于角色与权限的授权控制**：
   - 学习场景：在实现 `/api/admin/**` 和 `/api/test/user-read` 时
   - 掌握程度：☑ 熟悉
   - 学习心得：通过 `hasRole` 和 `hasAuthority` 的对比实践，我更清楚了粗粒度角色控制和细粒度权限控制的区别，也更理解了 RBAC 在真实项目中的落地方式。

4. **测试分层设计**：
   - 学习场景：在编写 `JwtTokenProviderTest`、`AuthControllerTest`、`SecurityIntegrationTest` 时
   - 掌握程度：☑ 熟悉
   - 学习心得：我意识到不同层级的测试不应全部依赖 Spring 上下文。像 Controller 单测用 Mockito 更高效，而 Security 场景则适合集成测试来验证 401/403 流程。

---

### 2.2 代码质量改进

本周在代码规范、重构、优化方面做了哪些工作？

- [x] 遵循阿里巴巴Java编码规范（或前端编码规范）
- [x] 对代码进行了重构，提升了可读性/可维护性
- [ ] 优化了数据库查询性能
- [ ] 优化了前端页面加载速度
- [x] 添加了必要的单元测试
- [x] 其他：统一 JWT 认证结构、统一 401/403 返回格式、修复测试环境配置问题

**具体说明**：  
本周对认证授权相关代码进行了较完整的结构化整理。主要包括：将登录逻辑、用户详情加载逻辑、JWT 工具类与过滤器职责拆分清楚；在 `SecurityConfig` 中统一配置会话策略、异常处理与过滤器链；将 `AuthControllerTest` 调整为 Mockito 纯单元测试，避免无关基础设施影响测试；补充 `SecurityIntegrationTest` 以验证 401 和 403 的真实运行效果，从而提高了项目整体可维护性与可验证性。

---

## 三、遇到的问题与解决方案

### 3.1 技术问题记录

| 问题编号 | 问题描述 | 影响范围 | 解决状态 | 解决方案 | 参考链接 |
|---------|---------|---------|---------|---------|---------|
| P-7-1 | JJWT API 方法 `subject()`、`verifyWith()` 报错 | JWT 模块 | ✅ 已解决 | 改为兼容当前依赖的旧版 API 写法 | JJWT 文档 |
| P-7-2 | 登录接口返回 500，而不是 401 | 认证模块 | ✅ 已解决 | 在 `AuthController.login()` 中捕获 `BadCredentialsException` 并统一返回 401 | Spring Security 文档 |
| P-7-3 | Windows PowerShell 下 `curl` 参数报错 | 本地联调 | ✅ 已解决 | 改用 `Invoke-RestMethod` 或 `curl.exe` 测试接口 | PowerShell 文档 |
| P-7-4 | 普通用户访问权限测试接口返回 403 | 授权模块 | ✅ 已解决 | 将权限测试接口移出 `/api/admin/**` 路径，避免先被管理员 URL 规则拦截 | Spring Security 文档 |
| P-7-5 | `@WebMvcTest` 启动时加载 Security/MyBatis 失败 | 测试模块 | ✅ 已解决 | 将 `AuthControllerTest` 改为 Mockito 纯单元测试 | Spring Boot Test 文档 |
| P-7-6 | 测试环境中初始用户密码与预期不一致 | 测试模块 | ✅ 已解决 | 在集成测试中先注册用户再登录，避免依赖固定初始化密码 | 课堂实践 |
| P-7-7 | 中文信息在 PowerShell/控制台中乱码 | 展示层 | ⏳ 进行中 | 初步通过 `chcp 65001` 改善终端显示，后续继续排查编码统一问题 | - |

**解决状态说明**：
- ✅ 已解决：问题已完全解决
- ⏳ 进行中：正在解决，尚未完成
- ❌ 未解决：暂时搁置，需要后续处理
- ❓ 待确认：需要请教老师或查阅资料

---

### 3.2 典型问题深度分析

**问题描述**：  
在编写 `AuthControllerTest` 时，测试并不是简单地验证控制器逻辑，而是在启动 `@WebMvcTest` 上下文阶段直接失败。最初报错是 `JwtTokenProvider` bean 找不到，后续又出现 `permissionMapper` 缺少 `sqlSessionFactory` 的异常，导致测试无法运行。

**问题分析**：  
我最开始把 `AuthControllerTest` 写成了依赖 Spring MVC 测试上下文的形式，但项目中 `SecurityConfig`、`JwtAuthenticationFilter`、MyBatis Mapper 等组件会被间接加载。由于 `@WebMvcTest` 只适合 MVC 层，无法完整提供 Security 与 MyBatis 所需的全部基础设施，结果造成测试上下文过重、依赖不完整，最终启动失败。

**解决过程**：
1. 尝试1：为缺失的安全组件补 `@MockBean`（结果：部分缓解，但仍失败）  
2. 尝试2：继续为安全链和 Mapper 相关依赖补 mock（结果：上下文仍然复杂，收益不高）  
3. 最终方案：将 `AuthControllerTest` 改为 Mockito 纯单元测试，只验证控制器方法本身的返回结构，不再依赖 Spring 容器（结果：成功）

**经验总结**：  
这次问题让我认识到，测试设计要和目标匹配。不是所有测试都应该起 Spring 上下文。对于控制器中较简单的逻辑，纯单元测试更轻量、更稳定；而像 401/403 这种完整安全链路，则更适合集成测试。后续写测试时，我会优先考虑测试边界，而不是一上来就用最重的测试方式。

---

## 四、下周工作计划

### 4.1 计划完成的任务

| 任务编号 | 任务描述 | 预计完成度 | 负责人 | 优先级 |
|---------|---------|-----------|--------|--------|
| T-8-1 | 继续完善基于 JWT 的当前用户获取与用户上下文传递 | 100% | 黎宏 | 高 |
| T-8-2 | 处理数据库与控制台中文乱码问题，统一编码配置 | 100% | 黎宏 | 中 |
| T-8-3 | 补充更多异常场景、边界条件和覆盖率统计 | 100% | 黎宏 | 中 |
| T-8-4 | 整理安全模块文档、接口说明和演示材料 | 100% | 黎宏 | 高 |

**优先级说明**：
- 高：核心功能，必须完成
- 中：重要功能，尽量完成
- 低：锦上添花，可适当延后

---

### 4.2 需要提前准备的知识/技术

1. Spring Security 中基于 JWT 获取当前用户上下文的标准实现  
   - 学习资源：Spring Security 官方文档、JWT 实战教程

2. JaCoCo 测试覆盖率统计与测试报告生成  
   - 学习资源：Gradle JaCoCo 插件文档

---

## 五、个人贡献声明（重要）

### 5.1 本周个人工作量统计

| 工作类型 | 具体内容 | 工作量估计（小时） |
|---------|---------|------------------|
| 需求分析 | 阅读第7周作业要求，拆解 JWT、过滤器、鉴权测试任务 | 2 |
| 数据库设计 | 核对 RBAC 表结构与角色权限关系是否支持 JWT 授权 | 1 |
| 后端开发 | 实现登录接口、JWT 工具类、过滤器、异常处理器、权限测试接口 | 13 |
| 前端开发 | 无 | 0 |
| 测试与调试 | 本地联调、PowerShell 测试、修复 Security/JWT/Test 问题 | 8 |
| 文档编写 | 周报整理、测试记录整理、提交检查清单整理 | 2 |
| 代码审查 | 检查角色前缀、接口放行、统一响应格式与测试代码结构 | 2 |
| 其他 | 查阅 Spring Security、JJWT、Spring Boot Test 文档 | 2 |
| **合计** | | **30 小时** |

---

### 5.2 本周个人贡献亮点

1. 独立完成了基于 Spring Security + JWT 的登录认证、过滤器鉴权与统一异常处理。
2. 独立完成了角色控制与权限控制的落地验证，成功打通 401、403、权限 200 三类典型场景。
3. 独立解决了 JJWT 版本兼容、PowerShell 接口测试、测试上下文过重等多个实际开发问题，并完成测试收尾。

---

### 5.3 本周自我评价

| 评价维度 | 自我评分（1-5分） | 说明 |
|---------|-----------------|------|
| 代码质量 | 4 分 | 结构较清晰，职责划分比上周更明确，后续可继续完善注释与配置抽取 |
| 工作效率 | 5 分 | 本周目标功能、联调验证和测试收尾均已完成 |
| 团队协作 | 3 分 | 本周依旧以个人独立推进后端安全模块为主，协作内容较少 |
| 学习能力 | 5 分 | 能较快理解并解决 Spring Security、JWT、测试分层中的实际问题 |
| **综合评分** | **4 分** | 平均分 |

**改进计划**：  
下周我会继续提升对异常边界、编码统一和测试覆盖率的关注，不只是实现“能跑通”，还要进一步提升工程完整性和展示效果。

---

## 六、团队协作评价（仅组长填写）

### 6.1 本周团队整体进展

**团队目标完成情况**：
- 本周计划完成任务数：____ 个
- 实际完成任务数：____ 个
- 完成率：_____ %

**团队协作状态**：
- [ ] 协作顺畅，进度正常
- [ ] 有少量沟通问题，但不影响进度
- [ ] 协作存在问题，影响进度

**存在问题与改进措施**：
- 

---

### 6.2 团队成员贡献评价

| 成员姓名 | 本周贡献 | 主要工作内容 | 协作态度 | 评分（1-5分） |
|---------|---------|-------------|---------|--------------|
| 张三 | 高/中/低 | | 优秀/良好/一般 | |
| 李四 | 高/中/低 | | 优秀/良好/一般 | |
| 王五 | 高/中/低 | | 优秀/良好/一般 | |

**本周最佳贡献者**：____________________

**需要改进的成员**：____________________（请说明原因和改进建议）

---

## 七、对课程/老师的建议

### 7.1 本周课程内容反馈

| 评价维度 | 评分（1-5分） | 说明 |
|---------|-------------|------|
| 课程内容难度 | 4 分 | JWT 与 Spring Security 部分有一定难度，但结合项目实战后理解更深 |
| 课程内容实用性 | 5 分 | 认证与授权是前后端分离项目中的核心能力，实用性很强 |
| 课堂教学效果 | 4 分 | 内容较系统，如果能补充更多“常见报错排查案例”会更好 |

**建议**：  
本周的 JWT 安全机制内容和真实项目结合度很高。建议后续课程增加更多“已有项目安全改造”的案例，尤其是从接口放行、登录认证、过滤器接入、权限测试到单元测试的完整闭环，这对学生理解工程化开发非常有帮助。

---

### 7.2 需要老师帮助的问题

1. 当前项目仍存在控制台与 SQL 初始化数据的中文乱码问题，希望了解课程推荐的统一编码配置方式。
2. 后续如果继续扩展 JWT，希望了解课程推荐的“获取当前登录用户信息”与“刷新 token”标准实现方案。

---

## 附录：参考资料

1. [Spring Security 官方文档](https://docs.spring.io/spring-security/reference/)
2. [JJWT 官方文档](https://github.com/jwtk/jjwt)
3. [Spring Boot Testing 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
4. 第7周作业文档：JWT 安全机制实现

---

**报告人签名**：晚

**提交日期**：2026年04月14日

---

## 教师评语（由教师填写）

| 评价项目 | 评分 | 评语 |
|---------|------|------|
| 工作量饱满度 | ___/20 | |
| 技术深度 | ___/20 | |
| 代码质量 | ___/20 | |
| 报告质量 | ___/20 | |
| 团队协作 | ___/20 | |
| **总分** | **___/100** | |

**教师签名**：____________________

**评定日期**：YYYY年MM月DD日
