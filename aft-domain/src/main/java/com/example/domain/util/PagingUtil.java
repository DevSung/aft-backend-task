package com.example.domain.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PagingUtil {
    private static final int DEFAULT_PAGE = 0; // 기본 페이지 번호
    private static final int DEFAULT_SIZE = 10; // 기본 페이지 크기
    private static final int MAX_SIZE = 100; // 최대 페이지 크기

    /**
     * 페이지 번호와 페이지 크기로 Pageable 객체를 생성.
     * 페이지 번호와 크기가 유효하지 않으면 기본값 사용.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return Pageable 객체
     */
    public static Pageable createPageable(Integer page, Integer size) {
        int pageNumber = (page != null && page >= 0) ? page : DEFAULT_PAGE;
        int pageSize = (size != null && size > 0) ? Math.min(size, MAX_SIZE) : DEFAULT_SIZE;

        return PageRequest.of(pageNumber, pageSize);
    }
}
