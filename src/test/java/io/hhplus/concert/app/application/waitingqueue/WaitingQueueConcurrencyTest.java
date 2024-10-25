package io.hhplus.concert.app.application.waitingqueue;

import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;
import io.hhplus.concert.app.domain.waitingqueue.WaitingQueueStatus;
import io.hhplus.concert.app.infra.jpa.waitingqueue.WaitingQueueCoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("대기열 토큰 동시성테스트")
@SpringBootTest
public class WaitingQueueConcurrencyTest {

    @Autowired
    private WaitingQueueUseCase waitingQueueUseCase;

    @Autowired
    private WaitingQueueCoreRepository waitingQueueRepository;

    private final int THREAD_COUNT = 10;

    @Test
    public void 여러_사용자가_동시에_토큰을_발급() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        Long concertId = 1L;

        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    waitingQueueUseCase.issueToken(concertId);  // 여러 스레드가 동시에 토큰 발급
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // 발급된 토큰 개수 확인 (예상: THREAD_COUNT만큼 발급됨)
        List<WaitingQueue> issuedTokens = waitingQueueRepository.findAll();
        assertEquals(THREAD_COUNT, issuedTokens.size());
    }

    @Test
    public void 여러_사용자가_동시에_토큰을_활성화() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 먼저 토큰 발급
        Long concertId = 1L;
        for (int i = 0; i < THREAD_COUNT; i++) {
            waitingQueueUseCase.issueToken(concertId);  // 미리 토큰 발급
        }

        // 동시에 토큰 활성화
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    waitingQueueUseCase.activeToken();  // 여러 스레드가 동시에 토큰 활성화
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // 모든 토큰이 활성화 상태인지 확인
        List<WaitingQueue> activeTokens = waitingQueueRepository.findAll();
        assertTrue(activeTokens.stream()
                .allMatch(token -> token.getStatus() == WaitingQueueStatus.ACTIVE));  // 토큰이 모두 활성화 상태인지 검증
    }

    @Test
    public void 여러_사용자가_동시에_토큰을_만료() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 먼저 토큰 발급 및 활성화
        Long concertId = 1L;
        for (int i = 0; i < THREAD_COUNT; i++) {
            WaitingQueue waitingQueue = waitingQueueUseCase.issueToken(concertId);
            waitingQueue.activate();  // 토큰을 활성화 상태로 변경
            waitingQueue.setExpiredAt(LocalDateTime.now().minusMinutes(1));  // 이미 만료된 상태로 설정
            waitingQueueRepository.save(waitingQueue);  // 수동으로 expiredAt 시간을 설정하여 저장
        }

        // 동시에 토큰 만료 처리
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    waitingQueueUseCase.expireToken();  // 여러 스레드가 동시에 토큰 만료 처리
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // 모든 토큰이 만료 상태인지 확인
        List<WaitingQueue> expiredTokens = waitingQueueRepository.findAll();
        assertTrue(expiredTokens.stream()
                .allMatch(token -> token.getStatus() == WaitingQueueStatus.EXPIRED));  // 모든 토큰이 만료 상태인지 검증
    }
}