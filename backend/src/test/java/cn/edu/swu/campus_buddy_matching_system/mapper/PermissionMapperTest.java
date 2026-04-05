package cn.edu.swu.campus_buddy_matching_system.mapper;

import cn.edu.swu.campus_buddy_matching_system.CampusApplication;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.repository.PermissionMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = CampusApplication.class)
@ActiveProfiles("test")
@Transactional
public class PermissionMapperTest {

    @Autowired
    private PermissionMapper permissionMapper;

    @Test
    public void testFindById() {
        Permission permission = permissionMapper.findById(1L);
        assertNotNull(permission);
        assertEquals("user:create", permission.getName());
    }

    @Test
    public void testFindPermissionsByRoleId() {
        Set<Permission> permissions = permissionMapper.findPermissionsByRoleId(1L);
        assertNotNull(permissions);
        assertTrue(permissions.size() > 0);
    }

    @Test
    public void testInsertPermission() {
        Permission permission = Permission.builder()
                .name("test:create")
                .description("测试权限")
                .resource("test")
                .action("create")
                .build();

        int rows = permissionMapper.insert(permission);
        assertEquals(1, rows);
        assertNotNull(permission.getId());
    }
}