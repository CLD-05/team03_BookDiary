package com.example.diary.user.dto;

import com.example.diary.user.entity.TbUser;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter @Setter
public class SignupRequestDto {
    @NotBlank(message = "아이디는 필수 항목입니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 필수 항목입니다.")
    private String pass;

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @Email(message = "올바른 이메일 형식이 아닙니다.") // 이메일 형식 체크 추가
    @NotBlank(message = "이메일은 필수 항목입니다.")
    private String email;

    public TbUser toEntity(String encodedPass) {
        return TbUser.builder()
                .userId(this.userId)
                .pass(encodedPass)
                .name(this.name)
                .email(this.email)
                .role(TbUser.Role.ROLE_USER)
                .status(TbUser.Status.ACTIVE)
                .createDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .build();
    }
}