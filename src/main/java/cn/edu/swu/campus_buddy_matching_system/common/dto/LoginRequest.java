package cn.edu.swu.campus_buddy_matching_system.common.dto;

import lombok.*;
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginRequest {
    private String studentId;
    private String password;
}
