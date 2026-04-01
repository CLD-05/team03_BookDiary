package com.example.diary.highlight.repository;

import com.example.diary.highlight.entity.TbHighlight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HighlightRepository extends JpaRepository<TbHighlight, Long> {

    List<TbHighlight> findAllByDiary_IdxDiaryOrderByPageNumberAsc(Long diaryId);

    List<TbHighlight> findAllByDiary_User_IdxUserOrderByCreateDateDesc(Long userIdx);

    @Query(value = "SELECT h FROM TbHighlight h JOIN FETCH h.diary d JOIN FETCH d.book WHERE d.user.idxUser = :userIdx",
           countQuery = "SELECT COUNT(h) FROM TbHighlight h JOIN h.diary d WHERE d.user.idxUser = :userIdx")
    Page<TbHighlight> findAllByDiary_User_IdxUser(@Param("userIdx") Long userIdx, Pageable pageable);
}