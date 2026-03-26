package cn.edu.swu.campus_buddy_matching_system.model.entity;

import java.time.LocalDateTime;

import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String studentId;
    private String passwordHash;
    private String nickname;
    private String avatarUrl;
    private String college;
    private String grade;
    private String tags;//JSON 字符串，后续可改为 List<String> + TypeHandler
    private String creditScore;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
