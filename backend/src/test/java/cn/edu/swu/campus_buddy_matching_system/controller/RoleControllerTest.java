package cn.edu.swu.campus_buddy_matching_system.controller;

import cn.edu.swu.campus_buddy_matching_system.common.dto.ApiResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RoleResponse;
import cn.edu.swu.campus_buddy_matching_system.role.controller.RoleController;
import cn.edu.swu.campus_buddy_matching_system.role.service.RoleService;
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
public class RoleControllerTest {

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    @Test
    void testGetAllRoles_Success() {
        RoleResponse role = RoleResponse.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .description("管理员角色")
                .createdAt(LocalDateTime.now())
                .permissions(null)
                .build();

        when(roleService.getAllRoles()).thenReturn(List.of(role));

        ApiResponse<List<RoleResponse>> response = roleController.getAllRoles();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("查询成功", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(1, response.getData().size());
        assertEquals("ROLE_ADMIN", response.getData().get(0).getName());
    }

    @Test
    void testGetRoleById_Success() {
        PermissionResponse permission = PermissionResponse.builder()
                .id(1L)
                .name("user:read")
                .resource("user")
                .action("read")
                .description("读取用户信息")
                .createdAt(LocalDateTime.now())
                .build();

        RoleResponse role = RoleResponse.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .description("管理员角色")
                .createdAt(LocalDateTime.now())
                .permissions(List.of(permission))
                .build();

        when(roleService.getRoleById(1L)).thenReturn(role);

        ApiResponse<RoleResponse> response = roleController.getRoleById(1L);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals("ROLE_ADMIN", response.getData().getName());
        assertNotNull(response.getData().getPermissions());
        assertEquals(1, response.getData().getPermissions().size());
    }

    @Test
    void testGetPermissionsByRoleId_Success() {
        PermissionResponse permission = PermissionResponse.builder()
                .id(1L)
                .name("user:read")
                .resource("user")
                .action("read")
                .description("读取用户信息")
                .createdAt(LocalDateTime.now())
                .build();

        when(roleService.getPermissionsByRoleId(1L)).thenReturn(List.of(permission));

        ApiResponse<List<PermissionResponse>> response = roleController.getPermissionsByRoleId(1L);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(200, response.getCode());
        assertEquals(1, response.getData().size());
        assertEquals("user:read", response.getData().get(0).getName());
    }

    @Test
    void testGetRoleById_NotFound() {
        when(roleService.getRoleById(999L))
                .thenThrow(new IllegalArgumentException("角色不存在"));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> roleController.getRoleById(999L)
        );

        assertEquals("角色不存在", ex.getMessage());
    }
}