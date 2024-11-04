package com.example.api.board.service;

import com.example.api.exception.CustomException;
import com.example.domain.entity.Board;
import com.example.domain.entity.User;
import com.example.domain.payload.board.request.BoardRequest;
import com.example.domain.payload.board.request.BoardSearchRequest;
import com.example.domain.payload.board.response.BoardDetailResponse;
import com.example.domain.payload.board.response.BoardResponse;
import com.example.domain.repository.board.BoardQRepository;
import com.example.domain.repository.board.BoardRepository;
import com.example.domain.repository.user.UserRepository;
import com.example.domain.util.EntityUpdateUtil;
import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardQRepository boardQRepository;

    private static final String VIEW_COUNT_KEY_PREFIX = "viewCount:"; // 조회수 키 prefix

    /**
     * 사용자 조회
     */
    private User getUser(Long idx) {
        return userRepository.findByIdx(idx)
                .orElseThrow(() -> new CustomException("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    /**
     * 게시글 조회
     */
    private Board getBoard(Long idx) {
        return boardRepository.findByIdx(idx)
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
    }

    /**
     * 게시판 목록을 조회합니다.
     * BoardSearchRequest 검색 조건
     *
     * @return 게시판 목록 응답
     */
    @Transactional(readOnly = true)
    public List<BoardResponse> getBoardList(Long userIdx, BoardSearchRequest searchRequest) {
        User user = getUser(userIdx);
        return boardQRepository.getBoardList(user.getIdx(), searchRequest);
    }

    /**
     * 게시판 상세 조회
     *
     * @param boardIdx 게시판 IDX
     * @return 게시판 상세 응답
     */
    @Transactional(readOnly = true)
    public BoardDetailResponse getBoardDetail(Long boardIdx) {
        Board board = getBoard(boardIdx);

        // 레디스에서 조회수 가져오기
        String redisViewCount;
        Integer redisViewCountInt = 0;

        try {
            redisViewCount = (String) redisTemplate.opsForValue().get(VIEW_COUNT_KEY_PREFIX + boardIdx);
            redisViewCountInt = Objects.nonNull(redisViewCount) ? Integer.parseInt(redisViewCount) : 0;
        } catch (RedisConnectionException e) {
            // 레디스 서버 연결 실패 시 기본 값을 사용
            log.warn("레디스 서버에 연결할 수 없습니다: {}", e.getMessage());
        } catch (NumberFormatException e) {
            // 레디스에서 조회수 변환 실패 시 기본 값을 사용
            log.warn("레디스 조회수 변환 실패: {}", e.getMessage());
        }

        // 총 조회수 (DB 조회수 + 레디스 조회수)
        int totalViewCount = board.getViewCount() + redisViewCountInt;
        return new BoardDetailResponse(board, totalViewCount);
    }

    /**
     * 게시판 등록
     *
     * @param userIdx 사용자 IDX
     * @param request 게시판 등록 요청
     * @return 게시판 등록 응답
     */
    @Transactional
    public BoardDetailResponse createBoard(Long userIdx, BoardRequest request) {
        User user = getUser(userIdx);
        Board board = request.toEntity(user);
        boardRepository.save(board);
        return new BoardDetailResponse(board);
    }

    /**
     * 게시판 수정
     *
     * @param userIdx  사용자 IDX
     * @param boardIdx 게시판 IDX
     * @param request  게시판 수정 요청
     * @return 게시판 수정 응답
     */
    @Transactional
    public BoardDetailResponse updateBoard(Long userIdx, Long boardIdx, BoardRequest request) {
        User user = getUser(userIdx);
        Board board = getBoard(boardIdx);

        if (!board.getUser().equals(user)) {
            throw new CustomException("게시글 작성자만 수정할 수 있습니다.", HttpStatus.FORBIDDEN);
        }

        EntityUpdateUtil.updateNonNull(request.updateBoard(), board);
        boardRepository.save(board);

        return new BoardDetailResponse(board);
    }

    /**
     * 게시판 삭제
     *
     * @param userIdx  사용자 IDX
     * @param boardIdx 게시판 IDX
     */
    @Transactional
    public void deleteBoard(Long userIdx, Long boardIdx) {
        User user = getUser(userIdx);
        Board board = getBoard(boardIdx);

        if (!board.getUser().equals(user)) {
            throw new CustomException("게시글 작성자만 삭제할 수 있습니다.", HttpStatus.FORBIDDEN);
        }

        board.deleteBoard();
        boardRepository.save(board);
    }

    @Transactional
    public void updateViewCount(Long boardIdx, Integer viewCount) {
        boardRepository.updateViewCount(boardIdx, viewCount);
    }
}
