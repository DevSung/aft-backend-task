package com.example.domain.payload.user.request;

import com.example.domain.entity.User;
import com.example.domain.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record JoinRequest(
        @NotBlank(message = "이름은 필수값입니다.")
        String name,

        @NotBlank(message = "아이디는 필수값입니다.")
        String userId,

        @NotBlank(message = "패스워드는 필수값입니다.")
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "비밀번호는 최소 8자 이상이어야 하며, 대문자 1개, 소문자 1개, 숫자 1개, 특수문자 1개를 포함해야 합니다.")
        String password,

        List<RoleType> role) {

    public User toEntity(String encodePassword) {
        return User.builder()
                .userId(this.userId)
                .password(encodePassword)
                .name(this.name)
                .build();
    }

}
