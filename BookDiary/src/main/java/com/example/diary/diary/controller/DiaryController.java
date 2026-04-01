package com.example.diary.diary.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.common.exception.CustomException;
import com.example.diary.diary.dto.CreateDiaryRequest;
import com.example.diary.diary.dto.DiaryResponseDto;
import com.example.diary.diary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController extends BaseController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<ApiResponse<DiaryResponseDto>> createDiary(
            @Valid @RequestBody CreateDiaryRequest request
    ) {
//        Long userIdx = getCurrentUserIdx();
        Long userIdx = 1L; // 테스트용: DB에 직접 넣은 유저 PK (나중에 지우기)
        if (userIdx == null) {
            throw CustomException.unauthorized("로그인이 필요합니다.");
        }

        DiaryResponseDto response = diaryService.createDiary(userIdx, request);
        return ok(response);
    }
}
