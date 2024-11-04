package com.example.domain.payload.board.response;

import com.example.domain.enums.BoardStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * 게시판 목록 응답
 *
 * @param idx       게시판 IDX
 * @param title     제목
 * @param writer    작성자
 * @param viewCount 조회수
 * @param regDate   등록일
 */
public record BoardResponse(
        Long idx,
        String title,
        String writer,
        BoardStatus status,
        int viewCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd") LocalDateTime regDate) {
}
