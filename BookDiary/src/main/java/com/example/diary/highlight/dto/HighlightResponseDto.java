package com.example.diary.highlight.dto;

import com.example.diary.highlight.entity.TbHighlight;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class HighlightResponseDto {
    private Long highlightId;
    private Long diaryId;
    private String bookTitle;
    private String bookAuthor;
    private String bookImageUrl;
    private Integer pageNumber;
    private String highlightText;
    private LocalDateTime createDate;

    public static HighlightResponseDto fromEntity(TbHighlight highlight) {
        return HighlightResponseDto.builder()
                .highlightId(highlight.getIdxHighlight())
                .diaryId(highlight.getDiary().getIdxDiary())
                .bookTitle(highlight.getDiary().getBook().getTitle())
                .bookAuthor(highlight.getDiary().getBook().getAuthor())
                .bookImageUrl(highlight.getDiary().getBook().getImageUrl())
                .pageNumber(highlight.getPageNumber())
                .highlightText(highlight.getHighlightText())
                .createDate(highlight.getCreateDate())
                .build();
    }
}