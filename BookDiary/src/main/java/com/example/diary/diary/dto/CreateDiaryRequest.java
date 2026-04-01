package com.example.diary.diary.dto;

import com.example.diary.diary.entity.BookStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class CreateDiaryRequest {

    // book 정보
    @NotBlank(message = "isbn은 필수입니다.")
    private String isbn;

    @NotBlank(message = "title은 필수입니다.")
    private String title;

    private String author;
    private String publisher;
    private String category;
    private String imageUrl;
    private LocalDate publishDate;

    // diary 정보
    private LocalDate startDate;
    private LocalDate endDate;

    @DecimalMin(value = "0.0", message = "rating은 0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "rating은 5 이하여야 합니다.")
    private BigDecimal rating;

    private Boolean favorite;
    private String memoTitle;
    private String memoContent;

    @NotNull(message = "status는 필수입니다.")
    private BookStatus status;
}