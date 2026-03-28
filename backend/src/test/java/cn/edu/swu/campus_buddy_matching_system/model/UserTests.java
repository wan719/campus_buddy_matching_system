package cn.edu.swu.campus_buddy_matching_system.model;

import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTests {

        private User user;
        private Role adminRole;
        private Role userRole;
        private Permission userRead;
        private Permission roleRead;

        @BeforeEach
        void setUp() {
                // 初始化测试数据
                user = User.builder()
                                .id(1L)
                                .studentId("20240001")
                                .username("testuser")
                                .password("password123")
                                .email("test@example.com")
                                .nickname("测试用户")
                                .creditScore(100)
                                .enabled(true)
                                .build();

                adminRole = Role.builder()
                                .id(1L)
                                .name("ROLE_ADMIN")
                                .description("管理员")
                                .build();

                userRole = Role.builder()
                                .id(2L)
                                .name("ROLE_USER")
                                .description("普通用户")
                                .build();

                userRead = Permission.builder()
                                .id(1L)
                                .name("user:read")
                                .resource("user")
                                .action("read")
                                .build();

                roleRead = Permission.builder()
                                .id(2L)
                                .name("role:read")
                                .resource("role")
                                .action("read")
                                .build();
        }

        @Test
        void testUserBuilder() {
                assertNotNull(user);
                assertEquals("testuser", user.getUsername());
                assertEquals("test@example.com", user.getEmail());
                assertTrue(user.getEnabled());
                assertEquals(100, user.getCreditScore());
        }

        @Test
        void testUserRoleManagement() {
                // 测试添加角色
                assertTrue(user.addRole(adminRole));
                assertTrue(user.addRole(userRole));
                assertEquals(2, user.getRoles().size());

                // 测试重复添加相同角色
                assertFalse(user.addRole(adminRole));
                assertEquals(2, user.getRoles().size());

                // 测试 hasRole 方法
                assertTrue(user.hasRole("ROLE_ADMIN"));
                assertTrue(user.hasRole("ROLE_USER"));
                assertFalse(user.hasRole("ROLE_GUEST"));
        }

        @Test
        void testRemoveRole() {
                user.addRole(adminRole);
                user.addRole(userRole);

                assertTrue(user.removeRole(adminRole));
                assertEquals(1, user.getRoles().size());
                assertFalse(user.hasRole("ROLE_ADMIN"));
                assertTrue(user.hasRole("ROLE_USER"));

                // 测试移除不存在的角色
                Role guestRole = Role.builder().name("ROLE_GUEST").build();
                assertFalse(user.removeRole(guestRole));
        }

        @Test
        void testGetRoleNames() {
                user.addRole(adminRole);
                user.addRole(userRole);

                Set<String> roleNames = user.getRoleNames();
                assertTrue(roleNames.contains("ROLE_ADMIN"));
                assertTrue(roleNames.contains("ROLE_USER"));
                assertEquals(2, roleNames.size());
        }

        @Test
        void testGetPermissions() {
                // 添加权限到角色
                userRole.addPermission(userRead);
                userRole.addPermission(roleRead);

                // 添加角色到用户
                user.addRole(userRole);

                // 测试获取权限
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

        @Test
        void testUserWithBuilder() {
                User newUser = User.builder()
                                .studentId("20240002")
                                .username("newuser")
                                .nickname("新用户")
                                .email("new@campus.edu")
                                .creditScore(100)
                                .enabled(true)
                                .build();

                assertNotNull(newUser);
                assertEquals("20240002", newUser.getStudentId());
                assertEquals("newuser", newUser.getUsername());
                assertEquals("新用户", newUser.getNickname());
                assertEquals(100, newUser.getCreditScore());
                assertTrue(newUser.getEnabled());
        }
}