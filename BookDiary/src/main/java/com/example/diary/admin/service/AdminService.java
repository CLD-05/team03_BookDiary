package com.example.diary.admin.service;

import com.example.diary.admin.dto.AdminRequestDto;
import com.example.diary.admin.dto.AdminResponseDto;
import com.example.diary.user.entity.TbUser;
import com.example.diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    // 회원 리스트 조회
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

    // 회원 상태 변경
    @Transactional
    public void updateUserStatus(Long userId, AdminRequestDto.UpdateUserStatus requestDto) {

        TbUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 없습니다."));

        TbUser.Status status = TbUser.Status.valueOf(requestDto.getStatus().toUpperCase());

        user.updateStatus(status);
    }

    public AdminResponseDto.AiTokenInfo getAiTokenInfo() {
        return null;
    }

    public void updateAiToken(AdminRequestDto.UpdateAiToken requestDto) {
    }
}