package com.example.diary.diary.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.PageResponse;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.common.exception.CustomException;
import com.example.diary.diary.dto.CreateDiaryRequest;
import com.example.diary.diary.dto.UpdateDiaryRequest;
import com.example.diary.diary.dto.DiaryResponseDto;
import com.example.diary.diary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryApiController extends BaseController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<ApiResponse<DiaryResponseDto>> createDiary(
            @Valid @RequestBody CreateDiaryRequest request
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ok(diaryService.createDiary(userIdx, request));
    }
    
    @PutMapping("/{diaryId}")
    public ResponseEntity<ApiResponse<DiaryResponseDto>> updateDiary(
            @PathVariable Long diaryId,
            @RequestBody UpdateDiaryRequest request
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ok(diaryService.updateDiary(userIdx, diaryId, request));
    }

    @GetMapping
    public ResponseEntity<PageResponse<DiaryResponseDto>> getDiaryList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        
        Pageable pageable = getPageable(page);
        Page<DiaryResponseDto> result = diaryService.getDiaryList(userIdx, keyword, pageable);
        return toPageResponse(result);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<ApiResponse<DiaryResponseDto>> getDiaryDetail(
            @PathVariable Long diaryId
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ok(diaryService.getDiaryDetail(userIdx, diaryId));
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<ApiResponse<Void>> deleteDiary(
            @PathVariable Long diaryId
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        diaryService.deleteDiary(userIdx, diaryId);
        return ok(null);
    }
}