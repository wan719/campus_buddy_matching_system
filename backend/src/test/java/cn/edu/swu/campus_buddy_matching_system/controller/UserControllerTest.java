package cn.edu.swu.campus_buddy_matching_system.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.PageResult;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RoleResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserResponse;
import cn.edu.swu.campus_buddy_matching_system.user.controller.UserController;
import cn.edu.swu.campus_buddy_matching_system.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUsers_Success() {
        UserResponse user = UserResponse.builder()
                .id(1L)
                .studentId("20240001")
                .username("admin")
                .email("admin@campus.edu")
                .nickname("管理员")
                .enabled(true)
                .creditScore(100)
                .createdAt(LocalDateTime.now())
                .roles(null)
                .build();

        PageResult<UserResponse> pageResult = PageResult.<UserResponse>builder()
                .content(List.of(user))
                .totalElements(1)
                .totalPages(1)
                .currentPage(0)
                .pageSize(10)
                .build();

        when(userService.getUsers(0, 10, null, null, null)).thenReturn(pageResult);

        ApiResponse<PageResult<UserResponse>> response =
                userController.getUsers(0, 10, null, null, null);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("查询成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().getContent().size());
        assertEquals("admin", response.getData().getContent().get(0).getUsername());
    }

    @Test
    void testGetUserById_Success() {
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

        when(userService.getUserById(1L)).thenReturn(user);

        ApiResponse<UserResponse> response = userController.getUserById(1L);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("admin", response.getData().getUsername());
        assertNotNull(response.getData().getRoles());
        assertEquals(1, response.getData().getRoles().size());
    }

    @Test
    void testDeleteUser_Success() {
        doNothing().when(userService).deleteUser(3L);

        ApiResponse<Void> response = userController.deleteUser(3L);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("删除成功", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userService.getUserById(999L))
                .thenThrow(new IllegalArgumentException("用户不存在"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userController.getUserById(999L)
        );

        assertEquals("用户不存在", ex.getMessage());
    }
}