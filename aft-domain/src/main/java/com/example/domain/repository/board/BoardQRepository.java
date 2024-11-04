package com.example.domain.repository.board;

import com.example.domain.enums.BoardStatus;
import com.example.domain.payload.board.request.BoardSearchRequest;
import com.example.domain.payload.board.response.BoardResponse;
import com.example.domain.util.PagingUtil;
import com.example.domain.util.QuerydslUtils;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.example.domain.entity.QBoard.board;


@Repository
@RequiredArgsConstructor
public class BoardQRepository {

    private final JPAQueryFactory queryFactory;

    public List<BoardResponse> getBoardList(Long userIdx, BoardSearchRequest searchRequest) {
        BooleanBuilder builder = searchCondition(searchRequest);
        Pageable pageable = PagingUtil.createPageable(searchRequest.page(), searchRequest.size());
        return queryFactory
                .select(Projections.constructor(BoardResponse.class,
                        board.idx,
                        board.title,
                        board.user.name.as("writer"),
                        board.status,
                        board.viewCount,
                        board.regDate
                ))
                .from(board)
                .innerJoin(board.user)
                .where(
                        builder,
                        board.status.eq(BoardStatus.PUBLIC)
                                .or(board.status.eq(BoardStatus.PRIVATE).and(board.user.idx.eq(userIdx)))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.regDate.desc())
                .fetch();
    }

    private BooleanBuilder searchCondition(BoardSearchRequest searchRequest) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QuerydslUtils.likeField(board.title, searchRequest.title()));
        builder.and(QuerydslUtils.likeField(board.user.name, searchRequest.writer()));
        builder.and(QuerydslUtils.likeField(board.content, searchRequest.content()));

        if (searchRequest.fromDate() != null && searchRequest.toDate() != null) {
            LocalDateTime startDateTime = searchRequest.fromDate().atStartOfDay();
            LocalDateTime endDateTime = searchRequest.toDate().atTime(LocalTime.MAX);
            builder.and(QuerydslUtils.betweenField(board.regDate, startDateTime, endDateTime));
        }

        return builder;
    }

}
