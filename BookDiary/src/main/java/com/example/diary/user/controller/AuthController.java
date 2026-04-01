package com.example.diary.user.controller;

import com.example.diary.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController extends BaseController {

    @GetMapping("/auth/signup")
    public String signupPage() {
        return "user/signup";
    }

    @GetMapping("/auth/login")
    public String loginPage() {
        return "user/login";
    }

    // 내 프로필 이동 추가
    @GetMapping("/mypage")
    public String myPage() {
        // BaseController의 isLoggedIn()을 사용하여 로그인 여부 체크 가능
//        if (!isLoggedIn()) {          // 일단 서버 로그인이 안 되어 있어도 가능하게 전체 주석
//            return "redirect:/auth/login";
//        }
        return "user/mypage";
    }
}