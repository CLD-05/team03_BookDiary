package com.example.diary.diary.dto;

import com.example.diary.diary.entity.BookStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class DiaryResponseDto {

    private Long diaryId;
    private Long bookId;

    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String imageUrl;

    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rating;
    private Boolean favorite;
    private String memoTitle;
    private String memoContent;
    private BookStatus status;
    private LocalDateTime createDate;
}
