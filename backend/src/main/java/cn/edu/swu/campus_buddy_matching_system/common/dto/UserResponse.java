package cn.edu.swu.campus_buddy_matching_system.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户响应DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long id;

    private String studentId;

    private String username;

    private String email;

    private String nickname;

    private Boolean enabled;

    private Integer creditScore;

    private LocalDateTime createdAt;

    private List<RoleResponse> roles;
}