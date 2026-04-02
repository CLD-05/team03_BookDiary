package com.example.diary.book.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class BookResponseDto {
    private Long bookId;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;
    private LocalDate publishDate;
}