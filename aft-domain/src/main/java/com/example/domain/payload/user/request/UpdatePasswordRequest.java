package com.example.domain.payload.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordRequest(
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password,

        @NotBlank(message = "패스워드는 필수값입니다.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "비밀번호는 최소 8자 이상이어야 하며, 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 1개를 포함해야 합니다.")
        String newPassword
) {
}
