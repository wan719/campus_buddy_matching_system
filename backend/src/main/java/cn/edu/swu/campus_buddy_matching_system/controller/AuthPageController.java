package cn.edu.swu.campus_buddy_matching_system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPageController {

    // 显示登录页面
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // 返回 login.html 页面
    }

    // 显示注册页面
    @GetMapping("/register")
    public String showRegisterPage() {
        return "register"; // 返回 register.html 页面
    }

    // 首页
    @GetMapping("/")
    public String showHomePage() {
        return "index"; // 返回 index.html 页面
    }
}