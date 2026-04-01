package com.example.diary.diary.dto;

import com.example.diary.diary.entity.BookStatus;
import com.example.diary.diary.entity.TbDiary;
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
    private String category;
    private String imageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal rating;
    private Boolean favorite;
    private String memoTitle;
    private String memoContent;
    private BookStatus status;
    private LocalDateTime createDate;

    public static DiaryResponseDto fromEntity(TbDiary diary) {
        return DiaryResponseDto.builder()
                .diaryId(diary.getIdxDiary())
                .bookId(diary.getBook().getIdxBook())
                .isbn(diary.getBook().getIsbn())
                .title(diary.getBook().getTitle())
                .author(diary.getBook().getAuthor())
                .publisher(diary.getBook().getPublisher())
                .category(diary.getBook().getCategory())
                .imageUrl(diary.getBook().getImageUrl())
                .startDate(diary.getStartDate())
                .endDate(diary.getEndDate())
                .rating(diary.getRating())
                .favorite(diary.getFavorite())
                .memoTitle(diary.getMemoTitle())
                .memoContent(diary.getMemoContent())
                .status(diary.getStatus())
                .createDate(diary.getCreateDate())
                .build();
    }
}