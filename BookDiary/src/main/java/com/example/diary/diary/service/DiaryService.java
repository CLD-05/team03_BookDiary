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

        return DiaryResponseDto.builder()
                .diaryId(savedDiary.getIdxDiary())
                .bookId(book.getIdxBook())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .imageUrl(book.getImageUrl())
                .startDate(savedDiary.getStartDate())
                .endDate(savedDiary.getEndDate())
                .rating(savedDiary.getRating())
                .favorite(savedDiary.getFavorite())
                .memoTitle(savedDiary.getMemoTitle())
                .memoContent(savedDiary.getMemoContent())
                .status(savedDiary.getStatus())
                .createDate(savedDiary.getCreateDate())
                .build();
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

    private void validateRequest(CreateDiaryRequest request) {
        if (request.getStartDate() != null && request.getEndDate() != null
                && request.getStartDate().isAfter(request.getEndDate())) {
            throw CustomException.badRequest("시작일은 종료일보다 늦을 수 없습니다.");
        }
    }

    private String normalizeIsbn(String isbn) {
        return isbn.replace("-", "").trim();
    }
}