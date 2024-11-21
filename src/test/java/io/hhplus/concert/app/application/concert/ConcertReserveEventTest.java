package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.application.payment.PaymentFacade;
import io.hhplus.concert.app.domain.concert.ConcertRepository;
import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.concert.ReservationStatus;
import io.hhplus.concert.app.domain.event.ConcertEventPublisher;
import io.hhplus.concert.app.domain.payment.Payment;
import io.hhplus.concert.app.domain.payment.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("이벤트 테스트")
public class ConcertReserveEventTest {

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private PaymentFacade paymentFacade;

    @Mock
    private ConcertEventPublisher concertEventPublisher;

    @InjectMocks
    private ConcertFacade concertFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 예약_좌석_이벤트_발행_확인() {

        long userId = 1L;
        long concertItemId = 1L;
        long seatId = 1L;
        long reservationId = 100L;
        long paymentId = 200L;
        double price = 50.0;

        Reservation temporaryReservation = Reservation.builder()
                .id(reservationId)
                .userId(userId)
                .concertItemId(concertItemId)
                .seatId(seatId)
                .price(price)
                .status(ReservationStatus.TEMP)
                .reservedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();

        Payment payment = Payment.builder()
                .userId(1L)
                .reservationId(100L)
                .totalAmount(50.0)
                .paymentAt(LocalDateTime.now())
                .status(PaymentStatus.COMPLETED)
                .build();

        Reservation confirmedReservation = Reservation.builder()
                .id(reservationId)
                .userId(userId)
                .concertItemId(concertItemId)
                .seatId(seatId)
                .price(price)
                .paymentId(paymentId)
                .status(ReservationStatus.CONFIRMED)
                .reservedAt(temporaryReservation.getReservedAt())
                .expiredAt(temporaryReservation.getExpiredAt())
                .build();

        // Mock
        when(concertRepository.createTemporaryReservation(userId, concertItemId, seatId)).thenReturn(temporaryReservation);
        when(paymentFacade.processPayment(temporaryReservation)).thenReturn(payment);
        when(concertRepository.confirmReservation(reservationId, payment)).thenReturn(confirmedReservation);

        // Execute
        Reservation result = concertFacade.reserveSeatAndPay(userId, concertItemId, seatId);

        // Verify
        verify(concertRepository).createTemporaryReservation(userId, concertItemId, seatId);
        verify(paymentFacade).processPayment(temporaryReservation);
        verify(concertRepository).confirmReservation(reservationId, payment);
        verify(concertEventPublisher).successReservation(confirmedReservation); // 이벤트 발행

        // Assertions
        assertNotNull(result, "예약 결과가 null이 아닙니다.");
        assertEquals(ReservationStatus.CONFIRMED, result.getStatus(), "예약 상태가 CONFIRMED 여야 합니다.");
    }

}
