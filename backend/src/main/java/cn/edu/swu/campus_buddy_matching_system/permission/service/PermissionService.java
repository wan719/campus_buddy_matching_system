package cn.edu.swu.campus_buddy_matching_system.permission.service;

import cn.edu.swu.campus_buddy_matching_system.common.dto.PermissionResponse;

import java.util.List;

public interface PermissionService {

    /**
     * 获取所有权限
     */
    List<PermissionResponse> getAllPermissions();

    /**
     * 根据ID获取权限详情
     */
    PermissionResponse getPermissionById(Long id);
}