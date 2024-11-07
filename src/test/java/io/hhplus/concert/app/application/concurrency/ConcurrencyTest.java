package io.hhplus.concert.app.application.concurrency;

import io.hhplus.concert.app.application.concert.ConcertUseCase;
import io.hhplus.concert.app.application.point.PointService;
import io.hhplus.concert.app.domain.concert.ConcertRepository;
import io.hhplus.concert.app.domain.concert.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("동시성 테스트")
@SpringBootTest
public class ConcurrencyTest {

    @Autowired
    private ConcertUseCase concertUseCase;
    @Autowired
    private ConcertRepository concertRepository;
    @Autowired
    private PointService pointService;


    @Test
    public void 여러_사용자가_동시에_같은_좌석을_예약할_때() throws InterruptedException {

        int THREAD_COUNT = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        Long concertItemId = 1L;
        Long seatId = 1L;
        Long userId = 0L;  // 다수의 사용자가 동일한 좌석을 예약 시도

        for (int i = 0; i < THREAD_COUNT; i++) {
            final long threadUserId = userId + i;  // 각각 다른 사용자
            executorService.submit(() -> {
                try {
                    concertUseCase.reserveSeatAndPay(threadUserId, concertItemId, seatId);
                } catch (Exception e) {
                    // 동시성 예외 발생 시 처리
                    System.out.println("동시성 문제 발생: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();  // 모든 스레드가 완료될 때까지 대기
        executorService.shutdown();

        // 좌석이 정확히 한 번만 예약되었는지 확인
        List<Reservation> reservations = concertRepository.findReservationsBySeatId(seatId);
        assertEquals(1, reservations.size());  // 동일 좌석이 한 번만 예약되었는지 검증
    }
    @Test
    void 포인트_충전_또는_차감_동시성_제어() throws InterruptedException {

        int THREAD_COUNT = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    if ( finalI % 2 == 0 ){
                        pointService.chargePoints(1,2);
                    }
                    else {
                        pointService.usePoints(1,1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        int userPoints = pointService.getUserPoints(1L);

        int i = THREAD_COUNT/2;
        int charge = i * 2;
        int use = i * 1;

        assertThat(userPoints).isEqualTo(10000000 + charge - use);

    }

}