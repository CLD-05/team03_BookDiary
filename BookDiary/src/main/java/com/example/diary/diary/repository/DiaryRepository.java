package com.example.diary.diary.repository;

import com.example.diary.book.entity.TbBook;
import com.example.diary.diary.entity.TbDiary;
import com.example.diary.user.entity.TbUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<TbDiary, Long> {
    // 목록 조회
    List<TbDiary> findAllByUser_IdxUserOrderByIdxDiaryDesc(Long userId);
    // 상세 조회
    Optional<TbDiary> findByIdxDiaryAndUser_IdxUser(Long diaryId, Long userIdx);
    boolean existsByUserAndBook(TbUser user, TbBook book);
}