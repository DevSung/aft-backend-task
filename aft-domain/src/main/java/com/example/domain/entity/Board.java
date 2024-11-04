package com.example.domain.entity;

import com.example.domain.entity.common.BaseEntity;
import com.example.domain.enums.BoardStatus;
import com.example.domain.util.BoardStatusConverter;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idx", nullable = false)
    private Long idx;

    // 제목
    @Column(name = "title")
    private String title;

    // 내용
    @Column(name = "content")
    private String content;

    // 작성자
    @JoinColumn(name = "user_idx")
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    // 조회수
    @Column(name = "view_count")
    private Integer viewCount;

    // 상태
    @Convert(converter = BoardStatusConverter.class)
    @Column(name = "status")
    private BoardStatus status;

    public void deleteBoard() {
        this.status = BoardStatus.DELETED;
    }

    @Builder
    public Board(String title, String content, User user, int viewCount, BoardStatus status) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.viewCount = viewCount;
        this.status = status;
    }
}
