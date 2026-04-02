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

import java.time.LocalDateTime; // 날짜 저장을 위해 추가

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

        // 엔티티 변환 시 암호화된 비밀번호와 기본값들을 설정합니다.
        // 이전에 발생했던 CreateDate null 에러를 방지하기 위해 날짜 정보가 포함되어야 합니다.
        userRepository.save(dto.toEntity(passwordEncoder.encode(dto.getPass())));
    }


    public String login(LoginRequestDto dto) {
        // 1. 아이디로 유저 조회 (없으면 '존재하지 않는 아이디' 예외 발생)
        TbUser user = userRepository.findByUserId(dto.getUserId())
                .orElseThrow(() -> CustomException.badRequest("존재하지 않는 아이디입니다."));

        // 2. 비밀번호 일치 여부 확인 (틀리면 '비밀번호 불일치' 예외 발생)
        if (!passwordEncoder.matches(dto.getPass(), user.getPass())) {
            throw CustomException.badRequest("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 시 JWT 토큰 생성 및 반환
        return jwtService.generateToken(user.getUserId(), user.getRole().name(), user.getIdxUser());
    }

    @Transactional
    public void updateProfile(Long idxUser, UserUpdateDto dto) {
        TbUser user = userRepository.findById(idxUser)
                .orElseThrow(() -> CustomException.notFound("사용자를 찾을 수 없습니다."));

        // 1. 본인 확인을 위한 현재 비밀번호 체크
        if (!passwordEncoder.matches(dto.getCurrentPass(), user.getPass())) {
            throw CustomException.badRequest("현재 비밀번호가 일치하지 않습니다.");
        }

        // 2. 이름 및 이메일 수정
        user.updateProfile(dto.getName(), dto.getEmail());

        // 3. 기존 비밀번호와 새 비밀번호 일치하는지 비교
        if (dto.getNewPass() != null && !dto.getNewPass().isBlank()) {
            if (passwordEncoder.matches(dto.getNewPass(), user.getPass())) {
                throw CustomException.badRequest("기존 비밀번호와 동일합니다.");
            }
            user.updatePassword(passwordEncoder.encode(dto.getNewPass()));
        }
    }

    @Transactional
    public void withdraw(Long idxUser) {
        userRepository.deleteById(idxUser);
    }
}