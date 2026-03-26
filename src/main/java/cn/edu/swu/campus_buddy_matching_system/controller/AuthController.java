package cn.edu.swu.campus_buddy_matching_system.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.LoginRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserVO;
import cn.edu.swu.campus_buddy_matching_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<Map<String, String>> register(@RequestBody RegisterRequest request) {
        String token = userService.register(request);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return ApiResponse.success(data);
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@RequestBody LoginRequest request) {
        String token = userService.login(request);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return ApiResponse.success(data);
    }

    @GetMapping("/profile")
    public ApiResponse<UserVO> getProfile(@RequestHeader("Authorization") String authHeader) {
        // TODO: 从 token 中解析 userId（后续 Security 配置完成后简化）
        // 临时方案：前端传 token，这里简单处理
        // 实际应使用 SecurityContextHolder
        return ApiResponse.success(null);
    }
}