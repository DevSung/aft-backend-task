package com.example.domain.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringPath;

import java.time.LocalDateTime;
import java.util.Optional;

public class QuerydslUtils {

    public static BooleanExpression likeField(StringPath field, String condition) {
        return Optional.ofNullable(condition)
                .map(field::contains)
                .orElse(null);
    }

    public static BooleanExpression betweenField(DateTimePath<LocalDateTime> field, LocalDateTime start, LocalDateTime end) {
        return Optional.ofNullable(start)
                .flatMap(s -> Optional.ofNullable(end)
                        .map(e -> field.between(s, e)))
                .orElse(null);
    }

}
