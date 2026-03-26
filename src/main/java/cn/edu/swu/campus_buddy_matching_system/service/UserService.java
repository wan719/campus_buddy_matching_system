package cn.edu.swu.campus_buddy_matching_system.service;

import cn.edu.swu.campus_buddy_matching_system.common.dto.LoginRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserVO;

public interface UserService {
    String register(RegisterRequest request);
    String login(LoginRequest request);
    UserVO getProfile(Long userId);
}