package com.example.diary.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class AdminResponseDto {

    // 회원 정보
    @Getter
    @AllArgsConstructor
    public static class UserInfo {
        private Long userId;
        private String email;
        private String nickname;
        private String status;
        private String role;
    }

    // 회원 리스트
    @Getter
    @AllArgsConstructor
    public static class UserList {
        private List<UserInfo> users;
    }

    // 대시보드
    @Getter
    @AllArgsConstructor
    public static class Dashboard {
        private long totalUsers;
        private long activeUsers;
        private long totalDiaries;
        private long totalAiUsage;
    }

    // AI 토큰 정보
    @Getter
    @AllArgsConstructor
    public static class AiTokenInfo {
        private String apiKey;
        private Integer maxToken;
        private Long usedToken;
        private Long remainingToken;
    }

    // 공통 메시지 응답
    @Getter
    @AllArgsConstructor
    public static class Message {
        private String message;
    }
}