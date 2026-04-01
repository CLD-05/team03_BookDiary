package com.example.diary.highlight.service;

import com.example.diary.common.exception.CustomException;
import com.example.diary.diary.entity.TbDiary;
import com.example.diary.diary.repository.DiaryRepository;
import com.example.diary.highlight.dto.HighlightRequestDto;
import com.example.diary.highlight.dto.HighlightResponseDto;
import com.example.diary.highlight.dto.HighlightUpdateRequestDto;
import com.example.diary.highlight.entity.TbHighlight;
import com.example.diary.highlight.repository.HighlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HighlightService {

    private final HighlightRepository highlightRepository;
    private final DiaryRepository diaryRepository;

    // 전체 목록 조회 (페이지네이션)
    public Page<HighlightResponseDto> getAllHighlightList(Long userIdx, Pageable pageable) {
        return highlightRepository.findAllByDiary_User_IdxUser(userIdx, pageable)
                .map(HighlightResponseDto::fromEntity);
    }

    // 전체 목록 조회 (List)
    public List<HighlightResponseDto> getAllHighlightList(Long userIdx) {
        return highlightRepository.findAllByDiary_User_IdxUserOrderByCreateDateDesc(userIdx)
                .stream()
                .map(HighlightResponseDto::fromEntity)
                .toList();
    }

    // 다이어리별 목록 조회
    public List<HighlightResponseDto> getHighlightList(Long userIdx, Long diaryId) {
        TbDiary diary = diaryRepository.findByIdxDiaryAndUser_IdxUser(diaryId, userIdx)
                .orElseThrow(() -> CustomException.notFound("서재 정보를 찾을 수 없습니다."));

        return highlightRepository.findAllByDiary_IdxDiaryOrderByPageNumberAsc(diaryId)
                .stream()
                .map(HighlightResponseDto::fromEntity)
                .toList();
    }

    // 상세 조회
    public HighlightResponseDto getHighlight(Long userIdx, Long highlightId) {
        TbHighlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> CustomException.notFound("하이라이트를 찾을 수 없습니다."));

        diaryRepository.findByIdxDiaryAndUser_IdxUser(
                highlight.getDiary().getIdxDiary(), userIdx)
                .orElseThrow(() -> CustomException.unauthorized("권한이 없습니다."));

        return HighlightResponseDto.fromEntity(highlight);
    }

    // 추가
    @Transactional
    public HighlightResponseDto createHighlight(Long userIdx, HighlightRequestDto request) {
        TbDiary diary = diaryRepository.findByIdxDiaryAndUser_IdxUser(request.getDiaryId(), userIdx)
                .orElseThrow(() -> CustomException.notFound("서재 정보를 찾을 수 없습니다."));

        if (request.getHighlightText() == null || request.getHighlightText().isBlank()) {
            throw CustomException.badRequest("대사를 입력해주세요.");
        }

        TbHighlight highlight = TbHighlight.builder()
                .diary(diary)
                .pageNumber(request.getPageNumber())
                .highlightText(request.getHighlightText())
                .build();

        return HighlightResponseDto.fromEntity(highlightRepository.save(highlight));
    }

    // 수정
    @Transactional
    public HighlightResponseDto updateHighlight(Long userIdx, Long highlightId,
                                                HighlightUpdateRequestDto request) {
        TbHighlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> CustomException.notFound("하이라이트를 찾을 수 없습니다."));

        diaryRepository.findByIdxDiaryAndUser_IdxUser(
                highlight.getDiary().getIdxDiary(), userIdx)
                .orElseThrow(() -> CustomException.unauthorized("권한이 없습니다."));

        if (request.getHighlightText() == null || request.getHighlightText().isBlank()) {
            throw CustomException.badRequest("대사를 입력해주세요.");
        }

        highlight.update(request.getPageNumber(), request.getHighlightText());

        return HighlightResponseDto.fromEntity(highlight);
    }

    // 삭제
    @Transactional
    public void deleteHighlight(Long userIdx, Long highlightId) {
        TbHighlight highlight = highlightRepository.findById(highlightId)
                .orElseThrow(() -> CustomException.notFound("하이라이트를 찾을 수 없습니다."));

        diaryRepository.findByIdxDiaryAndUser_IdxUser(
                highlight.getDiary().getIdxDiary(), userIdx)
                .orElseThrow(() -> CustomException.unauthorized("권한이 없습니다."));

        highlightRepository.delete(highlight);
    }
}