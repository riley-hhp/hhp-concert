package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.config.TestContainerSupport;
import io.hhplus.concert.app.domain.concert.*;
import io.hhplus.concert.app.interfaces.consumer.event.ConcertSpringEventListener;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@DisplayName("콘서트 예약 통합테스트")
@SpringBootTest
@Transactional
class ConcertUsecaseIntegrationTest extends TestContainerSupport {

    @Autowired
    private ConcertUsecase concertUseCase;

    @Autowired
    private ConcertRepository concertRepository;

    @MockBean
    private ConcertSpringEventListener concertEventListener;

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
    void 좌석_예약_및_결제() throws InterruptedException {

        long userId = 1L;
        long concertItemId = 1L;
        long seatId = 1L;

        // 가예약 생성 및 결제
        Reservation reserved = concertUseCase.reserveSeatAndPay(userId, concertItemId, seatId);
        verify(concertEventListener).successReservationHandler(reserved); // 이벤트 확인

        // 예약이 정상적으로 완료되었는지 확인
        Reservation reservation = concertRepository.findReservationByUserId(userId);
        assertNotNull(reservation, "예약이 생성되지 않았습니다.");
        assertEquals(ReservationStatus.CONFIRMED, reservation.getStatus(), "예약이 확인되었습니다.");
    }


}