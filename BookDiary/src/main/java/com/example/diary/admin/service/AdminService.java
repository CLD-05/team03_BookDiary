package com.example.diary.admin.service;

import com.example.diary.admin.dto.AdminRequestDto;
import com.example.diary.admin.dto.AdminResponseDto;
import com.example.diary.ailog.repository.AiLogRepository;
import com.example.diary.user.entity.TbUser;
import com.example.diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final AiLogRepository aiLogRepository;

    @Value("${spring.ai.google.genai.api-key}")
    private String geminiApiKey;

    public AdminResponseDto.UserList getUserList() {
        List<AdminResponseDto.UserInfo> users = userRepository.findAll().stream()
                .map(user -> new AdminResponseDto.UserInfo(
                        user.getIdxUser(),
                        user.getEmail(),
                        user.getName(),
                        user.getStatus().name(),
                        user.getRole().name()
                ))
                .toList();
        return new AdminResponseDto.UserList(users);
    }

    @Transactional
    public void updateUserStatus(Long userId, AdminRequestDto.UpdateUserStatus requestDto) {
        TbUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));
        TbUser.Status status = TbUser.Status.valueOf(requestDto.getStatus().toUpperCase());
        user.updateStatus(status);
    }

    public AdminResponseDto.AiTokenInfo getAiTokenInfo() {
        long totalUsed = aiLogRepository.count();
        return new AdminResponseDto.AiTokenInfo(
                geminiApiKey,
                1000,
                totalUsed,
                1000 - totalUsed
        );
    }

    public void updateAiToken(AdminRequestDto.UpdateAiToken requestDto) {
    }
}