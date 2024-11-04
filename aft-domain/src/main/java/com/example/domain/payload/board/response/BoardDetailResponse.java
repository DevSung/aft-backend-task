package com.example.domain.payload.board.response;

import com.example.domain.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record BoardDetailResponse(
        Long idx,
        String title,
        String content,
        Long writerIdx,
        String writer,
        int viewCount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss") LocalDateTime regDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss") LocalDateTime modDate) {

    public BoardDetailResponse(Board board) {
        this(
                board.getIdx(),
                board.getTitle(),
                board.getContent(),
                getUserIdx(board),
                getUserName(board),
                board.getViewCount(),
                board.getRegDate(),
                board.getModDate()
        );
    }

    public BoardDetailResponse(Board board, int viewCount) {
        this(
                board.getIdx(),
                board.getTitle(),
                board.getContent(),
                getUserIdx(board),
                getUserName(board),
                viewCount,
                board.getRegDate(),
                board.getModDate()
        );
    }

    private static Long getUserIdx(Board board) {
        return board.getUser() != null ? board.getUser().getIdx() : null;
    }

    private static String getUserName(Board board) {
        return board.getUser() != null ? board.getUser().getName() : "";
    }
}