package com.example.diary.diary.service;

import com.example.diary.book.entity.TbBook;
import com.example.diary.book.repository.BookRepository;
import com.example.diary.common.exception.CustomException;
import com.example.diary.diary.dto.CreateDiaryRequest;
import com.example.diary.diary.dto.DiaryResponseDto;
import com.example.diary.diary.entity.TbDiary;
import com.example.diary.diary.repository.DiaryRepository;
import com.example.diary.user.entity.TbUser;
import com.example.diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public DiaryResponseDto createDiary(Long userIdx, CreateDiaryRequest request) {

        validateRequest(request);

        TbUser user = userRepository.findById(userIdx)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

        String normalizedIsbn = normalizeIsbn(request.getIsbn());

        TbBook book = bookRepository.findByIsbn(normalizedIsbn)
                .orElseGet(() -> saveBook(request, normalizedIsbn));

        if (diaryRepository.existsByUserAndBook(user, book)) {
            throw CustomException.badRequest("이미 서재에 등록된 책입니다.");
        }

        TbDiary diary = TbDiary.builder()
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .rating(request.getRating())
                .favorite(request.getFavorite() != null ? request.getFavorite() : false)
                .memoTitle(request.getMemoTitle())
                .memoContent(request.getMemoContent())
                .status(request.getStatus())
                .createDate(LocalDateTime.now())
                .book(book)
                .user(user)
                .build();

        TbDiary savedDiary = diaryRepository.save(diary);

        return toDiaryResponseDto(savedDiary);
    }

    private TbBook saveBook(CreateDiaryRequest request, String normalizedIsbn) {
        TbBook book = TbBook.builder()
                .isbn(normalizedIsbn)
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .totalPage(request.getTotalPage())
                .publishDate(request.getPublishDate())
                .build();

        return bookRepository.save(book);
    }

    // 서재 목록 조회
    public List<DiaryResponseDto> getDiaryList(Long userIdx) {
        userRepository.findById(userIdx)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

        List<TbDiary> diaryList = diaryRepository.findAllByUser_IdxUserOrderByIdxDiaryDesc(userIdx);

        return diaryList.stream()
                .map(this::toDiaryResponseDto)
                .toList();
    }

    // 서재 상세 조회
    public DiaryResponseDto getDiaryDetail(Long userIdx, Long diaryId) {
        userRepository.findById(userIdx)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

        TbDiary diary = diaryRepository.findByIdxDiaryAndUser_IdxUser(diaryId, userIdx)
                .orElseThrow(() -> CustomException.notFound("서재 정보를 찾을 수 없습니다."));

        return toDiaryResponseDto(diary);
    }
    // 서재 삭제
    @Transactional
    public void deleteDiary(Long userIdx, Long diaryId) {
        userRepository.findById(userIdx)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

        TbDiary diary = diaryRepository.findByIdxDiaryAndUser_IdxUser(diaryId, userIdx)
                .orElseThrow(() -> CustomException.notFound("서재 정보를 찾을 수 없습니다."));

        diaryRepository.delete(diary);
    }

    // 공통 DTO 변환 메서드
    private DiaryResponseDto toDiaryResponseDto(TbDiary diary) {
        TbBook book = diary.getBook();

        return DiaryResponseDto.builder()
                .diaryId(diary.getIdxDiary())
                .bookId(book.getIdxBook())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .imageUrl(book.getImageUrl())
                .startDate(diary.getStartDate())
                .endDate(diary.getEndDate())
                .rating(diary.getRating())
                .favorite(diary.getFavorite())
                .memoTitle(diary.getMemoTitle())
                .memoContent(diary.getMemoContent())
                .status(diary.getStatus())
                .createDate(diary.getCreateDate())
                .build();
    }

    // 요청 데이터 유효성 검증
    // - 시작일이 종료일보다 늦으면 잘못된 데이터이므로 예외 발생
    private void validateRequest(CreateDiaryRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null
                && request.getStartDate().isAfter(request.getEndDate())) {
            throw CustomException.badRequest("시작일은 종료일보다 늦을 수 없습니다.");
        }
    }
    // ISBN 정규화
    // - 하이픈(-) 제거 + 공백 제거
    // - 동일한 책이 다른 형태로 저장되는 것 방지 (중복 방지 목적)
    private String normalizeIsbn(String isbn) {
        return isbn.replace("-", "").trim();
    }
}