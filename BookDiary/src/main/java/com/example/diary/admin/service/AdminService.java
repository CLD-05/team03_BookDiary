package com.example.diary.admin.service;

import com.example.diary.admin.dto.AdminRequestDto;
import com.example.diary.admin.dto.AdminResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    public AdminResponseDto.UserList getUserList() {
        // TODO: 회원 목록 조회 로직
        return new AdminResponseDto.UserList(null);
    }

    public void updateUserStatus(Long userId, AdminRequestDto.UpdateUserStatus requestDto) {
        // TODO: 회원 상태 변경 로직
    }

    public AdminResponseDto.AiTokenInfo getAiTokenInfo() {
        // TODO: AI 토큰 정보 조회 로직
        return null;
    }

    public void updateAiToken(AdminRequestDto.UpdateAiToken requestDto) {
        // TODO: AI 토큰 수정 로직
    }
}