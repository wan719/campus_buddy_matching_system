# API 接口设计文档

## 基础信息

- **Base URL**: `/api/v1`
- **认证方式**: JWT (Header: `Authorization: Bearer <token>`)
- **响应格式**: JSON

## 通用响应结构

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```
## 用户模块
### 1. 用户注册
POST /auth/register

请求体:
```bash
json
{
  "studentId": "2024001",
  "password": "123456",
  "nickname": "小明"
}
响应: 返回用户ID
```
### 2. 用户登录
POST /auth/login

请求体:
```bash
json
{
  "studentId": "2024001",
  "password": "123456"
}
响应:

json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "userId": 1,
  "nickname": "小明"
}
```
### 3. 获取个人信息
GET /user/profile

Header: Authorization: Bearer <token>

响应:
```bash
json
{
  "id": 1,
  "studentId": "2024001",
  "nickname": "小明",
  "avatarUrl": "/images/default.png",
  "college": "计算机学院",
  "creditScore": 100,
  "tags": ["学霸", "考研党"]
}
```
---
## 活动模块
### 4. 发布活动
POST /activity

Header: Authorization: Bearer <token>

请求体:
```bash
json
{
  "category": "study",
  "title": "图书馆自习搭子",
  "detail": "明天下午2点图书馆3楼，找考研搭子",
  "location": "图书馆3楼",
  "appointmentTime": "2026-03-25 14:00:00",
  "maxMembers": 4
}
```
### 5. 获取活动列表
GET /activity/list?page=1&size=10&category=study&location=图书馆

参数:
```bash
page: 页码

size: 每页数量

category: 活动类型

location: 地点（模糊搜索）

响应:

json
{
  "total": 50,
  "list": [
    {
      "id": 1,
      "title": "图书馆自习搭子",
      "category": "study",
      "location": "图书馆3楼",
      "currentMembers": 2,
      "maxMembers": 4,
      "status": "open"
    }
  ]
}
```
### 6. 获取活动详情
GET /activity/{id}

### 7. 申请加入
POST /activity/{id}/apply

Header: Authorization: Bearer <token>

请求体:
```bash
json
{
  "message": "我也是考研党，一起加油！"
}
```
### 8. 审核申请
PUT /activity/{id}/application/{applicationId}

请求体:
```bash
json
{
  "status": "approved",  // approved / rejected
  "reason": "欢迎加入！"  // 拒绝时必填
}
```
---
## 评价模块
### 9. 发布评价
POST /review
```bash
请求体:

json
{
  "activityId": 1,
  "toUserId": 2,
  "rating": 5,
  "comment": "很靠谱的搭子！"
}
```
### 10. 获取用户信用分
GET /user/{userId}/credit

---
## 错误码说明
|错误码|	说明|
|------|------|
|400	|参数错误|
|401	|未认证/Token失效|
|403	|无权限|
|404	|资源不存在|
|409	|冲突（重复申请等）|
|500	|服务器内部错误|
