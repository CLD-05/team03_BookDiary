package com.example.diary.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminRequestDto {

    // 회원 상태 변경
    @Getter
    @NoArgsConstructor
    public static class UpdateUserStatus {
        private String status; // ACTIVE, INACTIVE, SUSPENDED
    }

    // AI 토큰 수정
    @Getter
    @NoArgsConstructor
    public static class UpdateAiToken {
        private String apiKey;
        private Integer maxToken;
    }
}