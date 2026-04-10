package cn.edu.swu.campus_buddy_matching_system.auth.service;

import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserRegisterRequest;

public interface AuthService {
    Long register(UserRegisterRequest request);
}