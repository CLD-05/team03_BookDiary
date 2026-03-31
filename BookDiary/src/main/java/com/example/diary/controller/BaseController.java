package com.example.diary.controller;

//import com.example.diary.user.entity.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseController {
// 지워주세요
    protected static final int ROW_CNT = 10; // 페이지당 행 수

    // 현재 로그인한 유저 이메일
    protected String getCurrentUserEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) return null;
        return auth.getName();
    }

    // 현재 로그인한 유저 role
    protected String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        return auth.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .orElse(null);
    }

    // 관리자 여부 확인
    protected boolean isAdmin() {
        return "ROLE_ADMIN".equals(getCurrentUserRole());
    }

    // 로그인 여부 확인
    protected boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated()
                && !"anonymousUser".equals(auth.getPrincipal());
    }
}