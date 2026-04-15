package cn.edu.swu.campus_buddy_matching_system.service;

import cn.edu.swu.campus_buddy_matching_system.common.dto.PageResult;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserResponse;
import cn.edu.swu.campus_buddy_matching_system.mapper.UserMapper;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import cn.edu.swu.campus_buddy_matching_system.user.service.impl.UserServiceImpl;
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
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGetUsers_Success() {
        User user = User.builder()
                .id(1L)
                .studentId("20240001")
                .username("admin")
                .email("admin@campus.edu")
                .nickname("管理员")
                .enabled(true)
                .creditScore(100)
                .createdAt(LocalDateTime.now())
                .build();

        when(userMapper.selectByPage(0, 10, null, null, null)).thenReturn(List.of(user));
        when(userMapper.countByPage(null, null, null)).thenReturn(1L);

        PageResult<UserResponse> result = userService.getUsers(0, 10, null, null, null);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("admin", result.getContent().get(0).getUsername());
        assertEquals(1, result.getTotalPages());
        assertEquals(1L, result.getTotalElements());
    }

    @Test
    void testGetUserById_Success() {
        Role role = Role.builder()
                .id(1L)
                .name("ROLE_ADMIN")
                .description("管理员角色")
                .createdAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(1L)
                .studentId("20240001")
                .username("admin")
                .email("admin@campus.edu")
                .nickname("管理员")
                .enabled(true)
                .creditScore(100)
                .createdAt(LocalDateTime.now())
                .roles(Set.of(role))
                .build();

        when(userMapper.selectWithRoles(1L)).thenReturn(user);

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("admin", result.getUsername());
        assertNotNull(result.getRoles());
        assertEquals(1, result.getRoles().size());
        assertEquals("ROLE_ADMIN", result.getRoles().get(0).getName());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userMapper.selectWithRoles(999L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(999L)
        );

        assertEquals("用户不存在", ex.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        User user = User.builder()
                .id(3L)
                .studentId("20240003")
                .username("user2")
                .email("user2@campus.edu")
                .nickname("李四")
                .enabled(true)
                .creditScore(95)
                .createdAt(LocalDateTime.now())
                .build();

        when(userMapper.findById(3L)).thenReturn(user);
        when(userMapper.delete(3L)).thenReturn(1);

        assertDoesNotThrow(() -> userService.deleteUser(3L));
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userMapper.findById(999L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(999L)
        );

        assertEquals("用户不存在", ex.getMessage());
    }

    @Test
    void testGetCurrentUser_Success() {
        Role role = Role.builder()
                .id(2L)
                .name("ROLE_USER")
                .description("普通用户角色")
                .createdAt(LocalDateTime.now())
                .build();

        User user = User.builder()
                .id(6L)
                .studentId("20249999")
                .username("newuser999")
                .email("newuser999@swu.edu.cn")
                .nickname("新同学")
                .enabled(true)
                .creditScore(100)
                .createdAt(LocalDateTime.now())
                .roles(Set.of(role))
                .build();

        when(userMapper.selectWithRoles(6L)).thenReturn(user);

        UserResponse result = userService.getCurrentUser(6L);

        assertNotNull(result);
        assertEquals(6L, result.getId());
        assertEquals("newuser999", result.getUsername());
        assertNotNull(result.getRoles());
        assertEquals(1, result.getRoles().size());
    }
}