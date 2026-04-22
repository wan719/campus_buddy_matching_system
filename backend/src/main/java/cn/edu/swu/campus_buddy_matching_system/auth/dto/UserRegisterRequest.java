package cn.edu.swu.campus_buddy_matching_system.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户注册请求
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户注册请求参数")
public class UserRegisterRequest {

    @Schema(description = "学号", example = "20240001", required = true)
    @NotBlank(message = "学号不能为空")
    private String studentId;

    @Schema(description = "用户名", example = "newuser", required = true)
    @NotBlank(message = "用户名不能为空")
    private String username;

    @Schema(description = "密码", example = "123456", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "确认密码", example = "123456", required = true)
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;

    @Schema(description = "邮箱", example = "user@swu.edu.cn", required = true)
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "昵称", example = "小明", required = true)
    @NotBlank(message = "昵称不能为空")
    private String nickname;
}