package com.example.diary.user.controller;

import com.example.diary.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController extends BaseController {

    @GetMapping("/auth/signup")
    public String signupPage() {
        return "pages/user/signup";
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "pages/user/login";
    }

    // 내 프로필 이동 추가
    @GetMapping("/mypage/mypage")
    public String myPage() {
        return "pages/mypage/mypage";
    }
}