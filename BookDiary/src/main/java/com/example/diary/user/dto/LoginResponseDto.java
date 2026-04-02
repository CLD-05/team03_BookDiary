package com.example.diary.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class LoginResponseDto {
 private String token;
 private String role;
}