package cn.edu.swu.campus_buddy_matching_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // 放行首页、登录、注册页面以及静态资源
                .requestMatchers("/", "/login", "/register", "/static/**").permitAll()
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                // 自定义登录页面
                .loginPage("/login")
                .permitAll()  // 允许所有人访问登录页面
            )
            .logout(logout -> logout
                .permitAll()  // 允许所有人访问退出登录功能
            );
        return http.build();
    }

    // 密码加密器配置
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();  // 使用 BCrypt 加密密码
    }
}