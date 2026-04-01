package com.example.diary.ailog.service;

import com.example.diary.ailog.dto.*;
import com.example.diary.ailog.entity.TbAiLog;
import com.example.diary.ailog.repository.AiLogRepository;
import com.example.diary.user.entity.TbUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiLogService {

    private final ChatClient chatClient;
    private final BookSearchService bookSearchService;
    private final AiLogRepository aiLogRepository;
    private final ObjectMapper objectMapper;

}
