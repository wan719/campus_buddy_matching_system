package cn.edu.swu.campus_buddy_matching_system.model;

import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * User 实体类单元测试
 */
/**
 * UserTests 类包含多个测试方法，用于测试User类的各种功能
 */
class UserTests {

    /**
     * 测试UserBuilder是否能正确创建User对象并设置属性
     */
    @Test
    void testUserBuilder() {
        // 使用Builder模式创建User对象并设置各种属性
        User user = User.builder()
                .id(1L)
                .studentId("20240001")
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .nickname("测试用户")
                .creditScore(100)
                .enabled(true)
                .build();

        // 验证User对象不为null
        assertNotNull(user);
        // 验证username属性是否正确设置
        assertEquals("testuser", user.getUsername());
        // 验证email属性是否正确设置
        assertEquals("test@example.com", user.getEmail());
        // 验证enabled属性是否正确设置
        assertTrue(user.getEnabled());
        // 验证creditScore属性是否正确设置
        assertEquals(100, user.getCreditScore());
    }

    /**
     * 测试用户角色管理功能，包括添加角色、检查角色等
     */
    @Test
    void testUserRoleManagement() {
        // 创建一个测试用户
        User user = User.builder()
                .username("testuser")
                .build();

        // 创建管理员角色
        Role adminRole = Role.builder()
                .name("ROLE_ADMIN")
                .description("管理员")
                .build();

        // 创建普通用户角色
        Role userRole = Role.builder()
                .name("ROLE_USER")
                .description("普通用户")
                .build();

        // 测试添加角色功能
        assertTrue(user.addRole(adminRole));
        assertTrue(user.addRole(userRole));
        // 验证角色数量是否正确
        assertEquals(2, user.getRoles().size());

        // 测试重复添加相同角色的情况
        assertFalse(user.addRole(adminRole));
        // 验证角色数量没有变化
        assertEquals(2, user.getRoles().size());

        // 测试hasRole方法
        assertTrue(user.hasRole("ROLE_ADMIN"));
        assertTrue(user.hasRole("ROLE_USER"));
        // 验证不存在的角色
        assertFalse(user.hasRole("ROLE_GUEST"));
    }

    /**
     * 测试用户权限获取功能
     */
    @Test
    void testGetPermissions() {
        // 创建权限
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

        // 创建角色并添加权限
        Role userRole = Role.builder()
                .name("ROLE_USER")
                .build();
        userRole.addPermission(userRead);
        userRole.addPermission(roleRead);

        // 创建用户并添加角色
        User user = User.builder()
                .username("testuser")
                .build();
        user.addRole(userRole);

        // 测试 getPermissions
        Set<String> permissions = user.getPermissions();
        assertTrue(permissions.contains("user:read"));
        assertTrue(permissions.contains("role:read"));
        assertEquals(2, permissions.size());

        // 测试 hasPermission
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