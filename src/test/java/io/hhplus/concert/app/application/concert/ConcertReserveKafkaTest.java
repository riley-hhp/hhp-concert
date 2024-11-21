package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.config.TestContainerSupport;
import io.hhplus.concert.app.domain.concert.*;
import io.hhplus.concert.app.interfaces.consumer.kafka.ConcertKafkaConsumer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Duration;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@DisplayName("카프카 콘서트 예약 통합 테스트")
@SpringBootTest
class ConcertReserveKafkaTest extends TestContainerSupport {

    @Autowired
    private ConcertUsecase concertUseCase;

    @Autowired
    private ConcertRepository concertRepository;

    @SpyBean
    private ConcertKafkaConsumer concertEventListener;

    @Test
    void 좌석_예약_및_결제() throws InterruptedException {

        long userId = 1L;
        long concertItemId = 1L;
        long seatId = 1L;

        // 가예약 생성 및 결제
        Reservation reserved = concertUseCase.reserveSeatAndPay(userId, concertItemId, seatId);

        // 카프카가 소비하고 DB 상태가 변경될 때까지 기다림
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {

                    verify(concertEventListener, times(1)).successReservationHandler(any(Reservation.class));
                    Reservation reservation = concertRepository.findReservationByUserId(userId);
                    assertNotNull(reservation, "예약이 생성되지 않았습니다.");
                    assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus(), "예약이 확인되었습니다.");
        });
    }


}