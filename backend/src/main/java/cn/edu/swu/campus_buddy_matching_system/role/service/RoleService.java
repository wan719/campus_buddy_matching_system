package cn.edu.swu.campus_buddy_matching_system.role.service;

import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RoleResponse;

import java.util.List;

public interface RoleService {

    List<RoleResponse> getAllRoles();

    RoleResponse getRoleById(Long id);

    List<PermissionResponse> getPermissionsByRoleId(Long id);
}