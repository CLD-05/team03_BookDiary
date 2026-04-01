package com.example.diary.book.dto;

import lombok.Getter;

import java.util.List;

public class KakaoBookSearchResponse {

    private Meta meta;
    private List<Document> documents;

    @Getter
    public static class Meta {
        private boolean is_end;
        private int pageable_count;
        private int total_count;
    }

    @Getter
    public static class Document {
        private String title;
        private String contents;
        private String url;
        private String isbn;
        private String datetime;
        private List<String> authors;
        private String publisher;
        private List<String> translators;
        private int price;
        private int sale_price;
        private String thumbnail;
        private String status;
    }
}
