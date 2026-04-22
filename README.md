# 校园搭子匹配系统（Campus Buddy Matching System）

基于 Spring Boot + Spring Security + JWT + MyBatis 的校园社交匹配后端项目。

## 仓库结构

- `backend/`：后端服务（本项目主要代码）
- `docs/`：课程作业文档与设计资料

## 后端技术栈

- Java 17
- Spring Boot 3.1.5
- Spring Security
- JWT（jjwt）
- MyBatis
- MySQL 8+
- SpringDoc OpenAPI（Swagger UI）
- Gradle Wrapper

## 快速开始（后端）

```bash
cd backend
./gradlew bootRun
```

默认启动后端服务后可访问：

- Swagger UI：`http://localhost:8080/swagger-ui.html`
- OpenAPI JSON：`http://localhost:8080/v3/api-docs`

## 测试

```bash
cd backend
./gradlew test
```

指定测试类示例：

```bash
./gradlew test --tests cn.edu.swu.campus_buddy_matching_system.security.SwaggerIntegrationTest
./gradlew test --tests cn.edu.swu.campus_buddy_matching_system.security.SecurityIntegrationTest
```

## 测试数据库说明

测试环境默认使用 `backend/src/test/resources/application-test.yml` 中的数据源配置：

- 数据库：`campus_buddy_matching_system_test`
- 会在测试启动时自动执行 `db/schema.sql` 和 `db/data.sql`

## 登录性能测试（作业用）

已提供基础文件（位于 `backend/`）：

- `login.json`：登录请求示例（测试账号）
- `performance-test.md`：性能测试说明与结果模板
- `run-ab-test.ps1`：Windows 一键执行 `ab` 基础压测脚本

## 说明

- 本仓库优先用于课程开发与验证，建议在本地 MySQL 环境下运行。
- 若修改数据库账号/密码，请同步更新 `backend/src/main/resources/application.yaml` 与 `backend/src/test/resources/application-test.yml`。
