package com.example.diary.diary.dto;

import com.example.diary.diary.entity.BookStatus;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
public class UpdateDiaryRequest {
    private BookStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rating;
    private Boolean favorite;
    private String memoTitle;
    private String memoContent;
}