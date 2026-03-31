# 校园搭子匹配系统 2.0

## 项目简介
本项目旨在帮助在校大学生找到学习、运动、吃饭等“搭子”，建立真实的校园社交连接。

## 技术栈
- Spring Boot 4.0.4
- Spring Security + JWT
- MyBatis
- MySQL 8.0+
- Gradle 9.3.1

## 文档导航
- [系统规划](./docs/01-系统规划/)

## 开发进度
- [x] 阶段一：系统规划
- [x] 阶段二：系统分析
- [x] 阶段三：系统设计
- [ ] 阶段四：系统实施

## 环境配置（Windows）
1. 已将 Gradle Wrapper 分发包地址配置为本地文件：
   - `D:\Downloads\gradle-9.3.1-all.zip`
2. 请确保本地 MySQL 已启动，并可使用以下账号连接：
   - 用户名：`root`
   - 密码：`20060719li`
3. 运行环境数据库（启动时自动初始化）：
   - `campus_buddy_matching_system`
4. 测试环境数据库（测试时自动初始化）：
   - `campus_buddy_matching_system_test`

## 快速启动
```bash
# 克隆项目
git clone https://github.com/wan719/campus_buddy_matching_system.git

# 进入后端目录
cd campus_buddy_matching_system/backend

# 运行项目（使用 MySQL）
./gradlew bootRun

# 执行测试（使用 MySQL）
./gradlew test
```
