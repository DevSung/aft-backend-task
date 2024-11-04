package com.example.api.board.scheduler;

import com.example.api.board.service.BoardService;
import io.lettuce.core.RedisConnectionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class ViewCountScheduler {

    private final RedisTemplate<String, Object> redisTemplate;
    private final BoardService boardService;

    private static final String VIEW_COUNT_KEY_PREFIX = "viewCount:"; // 조회수 키 prefix

    @Scheduled(cron = "0 */30 * * * *") // 30분마다 실행
    public void updateAndResetViewCount() {
        Set<String> keys = null;
        try {
            keys = redisTemplate.keys(VIEW_COUNT_KEY_PREFIX + "*");
        } catch (RedisConnectionException e) {
            log.error("Redis 서버에 연결할 수 없습니다: {}", e.getMessage());
        }

        if (keys != null) {
            for (String key : keys) {
                String value = Objects.requireNonNull(redisTemplate.opsForValue().get(key)).toString(); // 값 가져오기
                Long boardIdx = extractBoardIdxFromKey(key);

                try {
                    int cnt;
                    if (value != null) {
                        cnt = Integer.parseInt(value);
                        // 조회수 업데이트
                        updateViewCount(boardIdx, cnt);
                        redisTemplate.delete(key);
                    }
                } catch (IllegalArgumentException e) {
                    log.error("조회수 업데이트 실패: {} - {}", boardIdx, e.getMessage());
                    redisTemplate.delete(key);
                }
            }
        }
    }

    private void updateViewCount(Long boardIdx, Integer viewCount) {
        boardService.updateViewCount(boardIdx, viewCount);
    }

    private Long extractBoardIdxFromKey(String key) {
        return Long.parseLong(key.replace(VIEW_COUNT_KEY_PREFIX, ""));
    }
}
