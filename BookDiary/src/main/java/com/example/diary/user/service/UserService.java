package com.example.diary.user.service;

import com.example.diary.common.config.JwtService;
import com.example.diary.common.exception.CustomException;
import com.example.diary.user.dto.*;
import com.example.diary.user.entity.TbUser;
import com.example.diary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void signup(SignupRequestDto dto) {
        if (userRepository.existsByUserId(dto.getUserId())) {
            throw CustomException.badRequest("이미 존재하는 아이디입니다.");
        }
        userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPass())));
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        TbUser user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> CustomException.badRequest("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getPass(), user.getPass())) {
            throw CustomException.badRequest("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtService.generateToken(user.getUserId(), user.getRole().name(), user.getIdxUser());
        return new LoginResponseDto(token, user.getRole().name());
    }

    @Transactional
    public void updateProfile(Long idxUser, UserUpdateDto dto) {
        TbUser user = userRepository.findById(idxUser)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(dto.getCurrentPass(), user.getPass())) {
            throw CustomException.badRequest("현재 비밀번호가 일치하지 않습니다.");
        }

        user.updateProfile(dto.getName(), dto.getEmail());

        if (dto.getNewPass() != null && !dto.getNewPass().isBlank()) {
            user.updatePassword(passwordEncoder.encode(dto.getNewPass()));
        }
    }

    @Transactional
    public void withdraw(Long idxUser) {
        userRepository.deleteById(idxUser);
    }
}