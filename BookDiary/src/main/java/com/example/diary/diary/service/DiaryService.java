package com.example.diary.diary.service;

import com.example.diary.book.entity.TbBook;
import com.example.diary.book.repository.BookRepository;
import com.example.diary.common.exception.CustomException;
import com.example.diary.diary.dto.CreateDiaryRequest;
import com.example.diary.diary.dto.UpdateDiaryRequest;
import com.example.diary.diary.dto.DiaryResponseDto;
import com.example.diary.diary.entity.TbDiary;
import com.example.diary.diary.repository.DiaryRepository;
import com.example.diary.user.entity.TbUser;
import com.example.diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

		TbUser user = userRepository.findById(userIdx).orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

		String normalizedIsbn = normalizeIsbn(request.getIsbn());

		TbBook book = bookRepository.findByIsbn(normalizedIsbn).orElseGet(() -> saveBook(request, normalizedIsbn));

		if (diaryRepository.existsByUserAndBook(user, book)) {
			throw CustomException.badRequest("이미 서재에 등록된 책입니다.");
		}

		TbDiary diary = TbDiary.builder().startDate(request.getStartDate()).endDate(request.getEndDate())
				.rating(request.getRating()).favorite(request.getFavorite() != null ? request.getFavorite() : false)
				.memoTitle(request.getMemoTitle()).memoContent(request.getMemoContent()).status(request.getStatus())
				.createDate(LocalDateTime.now()).book(book).user(user).build();

		TbDiary savedDiary = diaryRepository.save(diary);

		return DiaryResponseDto.fromEntity(savedDiary);
	}

	private TbBook saveBook(CreateDiaryRequest request, String normalizedIsbn) {
		TbBook book = TbBook.builder().isbn(normalizedIsbn).title(request.getTitle()).author(request.getAuthor())
				.publisher(request.getPublisher()).imageUrl(request.getImageUrl())
				.publishDate(request.getPublishDate()).build();

		return bookRepository.save(book);
	}

	@Transactional
	public DiaryResponseDto updateDiary(Long userIdx, Long diaryId, UpdateDiaryRequest request) {
		TbDiary diary = diaryRepository.findByIdxDiaryAndUser_IdxUser(diaryId, userIdx)
				.orElseThrow(() -> CustomException.notFound("서재 정보를 찾을 수 없습니다."));

		// 날짜 유효성 검사 추가
		if (request.getStartDate() != null && request.getEndDate() != null) {
			if (request.getEndDate().isBefore(request.getStartDate())) {
				throw CustomException.badRequest("완료일은 시작일보다 이전일 수 없습니다.");
			}
		}

		diary.update(request.getStatus(), request.getStartDate(), request.getEndDate(), request.getRating(),
				request.getFavorite(), request.getMemoTitle(), request.getMemoContent());

		return DiaryResponseDto.fromEntity(diary);
	}

	// AI용 List 반환 (기존 유지)
	public List<DiaryResponseDto> getDiaryList(Long userIdx) {
		userRepository.findById(userIdx).orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
		return diaryRepository.findAllByUser_IdxUserOrderByIdxDiaryDesc(userIdx).stream()
				.map(DiaryResponseDto::fromEntity).toList();
	}

	// 페이지네이션용 Page 반환
	public Page<DiaryResponseDto> getDiaryList(Long userIdx, String keyword, Pageable pageable) {
		userRepository.findById(userIdx).orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));
		Page<TbDiary> diaryPage;
		if (keyword != null && !keyword.isBlank()) {
			diaryPage = diaryRepository.findAllByUser_IdxUserAndBook_TitleContaining(userIdx, keyword, pageable);
		} else {
			diaryPage = diaryRepository.findAllByUser_IdxUser(userIdx, pageable);
		}
		return diaryPage.map(DiaryResponseDto::fromEntity);
	}

	public DiaryResponseDto getDiaryDetail(Long userIdx, Long diaryId) {
		userRepository.findById(userIdx).orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

		TbDiary diary = diaryRepository.findByIdxDiaryAndUser_IdxUser(diaryId, userIdx)
				.orElseThrow(() -> CustomException.notFound("서재 정보를 찾을 수 없습니다."));

		return DiaryResponseDto.fromEntity(diary); // 변경
	}

	// 서재 삭제
	@Transactional
	public void deleteDiary(Long userIdx, Long diaryId) {
		userRepository.findById(userIdx).orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

		TbDiary diary = diaryRepository.findByIdxDiaryAndUser_IdxUser(diaryId, userIdx)
				.orElseThrow(() -> CustomException.notFound("서재 정보를 찾을 수 없습니다."));

		diaryRepository.delete(diary);
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
		if (isbn == null)
			return null;
		// 공백으로 분리 후 첫 번째 ISBN만 사용, 하이픈 제거
		return isbn.trim().split(" ")[0].replace("-", "").trim();
	}
}