package com.example.diary.ailog.repository;

import com.example.diary.ailog.entity.TbAiLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AiLogRepository extends JpaRepository<TbAiLog, Long> {

    // 특정 유저의 AI 로그 조회
    List<TbAiLog> findByUser_IdxUserOrderByCreateDateDesc(Long idxUser);

    @Query("select coalesce(sum(a.totalTokens), 0) from TbAiLog a")
    Long sumTotalTokens();
}