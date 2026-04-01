package com.example.diary.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private String name;        // 수정할 이름
    private String email;       // 수정할 이메일
    private String currentPass; // 본인 확인을 위한 현재 비밀번호 (필수)
    private String newPass;     // 변경할 새 비밀번호 (선택)
}