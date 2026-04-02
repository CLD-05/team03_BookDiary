package com.example.diary.user.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.user.dto.UserResponseDto;
import com.example.diary.user.dto.UserUpdateDto;
import com.example.diary.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getProfile() {
        return ok(UserResponseDto.fromEntity(getCurrentUser()));
    }

    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<Void>> updateProfile(@Valid @RequestBody UserUpdateDto dto) {
        userService.updateProfile(getCurrentUserIdx(), dto);
        return ok();
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> withdraw() {
        userService.withdraw(getCurrentUserIdx());
        return ok();
    }
}