package com.example.diary.admin.controller;

import com.example.diary.admin.dto.AdminRequestDto;
import com.example.diary.admin.dto.AdminResponseDto;
import com.example.diary.admin.service.AdminService;
import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApiController extends BaseController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<AdminResponseDto.UserList>> getUserList() {
        return ok(adminService.getUserList());
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<AdminResponseDto.Message>> updateUserStatus(
            @PathVariable Long userId,
            @RequestBody AdminRequestDto.UpdateUserStatus requestDto) {
        adminService.updateUserStatus(userId, requestDto);
        return ok(new AdminResponseDto.Message("회원 상태가 변경되었습니다."));
    }

    @GetMapping("/ai/token")
    public ResponseEntity<ApiResponse<AdminResponseDto.AiTokenInfo>> getAiTokenInfo() {
        return ok(adminService.getAiTokenInfo());
    }

    @PatchMapping("/ai/token")
    public ResponseEntity<ApiResponse<AdminResponseDto.Message>> updateAiToken(
            @RequestBody AdminRequestDto.UpdateAiToken requestDto) {
        adminService.updateAiToken(requestDto);
        return ok(new AdminResponseDto.Message("AI 토큰 정보가 수정되었습니다."));
    }
}