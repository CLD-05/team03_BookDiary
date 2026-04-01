package com.example.diary.user.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.user.dto.LoginRequestDto;
import com.example.diary.user.dto.SignupRequestDto;
import com.example.diary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // API용이므로 RestController 사용
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController extends BaseController {

    private final UserService userService;

    // Postman에서 요청한 그 주소를 처리하는 메서드
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequestDto dto) {
        userService.signup(dto);
        return ok(); // BaseController의 ok() 메서드 사용
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequestDto dto) {
        String token = userService.login(dto);
        return ok(token);
    }
}