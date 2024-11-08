package io.hhplus.concert.app.infra.jpa.waitingqueue;

import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;
import io.hhplus.concert.app.domain.waitingqueue.WaitingQueueRepository;
import io.hhplus.concert.config.exception.CoreException;
import io.hhplus.concert.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Primary
@Repository
@RequiredArgsConstructor
public class WaitingQueueRedisRepository implements WaitingQueueRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String WAITING_QUEUE_PREFIX = "waiting_queue";
    private static final String TOKEN_PREFIX = "token:";
    private static final long TOKEN_EXPIRE_SECONDS = 60 * 5;

    @Override
    public WaitingQueue issueToken(long concertId) {

        WaitingQueue token = WaitingQueue.issue(concertId);
        String tokenKey = TOKEN_PREFIX + token.getToken();

        redisTemplate.opsForValue().set(tokenKey, Objects.requireNonNull(WaitingQueue.stringify(token)), TOKEN_EXPIRE_SECONDS*10, TimeUnit.SECONDS);
        redisTemplate.opsForZSet().add(WAITING_QUEUE_PREFIX, token.getToken(), System.currentTimeMillis());
        return token;
    }

    @Override
    public WaitingQueue getToken(String token) {

        String tokenKey = TOKEN_PREFIX + token;
        WaitingQueue waitingQueue = WaitingQueue.parseWaitingQueue(redisTemplate.opsForValue().get(tokenKey));
        if(waitingQueue == null) {
            throw new CoreException(ErrorCode.TOKEN_NOT_FOUND);
        }
        return waitingQueue;
    }

    @Override
    public void activeToken() {

        Set<String> tokensToActive = redisTemplate.opsForZSet().range(WAITING_QUEUE_PREFIX, 0, WaitingQueue.ACTIVE_SIZE - 1 );
        assert tokensToActive != null;
        if(tokensToActive.isEmpty()) {
            return;
        }

        for (String token : tokensToActive) {
            String tokenKey = TOKEN_PREFIX + token;
            WaitingQueue queue = WaitingQueue.parseWaitingQueue(redisTemplate.opsForValue().get(tokenKey));

            if(queue != null) {
                queue.activate();
                redisTemplate.opsForValue().set(tokenKey, Objects.requireNonNull(WaitingQueue.stringify(queue)),TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }
            redisTemplate.opsForZSet().remove(WAITING_QUEUE_PREFIX,token);
        }
    }

    /**
     * SCAN을 사용하여 특정 패턴의 키를 반환하는 메서드
     *
     * @param pattern
     * @return
     */
    private Set<String> scanKeys(String pattern) {

        Set<String> keys = new HashSet<>();
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(
                connection -> connection.scan(ScanOptions.scanOptions().match(pattern).count(100).build())
        );

        while (true) {
            assert cursor != null;
            if (!cursor.hasNext()) break;
            keys.add(new String(cursor.next()));
        }
        return keys;
    }

    @Override
    public void expireToken() {

        Set<String> tokens = scanKeys(TOKEN_PREFIX + "*");
        if (tokens.isEmpty()) {
            return;
        }

        for (String tokenKey : tokens) {
            WaitingQueue queue = WaitingQueue.parseWaitingQueue(redisTemplate.opsForValue().get(tokenKey));

            if (queue != null) {
                // expiredAt 시간이 현재 시간보다 이전이라면 만료 처리
                if (queue.getExpiredAt().isBefore(LocalDateTime.now())) {
                    redisTemplate.delete(tokenKey);
                }
            }
        }
    }

    @Override
    public List<WaitingQueue> findAll() {

        Set<String> tokens = scanKeys(TOKEN_PREFIX + "*");
        if (tokens.isEmpty()) return List.of();

        List<String> jsonStrings = redisTemplate.opsForValue().multiGet(tokens);
        assert jsonStrings != null;
        return jsonStrings.stream()
                .filter(Objects::nonNull)
                .map(WaitingQueue::parseWaitingQueue)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {

        Set<String> tokens = scanKeys(TOKEN_PREFIX + "*");
        if (tokens.isEmpty()) return;
        tokens.forEach(redisTemplate::delete);

        Set<String> waitingQueue = redisTemplate.opsForZSet().range(WAITING_QUEUE_PREFIX, 0, -1);
        assert waitingQueue != null;
        if (waitingQueue.isEmpty()) return;
        redisTemplate.delete(WAITING_QUEUE_PREFIX);
    }

    @Override
    public void save(WaitingQueue waitingQueue) {

        String tokenKey = TOKEN_PREFIX + waitingQueue.getToken();
        redisTemplate.opsForValue().set(tokenKey, Objects.requireNonNull(WaitingQueue.stringify(waitingQueue)), TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }
}
