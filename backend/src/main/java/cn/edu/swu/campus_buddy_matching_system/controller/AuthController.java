package cn.edu.swu.campus_buddy_matching_system.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.LoginRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserVO;
import cn.edu.swu.campus_buddy_matching_system.common.utils.JwtUtil;
import cn.edu.swu.campus_buddy_matching_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ApiResponse<Map<String, String>> register(@Valid @RequestBody RegisterRequest request) {
        String token = userService.register(request);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return ApiResponse.success(data);
    }

    @PostMapping("/login")
    public ApiResponse<Map<String, String>> login(@Valid @RequestBody LoginRequest request) {
        String token = userService.login(request);
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        return ApiResponse.success(data);
    }

    @GetMapping("/profile")
    public ApiResponse<UserVO> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ApiResponse.error(401, "未提供有效的认证令牌");
        }
        
        try {
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                return ApiResponse.error(401, "令牌无效或已过期");
            }
            
            Long userId = jwtUtil.getUserIdFromToken(token);
            UserVO userVO = userService.getProfile(userId);
            return ApiResponse.success(userVO);
        } catch (Exception e) {
            return ApiResponse.error(401, "认证失败：" + e.getMessage());
        }
    }
    
    @PostMapping("/logout")
    public ApiResponse<String> logout() {
        // 前端清除 token 即可，后端可以不做处理
        return ApiResponse.success("退出成功");
    }
}