package com.example.domain.enums;

import lombok.Getter;

@Getter
public enum RoleType implements CodeEnum {
    ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String code;

    RoleType(String code) {
        this.code = code;
    }

}
