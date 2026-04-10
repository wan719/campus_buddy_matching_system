package cn.edu.swu.campus_buddy_matching_system.service;

import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RoleResponse;
import cn.edu.swu.campus_buddy_matching_system.mapper.RoleMapper;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.role.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {

    @Mock
    private RoleMapper roleMapper;

    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void testGetAllRoles_Success() {
        Role role = Role.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .description("管理员角色")
                .createdAt(LocalDateTime.now())
                .build();

        when(roleMapper.selectAll()).thenReturn(List.of(role));

        List<RoleResponse> result = roleService.getAllRoles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("ROLE_ADMIN", result.get(0).getName());
        assertEquals("管理员角色", result.get(0).getDescription());
    }

    @Test
    void testGetRoleById_Success() {
        Permission permission = Permission.builder()
                .id(1L)
                .name("user:read")
                .resource("user")
                .action("read")
                .description("读取用户信息")
                .createdAt(LocalDateTime.now())
                .build();

        Role role = Role.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .description("管理员角色")
                .createdAt(LocalDateTime.now())
                .permissions(Set.of(permission))
                .build();

        when(roleMapper.selectWithPermissions(1L)).thenReturn(role);

        RoleResponse result = roleService.getRoleById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ROLE_ADMIN", result.getName());
        assertNotNull(result.getPermissions());
        assertEquals(1, result.getPermissions().size());
        assertEquals("user:read", result.getPermissions().get(0).getName());
    }

    @Test
    void testGetPermissionsByRoleId_Success() {
        Permission permission = Permission.builder()
                .id(1L)
                .name("user:read")
                .resource("user")
                .action("read")
                .description("读取用户信息")
                .createdAt(LocalDateTime.now())
                .build();

        Role role = Role.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .description("管理员角色")
                .createdAt(LocalDateTime.now())
                .permissions(Set.of(permission))
                .build();

        when(roleMapper.selectWithPermissions(1L)).thenReturn(role);

        List<PermissionResponse> result = roleService.getPermissionsByRoleId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user:read", result.get(0).getName());
    }

    @Test
    void testGetRoleById_NotFound() {
        when(roleMapper.selectWithPermissions(999L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.getRoleById(999L)
        );

        assertEquals("角色不存在", ex.getMessage());
    }
}