package com.example.diary.ailog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AiSummaryResponseDto {
    private String title;
    private String author;
    private String summary;
}