# 登录接口性能测试说明（`/api/auth/login`）

## 测试目标
- 对 `POST /api/auth/login` 做基础吞吐与响应耗时验证。
- 记录可提交作业的性能结果（并发下的请求能力与延迟表现）。

## 接口与请求体
- 接口：`POST /api/auth/login`
- 请求头：`Content-Type: application/json`
- 请求体字段：
  - `username`（String，必填）
  - `password`（String，必填）
- 示例请求体文件：`backend/login.json`

## 测试工具
- Apache Bench（`ab`）
- PowerShell（Windows 建议使用 `powershell -NoProfile`，避免本地 profile 干扰）

## 命令示例

```bash
# 先启动后端服务（另一个终端）
./gradlew bootRun
```

```bash
# Windows 一键脚本（推荐）
powershell -NoProfile -ExecutionPolicy Bypass -File .\run-ab-test.ps1
# 或者
.\run-ab-test.cmd
```

```bash
# 基础压测：总请求 500，并发 20
ab -n 500 -c 20 -p login.json -T application/json http://127.0.0.1:8080/api/auth/login
```

```bash
# 提高并发：总请求 2000，并发 100
ab -n 2000 -c 100 -p login.json -T application/json http://127.0.0.1:8080/api/auth/login
```

## 指标说明
- `Requests per second`：每秒处理请求数（吞吐能力，越高越好）。
- `Time per request`：单请求平均耗时（ms，越低越好）。
- `Transfer rate`：传输速率（KB/s，反映网络与响应体传输效率）。

## 结果填写模板

| 场景 | 命令参数 | Requests per second | Time per request (ms) | Transfer rate (KB/s) | 失败请求 |
|---|---|---:|---:|---:|---:|
| 基础 | `-n 500 -c 20` |  |  |  |  |
| 中压 | `-n 1000 -c 50` |  |  |  |  |
| 高压 | `-n 2000 -c 100` |  |  |  |  |

## 简短分析模板

```text
在并发从 <X> 提升到 <Y> 时，RPS 从 <A> 提升/下降到 <B>，
平均请求耗时从 <C>ms 变为 <D>ms，失败请求数为 <E>。
初步判断系统在 <并发区间> 内表现 <稳定/出现瓶颈>，
瓶颈可能位于 <数据库连接池/认证逻辑/网络>，后续可通过 <优化方向> 进一步验证。
```
