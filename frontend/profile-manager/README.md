# 个人信息管理器

一个基于 Node.js 原生开发服务器 + ES Modules 的前端练习项目。

## 环境要求

- Node.js 18+（建议）

## 启动方式

1. 进入项目目录：
   ```bash
   cd frontend/project
   ```
2. 启动开发服务器：
   ```bash
   npm start
   ```

## 访问应用

- 打开浏览器访问：`http://localhost:3000`

## 功能说明

- 用户列表展示（模块化渲染）
- 删除用户
- 新增用户（表单校验）
- 编辑用户（数据回显 + 保存修改）
- localStorage 数据持久化（刷新不丢失）
- 重置为初始数据
- WebSocket 热加载（修改文件自动刷新）
