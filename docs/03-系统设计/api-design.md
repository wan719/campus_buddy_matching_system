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
