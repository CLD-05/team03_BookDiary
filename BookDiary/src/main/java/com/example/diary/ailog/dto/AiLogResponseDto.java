package com.example.diary.ailog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

// AI 추천 응답
public class AiLogResponseDto {
    private List<BookItem> books;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BookItem {
        private String title;
        private String author;
        private String publisher;
        private String isbn;
        private String imageUrl;
        private String reason;
    }
}
