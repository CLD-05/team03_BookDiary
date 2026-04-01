package com.example.diary.book.dto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BookSearchResponse {
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private String imageUrl;
    private String contents;
}
