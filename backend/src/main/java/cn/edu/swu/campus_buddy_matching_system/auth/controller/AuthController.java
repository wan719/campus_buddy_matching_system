package cn.edu.swu.campus_buddy_matching_system.auth.controller;

import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserRegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.auth.service.AuthService;
import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ApiResponse<Map<String, Long>> register(
            @Valid @RequestBody UserRegisterRequest request) {

        Long userId = authService.register(request);
        return ApiResponse.success("注册成功", Map.of("userId", userId));
    }
}