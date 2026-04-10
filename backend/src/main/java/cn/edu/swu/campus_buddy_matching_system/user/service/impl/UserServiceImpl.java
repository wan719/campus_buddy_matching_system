package cn.edu.swu.campus_buddy_matching_system.user.service.impl;

import cn.edu.swu.campus_buddy_matching_system.common.dto.PageResult;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RoleResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserResponse;
import cn.edu.swu.campus_buddy_matching_system.mapper.UserMapper;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import cn.edu.swu.campus_buddy_matching_system.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户管理服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public PageResult<UserResponse> getUsers(int page, int size, String username, String email, Boolean enabled) {
        int offset = page * size;

        List<User> users = userMapper.selectByPage(offset, size, username, email, enabled);
        long total = userMapper.countByPage(username, email, enabled);

        List<UserResponse> content = users.stream()
                .map(this::toSimpleUserResponse)
                .toList();

        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) total / size);

        return PageResult.<UserResponse>builder()
                .content(content)
                .totalElements(total)
                .totalPages(totalPages)
                .currentPage(page)
                .pageSize(size)
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = userMapper.selectWithRoles(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        return toDetailUserResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }

        int rows = userMapper.delete(id);
        if (rows <= 0) {
            throw new IllegalStateException("删除失败");
        }
    }

    /**
     * 列表页用户DTO转换
     */
    private UserResponse toSimpleUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .enabled(user.getEnabled())
                .creditScore(user.getCreditScore())
                .createdAt(user.getCreatedAt())
                .roles(null)
                .build();
    }

    /**
     * 详情页用户DTO转换
     */
    private UserResponse toDetailUserResponse(User user) {
        List<RoleResponse> roles = user.getRoles() == null
                ? List.of()
                : user.getRoles().stream()
                        .map(this::toRoleResponse)
                        .toList();

        return UserResponse.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .enabled(user.getEnabled())
                .creditScore(user.getCreditScore())
                .createdAt(user.getCreatedAt())
                .roles(roles)
                .build();
    }

    /**
     * Role -> RoleResponse
     */
    private RoleResponse toRoleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .permissions(null)
                .build();
    }

    @Override
    public UserResponse getCurrentUser(Long userId) {
        Long actualUserId = (userId == null ? 1L : userId);

        User user = userMapper.selectWithRoles(actualUserId);
        if (user == null) {
            throw new IllegalArgumentException("当前用户不存在");
        }

        return toDetailUserResponse(user);
    }
}