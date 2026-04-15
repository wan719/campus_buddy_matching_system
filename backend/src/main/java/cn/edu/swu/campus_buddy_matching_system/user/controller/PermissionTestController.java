package cn.edu.swu.campus_buddy_matching_system.user.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 权限测试控制器
 */
@RestController
public class PermissionTestController {

    @Operation(summary = "权限测试接口", description = "需要 user:read 权限")
    @GetMapping("/api/test/user-read")
    @PreAuthorize("hasAuthority('user:read')")
    public ApiResponse<String> userReadTest() {
        return ApiResponse.success("访问成功", "you have user:read authority");
    }
}