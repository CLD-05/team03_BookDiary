package com.example.diary.diary.dto;
import com.example.diary.diary.entity.BookStatus;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UpdateDiaryRequest {

    private LocalDate startDate;
    private LocalDate endDate;

    @DecimalMin(value = "0.0", message = "rating은 0 이상이어야 합니다.")
    @DecimalMax(value = "5.0", message = "rating은 5 이하여야 합니다.")
    private BigDecimal rating;

    private Boolean favorite;
    private String memoTitle;
    private String memoContent;
    private BookStatus status;
}