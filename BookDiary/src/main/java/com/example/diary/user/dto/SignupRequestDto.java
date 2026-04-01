package com.example.diary.user.dto;

import com.example.diary.user.entity.TbUser;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class SignupRequestDto {
    private String userId;
    private String pass;
    private String name;
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