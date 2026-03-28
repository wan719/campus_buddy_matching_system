package cn.edu.swu.campus_buddy_matching_system.service.impl;

import cn.edu.swu.campus_buddy_matching_system.common.dto.LoginRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.RegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.common.dto.UserVO;
import cn.edu.swu.campus_buddy_matching_system.common.utils.JwtUtil;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import cn.edu.swu.campus_buddy_matching_system.repository.RoleMapper;
import cn.edu.swu.campus_buddy_matching_system.repository.UserMapper;
import cn.edu.swu.campus_buddy_matching_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private JwtUtil jwtUtil;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional
    public String register(RegisterRequest request) {
        // 1. 检查学号是否已存在
        User existingUser = userMapper.findByStudentId(request.getStudentId());
        if (existingUser != null) {
            throw new RuntimeException("学号已注册");
        }

        // 2. 检查用户名是否已存在（学号作为用户名）
        User existingUsername = userMapper.findByUsername(request.getStudentId());
        if (existingUsername != null) {
            throw new RuntimeException("用户名已存在");
        }

        // 3. 创建新用户
        User user = new User();
        user.setStudentId(request.getStudentId());
        user.setUsername(request.getStudentId()); // 使用学号作为登录用户名
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        user.setEmail(request.getStudentId() + "@campus.edu"); // 生成默认邮箱
        user.setCreditScore(100);
        user.setEnabled(true);

        // 4. 插入用户
        userMapper.insert(user);

        // 5. 分配默认角色（普通用户）
        Role defaultRole = roleMapper.findByName("ROLE_USER");
        if (defaultRole != null) {
            roleMapper.assignRoleToUser(user.getId(), defaultRole.getId());
        }

        // 6. 生成 JWT token
        return jwtUtil.generateToken(user.getId());
    }

    @Override
    public String login(LoginRequest request) {
        // 1. 用学号查找用户
        User user = userMapper.findByStudentId(request.getStudentId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        // 2. 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("密码错误");
        }

        // 3. 检查账号是否启用
        if (!user.getEnabled()) {
            throw new RuntimeException("账号已被禁用");
        }

        // 4. 生成 JWT token
        return jwtUtil.generateToken(user.getId());
    }

    @Override
    public UserVO getProfile(Long userId) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        return UserVO.builder()
                .id(user.getId())
                .studentId(user.getStudentId())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .creditScore(user.getCreditScore().toString())
                .build();
    }
}