package com.example.domain.repository.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.domain.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserQRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 사용자 아이디로 사용자 존재 여부 조회
     * @param userId 사용자 아이디
     * @return boolean
     */
    public boolean existsByUserId(String userId) {
        return queryFactory.selectOne()
                .from(user)
                .where(user.userId.eq(userId))
                .fetchFirst() != null;
    }

}
