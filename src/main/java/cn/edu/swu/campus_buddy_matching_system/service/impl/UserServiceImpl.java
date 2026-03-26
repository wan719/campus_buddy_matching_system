package cn.edu.swu.campus_buddy_matching_system.service.impl;

import cn.edu.swu.campus_buddy_matching_system.common.dto.LoginRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserVO;
import cn.edu.swu.campus_buddy_matching_system.common.utils.JwtUtil;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import cn.edu.swu.campus_buddy_matching_system.repository.UserMapper;
import cn.edu.swu.campus_buddy_matching_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String register(RegisterRequest request) {
        // 检查学号是否已存在
        User existingUser = userMapper.findByStudentId(request.getStudentId());
        if (existingUser != null) {
            throw new RuntimeException("学号已注册");
        }

        // 创建新用户
        User user = new User();
        user.setStudentId(request.getStudentId());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());

        userMapper.insert(user);
        return jwtUtil.generateToken(user.getId());
    }

    @Override
    public String login(LoginRequest request) {
        User user = userMapper.findByStudentId(request.getStudentId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("密码错误");
        }

        if (!user.getIsActive()) {
            throw new RuntimeException("账号已被禁用");
        }

        return jwtUtil.generateToken(user.getId());
    }

    @Override
    public UserVO getProfile(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setStudentId(user.getStudentId());
        vo.setNickname(user.getNickname());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setCreditScore(user.getCreditScore());
        return vo;
    }
}