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

    public AiLogResponseDto.BookItem verifyAndEnrichBook(
            String title, String author, String reason) {
        try {
            // 1차: 제목 + 저자
            AiLogResponseDto.BookItem result = searchKakao(title + " " + author, author, reason);
            if (result != null) return result;

            // 2차: 제목만
            log.info("제목만으로 재검색: {}", title);
            result = searchKakao(title, author, reason);
            if (result != null) return result;

            // 3차: 카카오에서 못 찾아도 Gemini 결과 그대로 반환
            log.warn("Kakao에서 찾지 못해 Gemini 결과 사용: {} - {}", title, author);
            return AiLogResponseDto.BookItem.builder()
                    .title(title)
                    .author(author)
                    .publisher(null)
                    .isbn(null)
                    .imageUrl(null)
                    .reason(reason)
                    .build();

        } catch (Exception e) {
            log.error("검증 실패 - title: {}, error: {}", title, e.getMessage());
            return AiLogResponseDto.BookItem.builder()
                    .title(title)
                    .author(author)
                    .publisher(null)
                    .isbn(null)
                    .imageUrl(null)
                    .reason(reason)
                    .build();
        }
    }

    private AiLogResponseDto.BookItem searchKakao(String query, String originalAuthor, String reason) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            String url = "https://dapi.kakao.com/v3/search/book?query=" + encodedQuery + "&size=1";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + kakaoBookConfig.getKakaoApiKey());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class);

            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode documents = root.path("documents");

            if (documents.isEmpty()) return null;

            JsonNode book = documents.get(0);
            String kakaoAuthor = book.path("authors").isEmpty()
                    ? originalAuthor
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
            log.error("Kakao 검색 실패 - query: {}, error: {}", query, e.getMessage());
            return null;
        }
    }
}