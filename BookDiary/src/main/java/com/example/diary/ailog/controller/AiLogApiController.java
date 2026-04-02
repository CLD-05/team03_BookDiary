package com.example.diary.ailog.controller;

import com.example.diary.ailog.dto.*;
import com.example.diary.ailog.service.AiLogService;
import com.example.diary.common.controller.BaseController;
import com.example.diary.common.exception.CustomException;
import com.example.diary.user.entity.TbUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiLogApiController extends BaseController {

    private final AiLogService aiLogService;

    @PostMapping("/recommend")
    public ResponseEntity<?> getAiRecommend(@RequestBody AiLogRequestDto request) {
        TbUser user = getCurrentUser();
        if (user == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        try {
            return ResponseEntity.ok(aiLogService.getRecommendations(request, user));
        } catch (Exception e) {
            log.error("AI 추천 실패: {}", e.getMessage());
            return ResponseEntity.ok(Map.of(
                "books", List.of(),
                "message", "AI 서비스가 일시적으로 사용량 한도에 도달했습니다. 잠시 후 다시 시도해주세요."
            ));
        }
    }

    @PostMapping("/summary")
    public ResponseEntity<?> getBookSummary(@RequestBody AiSummaryRequestDto request) {
        TbUser user = getCurrentUser();
        if (user == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        try {
            return ResponseEntity.ok(aiLogService.getSummary(request, user));
        } catch (Exception e) {
            log.error("AI 요약 실패: {}", e.getMessage());
            return ResponseEntity.ok(Map.of(
                "summary", "AI 서비스가 일시적으로 사용량 한도에 도달했습니다. 잠시 후 다시 시도해주세요."
            ));
        }
    }

    @PostMapping("/report")
    public ResponseEntity<?> getMonthlyReport() {
        TbUser user = getCurrentUser();
        if (user == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        try {
            return ResponseEntity.ok(aiLogService.getMonthlyReport(user));
        } catch (Exception e) {
            log.error("AI 리포트 실패: {}", e.getMessage());
            return ResponseEntity.ok(Map.of(
                "report", "AI 서비스가 일시적으로 사용량 한도에 도달했습니다. 잠시 후 다시 시도해주세요."
            ));
        }
    }
}