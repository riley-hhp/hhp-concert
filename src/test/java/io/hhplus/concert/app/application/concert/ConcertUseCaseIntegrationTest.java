package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.application.point.PointService;
import io.hhplus.concert.app.domain.concert.*;
import io.hhplus.concert.app.application.payment.PaymentUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private PointService pointService;

    //    @BeforeEach
    public void setupConcertData() {
        // 콘서트 생성
        Concert concert = new Concert();
        concert.setTitle("Concert");
        concert.setStartAt(LocalDateTime.now().plusDays(10)); // 10일 후의 날짜

        // 콘서트 등록
        concertRepository.save(concert);

        // 콘서트 아이템 생성
        ConcertItem concertItem = new ConcertItem();
        concertItem.setConcert(concert);
        concertItem.setCapacity(50); // 예를 들어, 100석
        concertItem.setSessionAt(LocalDateTime.now().plusDays(10));
        // 콘서트 아이템 등록
        concertRepository.save(concertItem);

        // 좌석 생성
        Seat seat = new Seat();
        seat.setConcertItem(concertItem);
        seat.setPrice(180000);
        seat.setSeatNumber(1);
        seat.setStatus(SeatStatus.AVAILABLE);

        // 좌석 등록
        concertRepository.save(seat);


    }

    @Test
    @Order(1)
    void 예약가능날짜조회() {
        setupConcertData();
        long concertId = 1L; // 테스트할 콘서트 ID

        List<ConcertItem> actualDates = concertUseCase.getAvailableDates(concertId);

        assertNotNull(actualDates);
        assertFalse(actualDates.isEmpty(), "예약 가능한 날짜가 없습니다.");
    }

    @Test
    @Order(2)
    void 좌석조회() {

        long concertItemId = 1L; // 테스트할 콘서트 아이템 ID

        List<Seat> actualSeats = concertUseCase.getAvailableSeats(concertItemId);

        assertNotNull(actualSeats);
        assertFalse(actualSeats.isEmpty(), "예약 가능한 좌석이 없습니다.");
    }

    @Test
    @Order(3)
    void 좌석예약및결제() {

        long userId = 1L;
        long concertItemId = 1L;
        long seatId = 1L;

        io.hhplus.concert.app.domain.point.Point point = new io.hhplus.concert.app.domain.point.Point();
        point.setUserId(1);
        point.setBalance(1000000);
        pointService.save(point);


        // 가예약 생성 및 결제
        concertUseCase.reserveSeatAndPay(userId, concertItemId, seatId);

        // 예약이 정상적으로 완료되었는지 확인
        Reservation reservation = concertRepository.findReservationByUserId(userId);
        assertNotNull(reservation, "예약이 생성되지 않았습니다.");
        // 예를 들어, 상태가 "CONFIRMED"라는 문자열을 사용한다면
        assertEquals("CONFIRMED", reservation.getStatus(), "예약이 확인되지 않았습니다.");
    }


}