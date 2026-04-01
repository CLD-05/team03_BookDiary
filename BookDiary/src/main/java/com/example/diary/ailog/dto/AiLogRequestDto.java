package com.example.diary.ailog.dto;

import lombok.Builder;
import lombok.Getter;

// AI 도서 추천 요청
@Getter
@Builder
public class AiLogRequestDto {
    private String category;
}
