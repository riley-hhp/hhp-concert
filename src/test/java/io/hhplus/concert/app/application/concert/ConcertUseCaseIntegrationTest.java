package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.application.payment.PaymentUseCase;
import io.hhplus.concert.app.application.point.PointUseCase;
import io.hhplus.concert.app.domain.concert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("콘서트 예약 통합테스트")
@SpringBootTest
@Transactional
class ConcertUseCaseIntegrationTest {

    @Autowired
    private ConcertUseCase concertUseCase;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private PaymentUseCase paymentUseCase;

    @Autowired
    private PointUseCase pointUseCase;

    @Test
    @Order(1)
    void 예약_가능날짜_조회() {

        long concertId = 1L; // 테스트할 콘서트 ID

        List<ConcertItem> actualDates = concertUseCase.getAvailableDates(concertId);
        assertNotNull(actualDates);
        assertFalse(actualDates.isEmpty(), "예약 가능한 날짜가 없습니다.");
    }

    @Test
    @Order(2)
    void 좌석_조회() {

        long concertItemId = 1L; // 테스트할 콘서트 아이템 ID

        List<Seat> actualSeats = concertUseCase.getAvailableSeats(concertItemId);
        assertNotNull(actualSeats);
        assertFalse(actualSeats.isEmpty(), "예약 가능한 좌석이 없습니다.");
    }

    @Test
    @Order(3)
    void 좌석_예약_및_결제() {

        long userId = 1L;
        long concertItemId = 1L;
        long seatId = 1L;

        // 가예약 생성 및 결제
        concertUseCase.reserveSeatAndPay(userId, concertItemId, seatId);

        // 예약이 정상적으로 완료되었는지 확인
        Reservation reservation = concertRepository.findReservationByUserId(userId);
        assertNotNull(reservation, "예약이 생성되지 않았습니다.");
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus(), "예약이 확인되었습니다.");
    }


}