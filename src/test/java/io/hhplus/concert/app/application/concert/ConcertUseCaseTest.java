package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.application.payment.PaymentUseCase;
import io.hhplus.concert.app.domain.concert.*;
import io.hhplus.concert.app.domain.payment.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Concert 단위 테스트")
@ExtendWith(MockitoExtension.class)
class ConcertUseCaseTest {

    @InjectMocks
    private ConcertUseCase concertUsecase;

    @Mock
    private ConcertRepository concertRepository;

    @Mock
    private PaymentUseCase paymentService;

    private List<ConcertItem> availableConcertItems;
    private List<Seat> availableSeats;

    @BeforeEach
    void setUp() {

        availableConcertItems = new ArrayList<>();
        availableConcertItems.add(new ConcertItem(1L, new Concert(1L, "Test Concert")));
        availableConcertItems.add(new ConcertItem(2L, new Concert(1L, "Test Concert")));

        availableSeats = new ArrayList<>();
        availableSeats.add(new Seat(1L, 1L));
        availableSeats.add(new Seat(2L, 1L));
    }

    @Test
    void 예약가능한_날짜_조회하기() {

        // given
        when(concertRepository.findAvailableConcertItems(1L)).thenReturn(availableConcertItems);

        // 예약 가능한 날짜 조회
        List<ConcertItem> items = concertUsecase.getAvailableDates(1L);

        // 검증
        assertEquals(2, items.size()); // 2개의 콘서트 아이템이 있어야 함
        verify(concertRepository, times(1)).findAvailableConcertItems(1L); // 메서드 호출 검증
    }

    @Test
    void 좌석_조회하기() {

        //given
        when(concertRepository.findAvailableSeats(1L)).thenReturn(availableSeats);

        // 좌석 조회
        List<Seat> seats = concertUsecase.getAvailableSeats(1L);

        // 검증
        assertEquals(2, seats.size()); // 2개의 좌석이 있어야 함
        verify(concertRepository, times(1)).findAvailableSeats(1L); // 메서드 호출 검증
    }

    @Test
    void 좌석_예약_및_결제_처리하기() {

        //given
        long userId = 1L;
        long concertItemId = 1L;
        long seatId = 1L;
        Reservation reservation = new Reservation(1L, userId, concertItemId, seatId);
        Payment payment = Payment.builder().id(1L).build();

        when(concertRepository.createTemporaryReservation(userId, concertItemId, seatId)).thenReturn(reservation);
        when(paymentService.processPayment(reservation.getPaymentId())).thenReturn(payment);

        // 좌석 예약 및 결제 처리
        concertUsecase.reserveSeatAndPay(userId, concertItemId, seatId);

        // 검증
        verify(concertRepository, times(1)).createTemporaryReservation(userId, concertItemId, seatId); // 예약 생성 검증
        verify(paymentService, times(1)).processPayment(reservation.getPaymentId()); // 결제 처리 검증
        verify(concertRepository, times(1)).confirmReservation(reservation.getId(), payment); // 예약 확정 검증
    }

    @Test
    void 만료된_예약_취소하기() {

        //given
        doNothing().when(concertRepository).cancelExpiredReservations();

        // 만료된 예약 취소 메서드 호출
        concertUsecase.cancelExpiredReservations();

        // 검증
        verify(concertRepository, times(1)).cancelExpiredReservations(); // 메서드 호출 검증
    }
}