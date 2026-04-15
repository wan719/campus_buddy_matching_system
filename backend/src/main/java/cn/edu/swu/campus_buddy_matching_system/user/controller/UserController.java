package cn.edu.swu.campus_buddy_matching_system.user.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.PageResult;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserResponse;
import cn.edu.swu.campus_buddy_matching_system.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 分页获取用户列表
     */
    @Operation(summary = "获取用户列表", description = "分页查询用户列表，支持用户名、邮箱和账户状态筛选")
    @Parameters({
            @Parameter(name = "page", description = "页码，从0开始"),
            @Parameter(name = "size", description = "每页数量"),
            @Parameter(name = "username", description = "用户名（模糊查询）"),
            @Parameter(name = "email", description = "邮箱（模糊查询）"),
            @Parameter(name = "enabled", description = "账户状态")
    })
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "参数错误")
    })
    @GetMapping
    public ApiResponse<PageResult<UserResponse>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Boolean enabled) {

        return ApiResponse.success(
                "查询成功",
                userService.getUsers(page, size, username, email, enabled)
        );
    }

    /**
     * 根据ID获取用户详情
     */
    @Operation(summary = "获取用户详情", description = "根据ID获取用户详细信息（包含角色）")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查询成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "用户不存在")
    })
    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUserById(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        return ApiResponse.success("查询成功", userService.getUserById(id));
    }

    /**
     * 根据ID删除用户
     */
    @Operation(summary = "删除用户", description = "根据ID删除指定用户")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "删除成功"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "用户不存在"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "删除失败")
    })
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(
            @Parameter(description = "用户ID") @PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("删除成功", null);
    }
}