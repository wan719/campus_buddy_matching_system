package cn.edu.swu.campus_buddy_matching_system.permission.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;
import cn.edu.swu.campus_buddy_matching_system.permission.service.PermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 获取权限列表
     */
    @Operation(summary = "获取权限列表", description = "查询所有权限")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAllPermissions() {
        return ApiResponse.success("查询成功", permissionService.getAllPermissions());
    }

    /**
     * 根据ID获取权限详情
     */
    @Operation(summary = "获取权限详情", description = "根据ID获取权限详细信息")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "权限不存在")
    })
    @GetMapping("/{id}")
    public ApiResponse<PermissionResponse> getPermissionById(
            @Parameter(description = "权限ID") @PathVariable Long id) {
        return ApiResponse.success("查询成功", permissionService.getPermissionById(id));
    }
}