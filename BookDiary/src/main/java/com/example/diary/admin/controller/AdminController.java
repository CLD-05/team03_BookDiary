package com.example.diary.admin.controller;

import com.example.diary.admin.dto.AdminRequestDto;
import com.example.diary.admin.dto.AdminResponseDto;
import com.example.diary.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<AdminResponseDto.UserList> getUserList() {
        return ResponseEntity.ok(adminService.getUserList());
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<AdminResponseDto.Message> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody AdminRequestDto.UpdateUserStatus requestDto) {

        adminService.updateUserStatus(userId, requestDto);
        return ResponseEntity.ok(new AdminResponseDto.Message("회원 상태가 변경되었습니다."));
    }

    @GetMapping("/ai/token")
    public ResponseEntity<AdminResponseDto.AiTokenInfo> getAiTokenInfo() {
        return ResponseEntity.ok(adminService.getAiTokenInfo());
    }

    @PatchMapping("/ai/token")
    public ResponseEntity<AdminResponseDto.Message> updateAiToken(
            @RequestBody AdminRequestDto.UpdateAiToken requestDto) {

        adminService.updateAiToken(requestDto);
        return ResponseEntity.ok(new AdminResponseDto.Message("AI 토큰 정보가 수정되었습니다."));
    }
}