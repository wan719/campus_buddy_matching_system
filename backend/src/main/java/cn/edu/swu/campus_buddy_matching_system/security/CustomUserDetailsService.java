package cn.edu.swu.campus_buddy_matching_system.security;

import cn.edu.swu.campus_buddy_matching_system.mapper.PermissionMapper;
import cn.edu.swu.campus_buddy_matching_system.mapper.UserMapper;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

/**
 * 自定义用户详情服务
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 补齐每个角色的权限
        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                List<Permission> rolePermissions = permissionMapper.selectByRole(role.getId());
                role.setPermissions(new HashSet<>(rolePermissions));
            }
        }

        return new CustomUserDetails(user);
    }
}