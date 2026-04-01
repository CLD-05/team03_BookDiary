package com.example.diary.ailog.controller;

import com.example.diary.ailog.dto.*;
import com.example.diary.ailog.service.AiLogService;
import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.user.entity.TbUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiLogController extends BaseController {

    private final AiLogService aiLogService;

    // AI 책 추천
    // POST /api/ai/recommend
    @PostMapping("/recommend")
    public ResponseEntity<?> getAiRecommend(@RequestBody AiLogRequestDto request) {
        TbUser user = getCurrentUser();  // BaseController 메서드 사용
        AiLogResponseDto response = aiLogService.getRecommendations(request, user);
        return ok(response);
    }

    // AI 줄거리 요약
    // POST /api/ai/summary
    @PostMapping("/summary")
    public ResponseEntity<?> getBookSummary(@RequestBody AiSummaryRequestDto request) {
        TbUser user = getCurrentUser();  // BaseController 메서드 사용
        AiSummaryResponseDto response = aiLogService.getSummary(request, user);
        return ok(response);
    }
}