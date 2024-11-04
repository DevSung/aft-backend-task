package com.example.domain.util;

import com.example.domain.enums.BoardStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class BoardStatusConverter implements AttributeConverter<BoardStatus, String> {

    @Override
    public String convertToDatabaseColumn(BoardStatus status) {
        return (status == null) ? null : status.getCode();
    }

    @Override
    public BoardStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        for (BoardStatus status : BoardStatus.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("알 수 없는 코드입니다: " + code);
    }
}