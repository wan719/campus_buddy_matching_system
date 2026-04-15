package cn.edu.swu.campus_buddy_matching_system.auth.service.impl;

import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserLoginRequest;
import cn.edu.swu.campus_buddy_matching_system.auth.dto.UserRegisterRequest;
import cn.edu.swu.campus_buddy_matching_system.auth.service.AuthService;
import cn.edu.swu.campus_buddy_matching_system.common.utils.JwtUtil;
import cn.edu.swu.campus_buddy_matching_system.mapper.RoleMapper;
import cn.edu.swu.campus_buddy_matching_system.mapper.UserMapper;
import cn.edu.swu.campus_buddy_matching_system.mapper.UserRoleMapper;
import cn.edu.swu.campus_buddy_matching_system.model.entity.Role;
import cn.edu.swu.campus_buddy_matching_system.model.entity.User;
import cn.edu.swu.campus_buddy_matching_system.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public Long register(UserRegisterRequest request) {
        log.info("用户注册: {}", request.getUsername());

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("密码和确认密码不匹配");
        }

        if (userMapper.findByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已存在");
        }

        if (userMapper.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("邮箱已被使用");
        }

        if (userMapper.findByStudentId(request.getStudentId()) != null) {
            throw new IllegalArgumentException("学号已存在");
        }

        User user = User.builder()
                .studentId(request.getStudentId())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .nickname(request.getNickname())
                .enabled(true)
                .creditScore(100)
                .roles(new HashSet<>())
                .build();

        userMapper.insert(user);

        Role defaultRole = roleMapper.findByName("ROLE_USER");
        if (defaultRole == null) {
            throw new IllegalStateException("默认角色 ROLE_USER 不存在");
        }

        userRoleMapper.insert(user.getId(), defaultRole.getId());

        return user.getId();
    }

    @Override
    public String login(UserLoginRequest request) {
        Authentication authenticationToken = new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return jwtUtil.generateToken(userDetails.getId());
    }
}