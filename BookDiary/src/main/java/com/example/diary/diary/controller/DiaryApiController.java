package com.example.diary.diary.controller;

import com.example.diary.book.service.BookService;
import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.common.exception.CustomException;
import com.example.diary.diary.dto.CreateDiaryRequest;
import com.example.diary.diary.dto.DiaryResponseDto;
import com.example.diary.diary.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiaryResponseDto>>> getDiaryList() {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ok(diaryService.getDiaryList(userIdx));
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