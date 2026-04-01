package com.example.diary.ailog.service;

import com.example.diary.ailog.dto.*;
import com.example.diary.ailog.entity.TbAiLog;
import com.example.diary.ailog.repository.AiLogRepository;
import com.example.diary.book.entity.TbBook;
import com.example.diary.diary.entity.TbDiary;
import com.example.diary.user.entity.TbUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import com.example.diary.diary.repository.DiaryRepository;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiLogService {

    private final ChatClient chatClient;
    private final BookSearchService bookSearchService;
    private final AiLogRepository aiLogRepository;
    private final ObjectMapper objectMapper;
    private final DiaryRepository diaryRepository;


    // 줄거리 요약
    public AiSummaryResponseDto getSummary(AiSummaryRequestDto request, TbUser user) {
        String prompt = String.format("""
                도서 정보:
                - 제목: %s
                - 저자: %s
                
                위 책의 줄거리를 한국어로 7문장 분량으로 요약해줘.
                반드시 아래 JSON 형식으로만 응답해줘. 다른 텍스트 없이 JSON만:
                {"title": "책 제목", "summary": "줄거리 요약"}
                """, request.getTitle(), request.getAuthor());

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        saveAiLog(user, null, prompt, Map.of("summary", response));

        try {
            String cleaned = response.replaceAll("```json|```", "").trim();
            Map<String, String> result = objectMapper.readValue(cleaned, Map.class);
            return new AiSummaryResponseDto(
                    result.getOrDefault("title", request.getTitle()),
                    request.getAuthor(),
                    result.getOrDefault("summary", "요약을 가져올 수 없습니다.")
            );
        } catch (Exception e) {
            log.error("줄거리 파싱 실패: {}", e.getMessage());
            return new AiSummaryResponseDto(request.getTitle(), request.getAuthor(), response);
        }
    }

    // AI 책 추천 (Gemini + Kakao 교차검증)
    public AiLogResponseDto getRecommendations(AiLogRequestDto request, TbUser user) {

        // tbDiary에서 로그인한 유저의 독서 이력 조회
        List<TbDiary> diaries = diaryRepository.findAllByUser_IdxUserOrderByIdxDiaryDesc(user.getIdxUser());
        List<String> readBooks = diaries.stream()
                .map(d -> d.getBook().getTitle() + " (저자: " + d.getBook().getAuthor() + ")")
                .collect(Collectors.toList());

        // 독서 이력 없을 경우
        String readBooksStr = readBooks.isEmpty() ? "없음" : String.join(", ", readBooks);

        String prompt = String.format("""
                사용자가 읽은 책 목록: %s
                선호 카테고리: %s
                
                위 독서 이력과 카테고리를 바탕으로 책 5권을 추천해줘.
                추천 이유는 사용자의 독서 이력을 언급해서 3줄 이상의 분량으로 작성해줘.
                반드시 아래 JSON 형식으로만 응답해줘. 다른 텍스트 없이 JSON만:
                {"recommendations": [{"title": "책 제목", "author": "저자", "reason": "추천 이유"}]}
                """, readBooksStr, request.getCategory());

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();


        // Kakao 교차검증
        List<AiLogResponseDto.BookItem> verifiedBooks = new ArrayList<>();
        try {
            String cleaned = response.replaceAll("```json|```", "").trim();
            Map<String, Object> result = objectMapper.readValue(cleaned, Map.class);
            List<Map<String, String>> recommendations =
                    (List<Map<String, String>>) result.get("recommendations");

            for (Map<String, String> rec : recommendations) {
                AiLogResponseDto.BookItem verified = bookSearchService.verifyAndEnrichBook(
                        rec.get("title"), rec.get("author"), rec.get("reason"));
                if (verified != null) {
                    verifiedBooks.add(verified);
                }
            }
        } catch (Exception e) {
            log.error("추천 파싱 실패: {}", e.getMessage());
        }

        saveAiLog(user, null, prompt, Map.of("recommendations", verifiedBooks));

        return AiLogResponseDto.builder()
                .books(verifiedBooks)
                .build();
    }

    // tbAiLog 저장 공통 메서드
    private void saveAiLog(TbUser user, TbBook book, String promptSummary, Map<String, Object> result) {
        TbAiLog aiLog = TbAiLog.builder()
                .user(user)
                .book(book)
                .promptSummary(promptSummary)
                .result(result)
                .build();
        aiLogRepository.save(aiLog);
    }
}