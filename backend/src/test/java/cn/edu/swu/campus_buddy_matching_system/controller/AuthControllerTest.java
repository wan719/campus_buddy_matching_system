package cn.edu.swu.campus_buddy_matching_system.controller;

import cn.edu.swu.campus_buddy_matching_system.auth.controller.AuthController;
import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserRegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.auth.service.AuthService;
import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RoleResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserResponse;
import cn.edu.swu.campus_buddy_matching_system.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegister_Success() {
        UserRegisterRequest request = UserRegisterRequest.builder()
                .studentId("20249999")
                .username("newuser999")
                .password("123456")
                .confirmPassword("123456")
                .email("newuser999@swu.edu.cn")
                .nickname("新同学")
                .build();

        when(authService.register(request)).thenReturn(6L);

        ApiResponse<Map<String, Long>> response = authController.register(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("注册成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(6L, response.getData().get("userId"));
    }

    @Test
    void testGetCurrentUser_DefaultUser() {
        RoleResponse role = RoleResponse.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .description("管理员角色")
                .createdAt(LocalDateTime.now())
                .permissions(null)
                .build();

        UserResponse user = UserResponse.builder()
                .id(1L)
                .studentId("20240001")
                .username("admin")
                .email("admin@campus.edu")
                .nickname("管理员")
                .enabled(true)
                .creditScore(100)
                .createdAt(LocalDateTime.now())
                .roles(List.of(role))
                .build();

        when(userService.getCurrentUser(null)).thenReturn(user);

        ApiResponse<UserResponse> response = authController.getCurrentUser(null);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("查询成功", response.getMessage());
        assertEquals("admin", response.getData().getUsername());
    }

    @Test
    void testGetCurrentUser_ByHeaderUserId() {
        RoleResponse role = RoleResponse.builder()
                .id(2L)
                .name("ROLE_USER")
                .description("普通用户角色")
                .createdAt(LocalDateTime.now())
                .permissions(null)
                .build();

        UserResponse user = UserResponse.builder()
                .id(6L)
                .studentId("20249999")
                .username("newuser999")
                .email("newuser999@swu.edu.cn")
                .nickname("新同学")
                .enabled(true)
                .creditScore(100)
                .createdAt(LocalDateTime.now())
                .roles(List.of(role))
                .build();

        when(userService.getCurrentUser(6L)).thenReturn(user);

        ApiResponse<UserResponse> response = authController.getCurrentUser(6L);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("newuser999", response.getData().getUsername());
    }
}