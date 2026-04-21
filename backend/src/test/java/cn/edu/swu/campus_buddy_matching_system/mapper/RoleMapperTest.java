package cn.edu.swu.campus_buddy_matching_system.mapper;

import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Transactional
public class RoleMapperTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testFindById() {
        Role role = roleMapper.findById(1L);
        assertNotNull(role);
        assertEquals("ROLE_ADMIN", role.getName());
    }

    @Test
    public void testFindByName() {
        Role role = roleMapper.findByName("ROLE_USER");
        assertNotNull(role);
        assertEquals("ROLE_USER", role.getName());
    }

    @Test
    public void testInsertRole() {
        Role role = Role.builder()
            .name("ROLE_TEST")
            .description("测试角色")
            .build();
        
        int rows = roleMapper.insert(role);
        assertEquals(1, rows);
        assertNotNull(role.getId());
    }

    @Test
    public void testFindRolesByUserId() {
        Set<Role> roles = roleMapper.findRolesByUserId(1L);
        assertNotNull(roles);
        assertTrue(roles.size() > 0);
    }
}
