package io.hhplus.concert.app.infra.jpa.waitingqueue;

import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;
import io.hhplus.concert.app.domain.waitingqueue.WaitingQueueRepository;
import io.hhplus.concert.config.RedisConfig;
import io.hhplus.concert.config.exception.CoreException;
import io.hhplus.concert.config.exception.ErrorCode;
import jakarta.annotation.PostConstruct;
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

    private final RedisConfig redisConfig;
    RedisTemplate<String, WaitingQueue> waitingQueueTemplate;
    RedisTemplate<String, String> stringTemplate;

    @PostConstruct
    public void init() {
        this.waitingQueueTemplate = redisConfig.createRedisTemplate( WaitingQueue.class );
        this.stringTemplate = redisConfig.createRedisTemplate( String.class );
    }

    private static final String WAITING_QUEUE_PREFIX = "waiting_queue";
    private static final String TOKEN_PREFIX = "token:";
    private static final long TOKEN_EXPIRE_SECONDS = 60 * 5;

    @Override
    public WaitingQueue issueToken(long concertId) {

        WaitingQueue token = WaitingQueue.issue(concertId);
        String tokenKey = TOKEN_PREFIX + token.getToken();
        waitingQueueTemplate.opsForValue().set(tokenKey, token, TOKEN_EXPIRE_SECONDS*10, TimeUnit.SECONDS);
        stringTemplate.opsForZSet().add(WAITING_QUEUE_PREFIX, token.getToken(), System.currentTimeMillis());
        return token;
    }

    @Override
    public WaitingQueue getToken(String token) {

        String tokenKey = TOKEN_PREFIX + token;
        WaitingQueue waitingQueue = waitingQueueTemplate.opsForValue().get(tokenKey);
        if(waitingQueue == null) {
            throw new CoreException(ErrorCode.TOKEN_NOT_FOUND);
        }
        return waitingQueue;
    }

    @Override
    public void activeToken() {

        Set<String> tokensToActive = stringTemplate.opsForZSet().range(WAITING_QUEUE_PREFIX, 0, WaitingQueue.ACTIVE_SIZE - 1 );
        assert tokensToActive != null;
        if(tokensToActive.isEmpty()) {
            return;
        }

        for (String token : tokensToActive) {
            String tokenKey = TOKEN_PREFIX + token;
            WaitingQueue queue = waitingQueueTemplate.opsForValue().get(tokenKey);
            if(queue != null) {
                queue.activate();
                waitingQueueTemplate.opsForValue().set(tokenKey, queue, TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
            }
            stringTemplate.opsForZSet().remove(WAITING_QUEUE_PREFIX,token);
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
        Cursor<byte[]> cursor = waitingQueueTemplate.executeWithStickyConnection(
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
            WaitingQueue queue = waitingQueueTemplate.opsForValue().get(tokenKey);
            if (queue != null) {
                // expiredAt 시간이 현재 시간보다 이전이라면 만료 처리
                if (queue.getExpiredAt().isBefore(LocalDateTime.now())) {
                    waitingQueueTemplate.delete(tokenKey);
                }
            }
        }
    }

    @Override
    public List<WaitingQueue> findAll() {

        Set<String> tokens = scanKeys(TOKEN_PREFIX + "*");
        if (tokens.isEmpty()) return List.of();
        List<WaitingQueue> jsonStrings = waitingQueueTemplate.opsForValue().multiGet(tokens);
        assert jsonStrings != null;
        return jsonStrings.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAll() {

        Set<String> tokens = scanKeys(TOKEN_PREFIX + "*");
        if (tokens.isEmpty()) return;
        tokens.forEach(waitingQueueTemplate::delete);
        Set<String> waitingQueue = stringTemplate.opsForZSet().range(WAITING_QUEUE_PREFIX, 0, -1);
        assert waitingQueue != null;
        waitingQueueTemplate.delete(WAITING_QUEUE_PREFIX);
    }

    @Override
    public void save(WaitingQueue waitingQueue) {

        String tokenKey = TOKEN_PREFIX + waitingQueue.getToken();
        waitingQueueTemplate.opsForValue().set( tokenKey, waitingQueue, TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }
}
