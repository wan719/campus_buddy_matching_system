package cn.edu.swu.campus_buddy_matching_system.role.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RoleResponse;
import cn.edu.swu.campus_buddy_matching_system.role.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理控制器
 */
@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 获取角色列表
     */
    @Operation(summary = "获取角色列表", description = "查询所有角色")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功")
    })
    @GetMapping
    public ApiResponse<List<RoleResponse>> getAllRoles() {
        return ApiResponse.success("查询成功", roleService.getAllRoles());
    }

    /**
     * 获取角色详情
     */
    @Operation(summary = "获取角色详情", description = "根据ID获取角色详细信息（包含权限）")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "角色不存在")
    })
    @GetMapping("/{id}")
    public ApiResponse<RoleResponse> getRoleById(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        return ApiResponse.success("查询成功", roleService.getRoleById(id));
    }

    /**
     * 获取指定角色的权限列表
     */
    @Operation(summary = "获取角色的权限列表", description = "根据角色ID查询该角色拥有的所有权限")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "角色不存在")
    })
    @GetMapping("/{id}/permissions")
    public ApiResponse<List<PermissionResponse>> getPermissionsByRoleId(
            @Parameter(description = "角色ID") @PathVariable Long id) {
        return ApiResponse.success("查询成功", roleService.getPermissionsByRoleId(id));
    }
}