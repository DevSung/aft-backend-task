package com.example.domain.repository.board;

import com.example.domain.entity.Board;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<Board> findByIdx(Long idx);

    @Modifying
    @Query("update Board b set b.viewCount = b.viewCount + :viewCount where b.idx = :boardIdx")
    void updateViewCount(@Param("boardIdx") Long boardIdx, @Param("viewCount") Integer viewCount);
}
