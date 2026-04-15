package cn.edu.swu.campus_buddy_matching_system.auth.controller;

import cn.edu.swu.campus_buddy_matching_system.auth.dto.LoginResponse;
import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserLoginRequest;
import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserRegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.auth.service.AuthService;
import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserResponse;
import cn.edu.swu.campus_buddy_matching_system.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    /**
     * 用户注册
     */
    @Operation(summary = "用户注册", description = "注册新用户并返回新用户ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "注册成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误或业务校验失败"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    @PostMapping("/register")
    public ApiResponse<Map<String, Long>> register(
            @Valid @RequestBody UserRegisterRequest request) {
        Long userId = authService.register(request);
        return ApiResponse.success("注册成功", Map.of("userId", userId));
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody UserLoginRequest request) {
        String token = authService.login(request);

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .username(request.getUsername())
                .build();

        return ApiResponse.success("登录成功", response);
    }

    /**
     * 获取当前用户信息
     */
    @Operation(summary = "获取当前用户信息", description = "根据请求头中的 userId 获取当前用户信息；若未传入，则默认查询ID为1的用户")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "用户不存在或参数错误")
    })
    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser(
            @Parameter(description = "当前用户ID，可选；不传默认取1")
            @RequestHeader(value = "userId", required = false) Long userId) {
        return ApiResponse.success("查询成功", userService.getCurrentUser(userId));
    }
}