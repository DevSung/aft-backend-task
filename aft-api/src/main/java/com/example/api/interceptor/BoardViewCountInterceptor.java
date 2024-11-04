package com.example.api.interceptor;

import com.example.api.exception.CustomException;
import com.example.domain.entity.Board;
import com.example.domain.repository.board.BoardRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardViewCountInterceptor implements HandlerInterceptor {
    private final RedisTemplate<String, Object> redisTemplate;
    private final BoardRepository boardRepository;

    private static final String VIEW_RECORD_KEY_PREFIX = "viewRecord:"; // 조회 기록 키 prefix
    private static final String VIEW_COUNT_KEY_PREFIX = "viewCount:"; // 조회수 키 prefix
    private static final long VIEW_COUNT_EXPIRATION_TIME_MILLIS = TimeUnit.HOURS.toMillis(24); // 24시간

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Long boardIdx = extractBoardId(request.getRequestURI());
        Long userIdx = getUserIdFromRequest(request);

        // 자신의 게시물인지 확인
        if (!isAuthor(boardIdx, userIdx)) {
            try {
                String viewRecordKey = VIEW_RECORD_KEY_PREFIX + boardIdx; // 게시글 ID에 대한 조회 기록 키
                // 사용자별 조회 기록을 확인
                boolean hasViewed = redisTemplate.opsForHash().hasKey(viewRecordKey, userIdx.toString());
                // 24시간 이내에 조회하지 않은 경우 조회수 증가
                if (!hasViewed) {
                    // 조회수 증가
                    redisTemplate.opsForValue().increment(VIEW_COUNT_KEY_PREFIX + boardIdx, 1);

                    // Hash 구조를 사용해 사용자별 조회 기록 추가
                    redisTemplate.opsForHash().put(viewRecordKey, userIdx.toString(), "viewed");

                    // 24시간 후 만료 설정
                    redisTemplate.expire(viewRecordKey, VIEW_COUNT_EXPIRATION_TIME_MILLIS, TimeUnit.MILLISECONDS);
                }
            } catch (Exception e) {
                log.error("Redis 서버 에러 발생: {}", e.getMessage());
            }
        }
        return true;
    }

    private Long extractBoardId(String path) {
        try {
            return Long.parseLong(path.replaceAll(".*/board/(\\d+).*", "$1"));
        } catch (NumberFormatException e) {
            throw new CustomException("올바르지 않은 URL 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        Object userIdxAttribute = request.getAttribute("userIdx");
        if (userIdxAttribute == null) {
            throw new CustomException("유효하지 않은 사용자 정보입니다.", HttpStatus.UNAUTHORIZED);
        }
        try {
            return Long.parseLong(userIdxAttribute.toString());
        } catch (NumberFormatException e) {
            throw new CustomException("잘못된 사용자 ID 입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isAuthor(Long boardIdx, Long userIdx) {
        Board board = boardRepository.findByIdx(boardIdx)
                .orElseThrow(() -> new CustomException("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND));
        return board.getUser() != null && board.getUser().getIdx().equals(userIdx);
    }
}