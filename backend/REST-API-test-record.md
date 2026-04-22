# REST API 测试记录（作业模板）

## 项目信息
- 项目：Campus Buddy Matching System
- 模块：backend
- 测试日期：`____-__-__`
- 测试人：`________`
- 环境：`本地 / 测试机`

## 测试环境
- JDK：`17`
- Spring Boot：`3.1.5`
- 数据库：`MySQL 8+`
- 测试库：`campus_buddy_matching_system_test`

## 接口测试记录

| 编号 | 接口 | 方法 | 测试场景 | 请求参数摘要 | 期望结果 | 实际结果 | 结论 |
|---|---|---|---|---|---|---|---|
| 1 | `/api/auth/register` | POST | 正常注册 | 合法学号/用户名/邮箱 | 200 + userId |  |  |
| 2 | `/api/auth/login` | POST | 正常登录 | admin/password123 | 200 + token |  |  |
| 3 | `/api/auth/me` | GET | 查询当前用户 | Header: userId=1 | 200 + 用户信息 |  |  |
| 4 | `/api/admin/hello` | GET | 无 token | 无 | 401 |  |  |
| 5 | `/api/admin/hello` | GET | 普通用户 token | Bearer user token | 403 |  |  |
| 6 | `/api/admin/hello` | GET | 管理员 token | Bearer admin token | 200 |  |  |
| 7 | `/api/admin/hello` | GET | 无效 token | Bearer invalid token | 401 |  |  |
| 8 | `/v3/api-docs` | GET | OpenAPI 文档可访问 | 无 | 200 |  |  |
| 9 | `/swagger-ui/index.html` | GET | Swagger UI 可访问 | 无 | 200 |  |  |

## 自动化测试运行记录

```bash
./gradlew test --tests cn.edu.swu.campus_buddy_matching_system.security.SwaggerIntegrationTest --tests cn.edu.swu.campus_buddy_matching_system.security.SecurityIntegrationTest
```

- 执行结果：`PASS / FAIL`
- 失败用例：`无 / 列出用例名`
- 报错摘要：`________`

## 问题与修复记录

| 问题 | 影响 | 修复措施 | 修复后验证 |
|---|---|---|---|
|  |  |  |  |

## 结论
- 当前接口稳定性：`________`
- 文档完整性：`________`
- 后续优化建议：`________`
