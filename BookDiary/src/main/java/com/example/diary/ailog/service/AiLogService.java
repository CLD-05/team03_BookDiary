package com.example.diary.ailog.service;

import com.example.diary.ailog.dto.*;
import com.example.diary.ailog.entity.TbAiLog;
import com.example.diary.ailog.repository.AiLogRepository;
import com.example.diary.book.entity.TbBook;
import com.example.diary.diary.entity.TbDiary;
import com.example.diary.diary.repository.DiaryRepository;
import com.example.diary.user.entity.TbUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

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

    // AI 책 추천
    public AiLogResponseDto getRecommendations(AiLogRequestDto request, TbUser user) {
        List<TbDiary> diaries = diaryRepository.findAllByUser_IdxUserOrderByIdxDiaryDesc(user.getIdxUser());
        List<String> readBooks = diaries.stream()
                .map(d -> d.getBook().getTitle() + " (저자: " + d.getBook().getAuthor() + ")")
                .collect(Collectors.toList());

        String readBooksStr = readBooks.isEmpty() ? "없음" : String.join(", ", readBooks);

        String prompt = String.format("""
                사용자가 읽은 책 목록: %s
                선호 카테고리: %s
                
                위 독서 이력과 카테고리를 바탕으로 책 5권을 추천해줘.
                반드시 한국 서점(교보문고, 예스24 등)에서 구매 가능한 책만 추천해줘.
                한국어로 번역 출판된 책이나 한국 작가의 책을 우선 추천해줘.
                추천 이유는 사용자의 독서 이력을 언급해서 3줄 이상의 분량으로 작성해줘.
                반드시 아래 JSON 형식으로만 응답해줘. 다른 텍스트 없이 JSON만:
                {"recommendations": [{"title": "책 제목", "author": "저자", "reason": "추천 이유"}]}
                """, readBooksStr, request.getCategory());

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        log.info("Gemini 추천 응답: {}", response);

        List<AiLogResponseDto.BookItem> books = new ArrayList<>();
        try {
            String cleaned = response.replaceAll("```json|```", "").trim();
            Map<String, Object> result = objectMapper.readValue(cleaned, Map.class);
            List<Map<String, String>> recommendations =
                    (List<Map<String, String>>) result.get("recommendations");

            if (recommendations != null) {
                for (Map<String, String> rec : recommendations) {
                    String title = rec.get("title");
                    String author = rec.get("author");
                    String reason = rec.get("reason");

                    log.info("추천 책 처리 중: {} - {}", title, author);

                    // 카카오 교차검증 시도
                    AiLogResponseDto.BookItem verified =
                            bookSearchService.verifyAndEnrichBook(title, author, reason);

                    if (verified != null) {
                        log.info("카카오 검증 성공: {}", title);
                        books.add(verified);
                    } else {
                        log.info("카카오 검증 실패, Gemini 결과 사용: {}", title);
                        books.add(AiLogResponseDto.BookItem.builder()
                                .title(title)
                                .author(author)
                                .publisher(null)
                                .isbn(null)
                                .imageUrl(null)
                                .reason(reason)
                                .build());
                    }
                }
            }
        } catch (Exception e) {
            log.error("추천 파싱 실패: {}", e.getMessage());
        }

        log.info("최종 추천 결과 수: {}", books.size());

        saveAiLog(user, null, prompt, Map.of("recommendations", books));

        return AiLogResponseDto.builder()
                .books(books)
                .build();
    }

    // 월간 독서 리포트
    public AiReportResponseDto getMonthlyReport(TbUser user) {
        List<TbDiary> diaries = diaryRepository.findAllByUser_IdxUserOrderByIdxDiaryDesc(user.getIdxUser());

        long totalBooks = diaries.size();
        long doneBooks = diaries.stream().filter(d -> d.getStatus().name().equals("DONE")).count();
        long readingBooks = diaries.stream().filter(d -> d.getStatus().name().equals("READING")).count();
        long wantBooks = diaries.stream().filter(d -> d.getStatus().name().equals("WANT")).count();

        String bookList = diaries.stream()
                .map(d -> "- " + d.getBook().getTitle() + " (" + d.getStatus().name() + ")")
                .collect(Collectors.joining("\n"));

        String prompt = String.format("""
                사용자의 독서 현황:
                - 전체 등록 도서: %d권
                - 완독: %d권
                - 읽는 중: %d권
                - 읽고 싶어요: %d권
                
                도서 목록:
                %s
                
                위 독서 현황을 바탕으로 한국어로 월간 독서 리포트를 작성해줘.
                칭찬과 격려를 포함하고, 다음 독서 목표도 제안해줘.
                반드시 아래 JSON 형식으로만 응답해줘. 다른 텍스트 없이 JSON만:
                {"report": "리포트 내용"}
                """, totalBooks, doneBooks, readingBooks, wantBooks, bookList);

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        saveAiLog(user, null, prompt, Map.of("report", response));

        try {
            String cleaned = response.replaceAll("```json|```", "").trim();
            Map<String, String> result = objectMapper.readValue(cleaned, Map.class);
            return new AiReportResponseDto(
                    result.getOrDefault("report", "리포트를 가져올 수 없습니다.")
            );
        } catch (Exception e) {
            log.error("리포트 파싱 실패: {}", e.getMessage());
            return new AiReportResponseDto(response);
        }
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