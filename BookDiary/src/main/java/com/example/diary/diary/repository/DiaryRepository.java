package com.example.diary.diary.repository;

import com.example.diary.book.entity.TbBook;
import com.example.diary.diary.entity.TbDiary;
import com.example.diary.user.entity.TbUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<TbDiary, Long> {

	// AI용 - List 반환 (AiLogService에서 사용)
	@Query("SELECT d FROM TbDiary d JOIN FETCH d.book WHERE d.user.idxUser = :userIdx ORDER BY d.idxDiary DESC")
	List<TbDiary> findAllByUser_IdxUserOrderByIdxDiaryDesc(@Param("userIdx") Long userIdx);

	@Query(value = "SELECT d FROM TbDiary d JOIN FETCH d.book WHERE d.user.idxUser = :userIdx", countQuery = "SELECT COUNT(d) FROM TbDiary d WHERE d.user.idxUser = :userIdx")
	Page<TbDiary> findAllByUser_IdxUser(@Param("userIdx") Long userIdx, Pageable pageable);

	@Query(value = "SELECT d FROM TbDiary d JOIN FETCH d.book WHERE d.user.idxUser = :userIdx AND d.book.title LIKE %:keyword%", countQuery = "SELECT COUNT(d) FROM TbDiary d WHERE d.user.idxUser = :userIdx AND d.book.title LIKE %:keyword%")
	Page<TbDiary> findAllByUser_IdxUserAndBook_TitleContaining(@Param("userIdx") Long userIdx,
			@Param("keyword") String keyword, Pageable pageable);

	Optional<TbDiary> findByIdxDiaryAndUser_IdxUser(Long diaryId, Long userIdx);

	boolean existsByUserAndBook(TbUser user, TbBook book);
}