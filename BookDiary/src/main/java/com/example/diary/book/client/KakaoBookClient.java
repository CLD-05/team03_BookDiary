package com.example.diary.book.client;
import com.example.diary.book.dto.KakaoBookSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@Component
@RequiredArgsConstructor
public class KakaoBookClient {

    private final RestTemplate restTemplate;

    @Value("${kakao.api.rest-key}") // 카카오 REST API 키
    private String restApiKey;

    @Value("${kakao.book-search.url}") // 카카오 책 검색 URL
    private String bookSearchUrl;

    public KakaoBookSearchResponse searchBooks(String keyword) {
        // 1. 요청 URL 생성
        String url = UriComponentsBuilder
                .fromHttpUrl(bookSearchUrl)
                .queryParam("query", keyword) // 검색 키워드
                .queryParam("size", 10) // 결과 개수
                .build()
                .toUriString();
        // 2. HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + restApiKey);
        // 카카오 API는 이 형식으로 인증 필요

        // 3. 요청 객체 생성
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // 4. 실제 API 호출
        ResponseEntity<KakaoBookSearchResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                KakaoBookSearchResponse.class
        );

        return response.getBody();
    }
}