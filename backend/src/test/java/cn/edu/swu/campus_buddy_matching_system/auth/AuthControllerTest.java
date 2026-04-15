package cn.edu.swu.campus_buddy_matching_system.auth;

import cn.edu.swu.campus_buddy_matching_system.auth.controller.AuthController;
import cn.edu.swu.campus_buddy_matching_system.auth.dto.LoginResponse;
import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserLoginRequest;
import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserRegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.auth.service.AuthService;
import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserResponse;
import cn.edu.swu.campus_buddy_matching_system.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegister_Success() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.setStudentId("20250001");
        request.setUsername("testuser");
        request.setPassword("123456");
        request.setConfirmPassword("123456");
        request.setEmail("testuser@swu.edu.cn");
        request.setNickname("测试用户");

        when(authService.register(request)).thenReturn(100L);

        ApiResponse<Map<String, Long>> response = authController.register(request);

        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("注册成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(100L, response.getData().get("userId"));
    }

    @Test
    void testLogin_Success() {
        UserLoginRequest request = new UserLoginRequest("admin", "admin123");

        when(authService.login(request)).thenReturn("mock-jwt-token");

        ApiResponse<LoginResponse> response = authController.login(request);

        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("登录成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("mock-jwt-token", response.getData().getToken());
        assertEquals("Bearer", response.getData().getType());
        assertEquals("admin", response.getData().getUsername());
    }

    @Test
    void testLogin_InvalidPassword_Failure() {
        UserLoginRequest request = new UserLoginRequest("admin", "wrongpassword");

        when(authService.login(request)).thenThrow(new BadCredentialsException("Bad credentials"));

        ApiResponse<LoginResponse> response = authController.login(request);

        assertFalse(response.isSuccess());
        assertEquals(401, response.getCode());
        assertEquals("用户名或密码错误", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testGetCurrentUser_DefaultUser() {
        UserResponse userResponse = new UserResponse();
        when(userService.getCurrentUser(null)).thenReturn(userResponse);

        ApiResponse<UserResponse> response = authController.getCurrentUser(null);

        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("查询成功", response.getMessage());
        assertEquals(userResponse, response.getData());
    }

    @Test
    void testGetCurrentUser_ByHeaderUserId() {
        UserResponse userResponse = new UserResponse();
        when(userService.getCurrentUser(1L)).thenReturn(userResponse);

        ApiResponse<UserResponse> response = authController.getCurrentUser(1L);

        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("查询成功", response.getMessage());
        assertEquals(userResponse, response.getData());
    }
}