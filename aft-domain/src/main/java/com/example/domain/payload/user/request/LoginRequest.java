package com.example.domain.payload.user.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "아이디는 필수값입니다.") String userId,
        @NotBlank(message = "패스워드는 필수값입니다.") String password) {
}
