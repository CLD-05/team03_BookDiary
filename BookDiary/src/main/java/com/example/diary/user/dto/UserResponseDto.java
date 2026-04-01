package com.example.diary.user.dto;

import com.example.diary.user.entity.TbUser;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {
    private Long idxUser;
    private String userId;
    private String name;
    private String email;
    private String role;

    public static UserResponseDto fromEntity(TbUser user) {
        return UserResponseDto.builder()
                .idxUser(user.getIdxUser())
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}