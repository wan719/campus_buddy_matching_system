package cn.edu.swu.campus_buddy_matching_system.mapper;

import cn.edu.swu.campus_buddy_matching_system.CampusApplication;
import cn.edu.swu.campus_buddy_matching_system.mapper.UserMapper;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CampusApplication.class)
@ActiveProfiles("test")
@Transactional
public class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void testFindById() {
        User user = userMapper.findById(1L);
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("20240001", user.getStudentId());
    }

    @Test
    public void testFindByUsername() {
        User user = userMapper.findByUsername("user1");
        assertNotNull(user);
        assertEquals("user1", user.getUsername());
        assertNotNull(user.getNickname());
    }

    @Test
    public void testFindByStudentId() {
        User user = userMapper.findByStudentId("20240001");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void testInsertUser() {
        User user = User.builder()
                .studentId("20240999")
                .username("testuser999")
                .password("password123")
                .email("test999@campus.edu")
                .nickname("测试用户")
                .college("计算机学院")
                .grade(2024)
                .creditScore(100)
                .enabled(true)
                .build();

        int rows = userMapper.insert(user);
        assertEquals(1, rows);
        assertNotNull(user.getId());
    }

    @Test
    public void testUpdatePassword() {
        int rows = userMapper.updatePassword(1L, "newpassword123");
        assertEquals(1, rows);
    }

    @Test
    public void testUpdateCreditScore() {
        int rows = userMapper.updateCreditScore(1L, 95);
        assertEquals(1, rows);
    }
}