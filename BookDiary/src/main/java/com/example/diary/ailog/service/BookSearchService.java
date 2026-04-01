package com.example.diary.ailog.service;

import com.example.diary.ailog.config.KakaoBookConfig;
import com.example.diary.ailog.dto.AiLogResponseDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class BookSearchService {

    private final RestTemplate restTemplate;
    private final KakaoBookConfig kakaoBookConfig;
    private final ObjectMapper objectMapper;


    // Gemini가 추천한 책을 Kakao로 교차검증
    public AiLogResponseDto.BookItem verifyAndEnrichBook(
            String title, String author, String reason) {
        try {
            String query = title + " " + author;
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            String url = "https://dapi.kakao.com/v3/search/book?query=" + encodedQuery + "&size=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoBookConfig.getKakaoApiKey());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode documents = root.path("documents");

            // 카카오에서 찾지 못한 책은 제외
            if (documents.isEmpty()) {
                log.warn("Kakao에서 찾지 못한 책: {} - {}", title, author);
                return null;
            }

            JsonNode book = documents.get(0);

            // authors 배열에서 첫 번째 저자 추출
            String kakaoAuthor = book.path("authors").isEmpty()
                    ? author
                    : book.path("authors").get(0).asText();

            return AiLogResponseDto.BookItem.builder()
                    .title(book.path("title").asText())
                    .author(kakaoAuthor)
                    .publisher(book.path("publisher").asText())
                    .isbn(book.path("isbn").asText())
                    .imageUrl(book.path("thumbnail").asText())
                    .reason(reason)
                    .build();

        } catch (Exception e) {
            log.error("Kakao 교차검증 실패 - title: {}, error: {}", title, e.getMessage());
            return null;
        }
    }
}
