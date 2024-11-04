package com.example.domain.enums;

import lombok.Getter;

@Getter
public enum BoardStatus implements CodeEnum {
    PUBLIC("Y"),   // 공개 상태
    PRIVATE("N"),  // 비공개 상태
    DELETED("D");  // 삭제 상태

    private final String code;

    BoardStatus(String code) {
        this.code = code;
    }
}
