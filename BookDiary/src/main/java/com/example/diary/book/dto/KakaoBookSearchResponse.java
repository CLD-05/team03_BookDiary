package com.example.diary.book.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoBookSearchResponse {

    private Meta meta;
    private List<Document> documents;

    @Getter
    public static class Meta {
        private boolean is_end; // 마지막 페이지 여부
        private int pageable_count; // 노출 가능한 총 개수
        private int total_count; // 전체 검색 결과 수
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
