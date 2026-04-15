package cn.edu.swu.campus_buddy_matching_system.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;
import cn.edu.swu.campus_buddy_matching_system.permission.controller.PermissionController;
import cn.edu.swu.campus_buddy_matching_system.permission.service.PermissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PermissionControllerTest {

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private PermissionController permissionController;

    @Test
    void testGetAllPermissions_Success() {
        PermissionResponse permission = PermissionResponse.builder()
                .id(1L)
                .name("user:read")
                .resource("user")
                .action("read")
                .description("读取用户信息")
                .createdAt(LocalDateTime.now())
                .build();

        when(permissionService.getAllPermissions()).thenReturn(List.of(permission));

        ApiResponse<List<PermissionResponse>> response = permissionController.getAllPermissions();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("查询成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals("user:read", response.getData().get(0).getName());
    }

    @Test
    void testGetPermissionById_Success() {
        PermissionResponse permission = PermissionResponse.builder()
                .id(1L)
                .name("user:read")
                .resource("user")
                .action("read")
                .description("读取用户信息")
                .createdAt(LocalDateTime.now())
                .build();

        when(permissionService.getPermissionById(1L)).thenReturn(permission);

        ApiResponse<PermissionResponse> response = permissionController.getPermissionById(1L);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("查询成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1L, response.getData().getId());
        assertEquals("user:read", response.getData().getName());
    }

    @Test
    void testGetPermissionById_NotFound() {
        when(permissionService.getPermissionById(999L))
                .thenThrow(new IllegalArgumentException("权限不存在"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> permissionController.getPermissionById(999L)
        );

        assertEquals("权限不存在", ex.getMessage());
    }
}