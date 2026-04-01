package com.example.diary.highlight.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.common.dto.PageResponse;
import com.example.diary.common.exception.CustomException;
import com.example.diary.highlight.dto.HighlightRequestDto;
import com.example.diary.highlight.dto.HighlightResponseDto;
import com.example.diary.highlight.dto.HighlightUpdateRequestDto;
import com.example.diary.highlight.service.HighlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/highlights")
public class HighlightApiController extends BaseController {

    private final HighlightService highlightService;

    @GetMapping("/all")
    public ResponseEntity<PageResponse<HighlightResponseDto>> getAllHighlightList(
            @RequestParam(defaultValue = "1") int page
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        Pageable pageable = getPageable(page, 5);
        Page<HighlightResponseDto> result = highlightService.getAllHighlightList(userIdx, pageable);
        return toPageResponse(result);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HighlightResponseDto>>> getHighlightList(
            @RequestParam Long diaryId
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ok(highlightService.getHighlightList(userIdx, diaryId));
    }

    @GetMapping("/{highlightId}")
    public ResponseEntity<ApiResponse<HighlightResponseDto>> getHighlight(
            @PathVariable Long highlightId
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ok(highlightService.getHighlight(userIdx, highlightId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HighlightResponseDto>> createHighlight(
            @RequestBody HighlightRequestDto request
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ok(highlightService.createHighlight(userIdx, request));
    }

    @PutMapping("/{highlightId}")
    public ResponseEntity<ApiResponse<HighlightResponseDto>> updateHighlight(
            @PathVariable Long highlightId,
            @RequestBody HighlightUpdateRequestDto request
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        return ok(highlightService.updateHighlight(userIdx, highlightId, request));
    }

    @DeleteMapping("/{highlightId}")
    public ResponseEntity<ApiResponse<Void>> deleteHighlight(
            @PathVariable Long highlightId
    ) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) throw CustomException.unauthorized("로그인이 필요합니다.");
        highlightService.deleteHighlight(userIdx, highlightId);
        return ok(null);
    }
}