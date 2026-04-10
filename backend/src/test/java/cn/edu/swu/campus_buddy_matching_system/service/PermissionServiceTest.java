package cn.edu.swu.campus_buddy_matching_system.service;

import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;
import cn.edu.swu.campus_buddy_matching_system.mapper.PermissionMapper;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.permission.service.impl.PermissionServiceImpl;
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
public class PermissionServiceTest {

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @Test
    void testGetAllPermissions_Success() {
        Permission permission = Permission.builder()
                .id(1L)
                .name("user:read")
                .resource("user")
                .action("read")
                .description("读取用户信息")
                .createdAt(LocalDateTime.now())
                .build();

        when(permissionMapper.selectAll()).thenReturn(List.of(permission));

        List<PermissionResponse> result = permissionService.getAllPermissions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user:read", result.get(0).getName());
        assertEquals("user", result.get(0).getResource());
        assertEquals("read", result.get(0).getAction());
    }

    @Test
    void testGetPermissionById_Success() {
        Permission permission = Permission.builder()
                .id(1L)
                .name("user:read")
                .resource("user")
                .action("read")
                .description("读取用户信息")
                .createdAt(LocalDateTime.now())
                .build();

        when(permissionMapper.findById(1L)).thenReturn(permission);

        PermissionResponse result = permissionService.getPermissionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("user:read", result.getName());
        assertEquals("user", result.getResource());
    }

    @Test
    void testGetPermissionById_NotFound() {
        when(permissionMapper.findById(999L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> permissionService.getPermissionById(999L)
        );

        assertEquals("权限不存在", ex.getMessage());
    }
}