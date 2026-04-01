package com.example.diary.user.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.user.dto.UserResponseDto;
import com.example.diary.user.dto.UserUpdateDto;
import com.example.diary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponseDto>> getProfile() {
        // BaseController의 getCurrentUser() 사용
        return ok(UserResponseDto.fromEntity(getCurrentUser()));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@RequestBody UserUpdateDto dto) {
        // BaseController의 getCurrentUserIdx() 사용
        userService.updateProfile(getCurrentUserIdx(), dto);
        return ok();
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw() {
        userService.withdraw(getCurrentUserIdx());
        return ok();
    }
}