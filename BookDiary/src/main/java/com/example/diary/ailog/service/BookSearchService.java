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

}
