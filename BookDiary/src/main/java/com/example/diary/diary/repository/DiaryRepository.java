package com.example.diary.diary.repository;

import com.example.diary.diary.entity.TbDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<TbDiary, Long> {
    List<TbDiary> findAllByUser_IdxUserOrderByIdxDiaryDesc(Long userId);
}