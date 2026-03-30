package cn.edu.swu.campus_buddy_matching_system.model;

import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTests {

    @Test
    void testUserBuilder() {
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .enabled(true)
                .build();

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
    }

    @Test
    void testUserRoleManagement() {
        User user = User.builder()
                .username("testuser")
                .build();

        Role adminRole = Role.builder()
                .name("ROLE_ADMIN")
                .description("管理员")
                .build();

        Role userRole = Role.builder()
                .name("ROLE_USER")
                .description("普通用户")
                .build();

        assertTrue(user.addRole(adminRole));
        assertTrue(user.addRole(userRole));
        assertEquals(2, user.getRoles().size());

        assertFalse(user.addRole(adminRole));
        assertEquals(2, user.getRoles().size());

        assertTrue(user.hasRole("ROLE_ADMIN"));
        assertTrue(user.hasRole("ROLE_USER"));
        assertFalse(user.hasRole("ROLE_GUEST"));
    }

    @Test
    void testGetPermissions() {
        Permission userRead = Permission.builder()
                .name("user:read")
                .resource("user")
                .action("read")
                .build();

        Permission roleRead = Permission.builder()
                .name("role:read")
                .resource("role")
                .action("read")
                .build();

        Role userRole = Role.builder()
                .name("ROLE_USER")
                .build();
        userRole.addPermission(userRead);
        userRole.addPermission(roleRead);

        User user = User.builder()
                .username("testuser")
                .build();
        user.addRole(userRole);

        Set<String> permissions = user.getPermissions();
        assertTrue(permissions.contains("user:read"));
        assertTrue(permissions.contains("role:read"));
        assertEquals(2, permissions.size());

        assertTrue(user.hasPermission("user:read"));
        assertTrue(user.hasPermission("role:read"));
        assertFalse(user.hasPermission("user:create"));
    }

    @Test
    void testPermissionNameBuilder() {
        String permissionName = Permission.buildPermissionName("activity", "create");
        assertEquals("activity:create", permissionName);

        Permission perm = Permission.builder()
                .resource("user")
                .action("delete")
                .build();
        perm.updateName();
        assertEquals("user:delete", perm.getName());
    }
}