package com.example.domain.payload.board.request;

import com.example.domain.entity.Board;
import com.example.domain.entity.User;
import com.example.domain.enums.BoardStatus;

public record BoardRequest(
        String title,
        String content,
        BoardStatus status
) {
    public Board toEntity(User user) {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .user(user)
                .status(this.status)
                .build();
    }

    public Board updateBoard() {
        return Board.builder()
                .title(this.title)
                .content(this.content)
                .status(this.status)
                .build();
    }
}
