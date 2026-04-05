package cn.edu.swu.campus_buddy_matching_system.mapper;

import cn.edu.swu.campus_buddy_matching_system.CampusApplication;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.repository.RoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

@SpringBootTest(classes = CampusApplication.class)
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