package com.example.diary.ailog.controller;

import com.example.diary.ailog.dto.*;
import com.example.diary.ailog.service.AiLogService;
import com.example.diary.common.controller.BaseController;
import com.example.diary.common.exception.CustomException;
import com.example.diary.user.entity.TbUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiLogApiController extends BaseController {

    private final AiLogService aiLogService;

    @PostMapping("/recommend")
    public ResponseEntity<?> getAiRecommend(@RequestBody AiLogRequestDto request) {
        TbUser user = getCurrentUser();
        if (user == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ResponseEntity.ok(aiLogService.getRecommendations(request, user));
    }

    @PostMapping("/summary")
    public ResponseEntity<?> getBookSummary(@RequestBody AiSummaryRequestDto request) {
        TbUser user = getCurrentUser();
        if (user == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ResponseEntity.ok(aiLogService.getSummary(request, user));
    }

    @PostMapping("/report")
    public ResponseEntity<?> getMonthlyReport() {
        TbUser user = getCurrentUser();
        if (user == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ResponseEntity.ok(aiLogService.getMonthlyReport(user));
    }
}