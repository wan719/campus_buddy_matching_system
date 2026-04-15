package cn.edu.swu.campus_buddy_matching_system.role.service.impl;

import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RoleResponse;
import cn.edu.swu.campus_buddy_matching_system.mapper.RoleMapper;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Permission;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleMapper.selectAll()
                .stream()
                .map(this::toSimpleResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RoleResponse getRoleById(Long id) {
        Role role = roleMapper.selectWithPermissions(id);
        if (role == null) {
            throw new IllegalArgumentException("角色不存在");
        }
        return toDetailResponse(role);
    }

    @Override
    public List<PermissionResponse> getPermissionsByRoleId(Long id) {
        Role role = roleMapper.selectWithPermissions(id);
        if (role == null) {
            throw new IllegalArgumentException("角色不存在");
        }

        if (role.getPermissions() == null) {
            return Collections.emptyList();
        }

        return role.getPermissions().stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toList());
    }

    private RoleResponse toSimpleResponse(Role role) {
        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .permissions(null)
                .build();
    }

    private RoleResponse toDetailResponse(Role role) {
        List<PermissionResponse> permissions = role.getPermissions() == null
                ? Collections.emptyList()
                : role.getPermissions().stream()
                .map(this::toPermissionResponse)
                .collect(Collectors.toList());

        return RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .createdAt(role.getCreatedAt())
                .permissions(permissions)
                .build();
    }

    private PermissionResponse toPermissionResponse(Permission permission) {
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .resource(permission.getResource())
                .action(permission.getAction())
                .description(permission.getDescription())
                .createdAt(permission.getCreatedAt())
                .build();
    }
}