package com.example.domain.util;

import com.example.domain.enums.RoleType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class RoleTypeConverter implements AttributeConverter<RoleType, String> {

    @Override
    public String convertToDatabaseColumn(RoleType status) {
        return (status == null) ? null : status.getCode();
    }

    @Override
    public RoleType convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }
        for (RoleType status : RoleType.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("알 수 없는 코드입니다: " + code);
    }
}