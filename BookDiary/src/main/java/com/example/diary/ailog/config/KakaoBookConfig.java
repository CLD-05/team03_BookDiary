package com.example.diary.ailog.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class KakaoBookConfig {

    @Value("${kakao.api-key}")
    private String kakaoApiKey;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getKakaoApiKey() {
        return kakaoApiKey;
    }
}