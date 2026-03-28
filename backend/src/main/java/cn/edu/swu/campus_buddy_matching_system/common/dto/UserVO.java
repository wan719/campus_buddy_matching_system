package cn.edu.swu.campus_buddy_matching_system.common.dto;

import lombok.*;
/**
 * 用户视图对象(UserVo)类
 * 用于封装用户相关的数据，通常用于前后端数据交互
 * 使用了Lombok注解简化代码
 * @Data：自动生成 getter、setter、toString、equals 和 hashCode 方法
 * @AllArgsConstructor：生成一个包含所有字段的构造函数
 * @NoArgsConstructor：生成一个无参构造函数
 * @Builder：生成一个建造者模式的构建器
 * 返回给前端的用户信息，不包含敏感信息如密码等
 */
@AllArgsConstructor // 全参构造函数注解
@NoArgsConstructor
@Builder
@Data
public class UserVO {
    private Long id;
    private String studentId;
    private String nickname;
    private String avatarUrl;
    private String creditScore;
}
