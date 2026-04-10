package cn.edu.swu.campus_buddy_matching_system.user.service;

import cn.edu.swu.campus_buddy_matching_system.common.dto.PageResult;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserResponse;

/**
 * 用户管理服务接口
 */
public interface UserService {

    /**
     * 分页查询用户列表
     *
     * @param page 页码，从0开始
     * @param size 每页数量
     * @param username 用户名（可选，模糊查询）
     * @param email 邮箱（可选，模糊查询）
     * @param enabled 账户状态（可选）
     * @return 分页用户结果
     */
    PageResult<UserResponse> getUsers(int page, int size, String username, String email, Boolean enabled);

    /**
     * 根据ID查询用户详情（包含角色）
     *
     * @param id 用户ID
     * @return 用户详情
     */
    UserResponse getUserById(Long id);

    /**
     * 根据ID删除用户
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);
    UserResponse getCurrentUser(Long userId);
}