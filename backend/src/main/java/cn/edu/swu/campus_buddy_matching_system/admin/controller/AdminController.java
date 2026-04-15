package cn.edu.swu.campus_buddy_matching_system.admin.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员接口
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Operation(summary = "管理员测试接口", description = "只有管理员可以访问")
    @GetMapping("/hello")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> hello() {
        return ApiResponse.success("访问成功", "hello admin");
    }

    @Operation(summary = "权限测试接口", description = "需要 user:read 权限")
    @GetMapping("/permission-test")
    @PreAuthorize("hasAuthority('user:read')")
    public ApiResponse<String> permissionTest() {
        return ApiResponse.success("访问成功", "you have user:read authority");
    }
}