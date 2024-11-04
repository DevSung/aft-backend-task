package com.example.domain.payload.board.request;

import java.time.LocalDate;

/**
 * 게시판 검색 요청
 * @param title 제목
 * @param writer 작성자
 * @param content 내용
 * @param fromDate 검색 시작일
 * @param toDate 검색 종료일
 * @param page 페이지 번호
 * @param size 페이지 크기
 */
public record BoardSearchRequest(
        String title,     // 게시글 제목
        String writer,    // 작성자
        String content,   // 게시글 내용
        LocalDate fromDate, // 검색 시작일
        LocalDate toDate,    // 검색 종료일
        Integer page,       // 페이지 번호
        Integer size        // 페이지 크기
) {
}