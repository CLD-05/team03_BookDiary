package com.example.diary.user.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.user.dto.LoginRequestDto;
import com.example.diary.user.dto.LoginResponseDto;
import com.example.diary.user.dto.SignupRequestDto;
import com.example.diary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController extends BaseController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequestDto dto) { // @Valid 추가!
        userService.signup(dto);
        return ok();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginRequestDto dto) {
        return ok(userService.login(dto));
    }
}