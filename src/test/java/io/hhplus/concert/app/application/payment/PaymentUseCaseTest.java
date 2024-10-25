package io.hhplus.concert.app.application.payment;

import static org.junit.jupiter.api.Assertions.*;

import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.concert.ReservationStatus;
import io.hhplus.concert.app.domain.payment.Payment;
import io.hhplus.concert.app.domain.payment.PaymentRepository;
import io.hhplus.concert.app.domain.payment.PaymentStatus;
import io.hhplus.concert.app.application.point.PointUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@DisplayName("결제 단위 테스트")
@ExtendWith(MockitoExtension.class)
class PaymentUseCaseTest {

    @InjectMocks
    private PaymentFacade paymentUseCase;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PointUseCase pointUseCase;

    private Payment payment; // 결제 객체

    @BeforeEach
    void setUp() {
        payment = Payment.builder().id(1L)
                                   .reservationId(1L)
                                   .userId(1L)
                                   .totalAmount(1000)
                                   .status(PaymentStatus.PENDING)
                                   .paymentAt(null)
                                   .build();
    }

    @Test
    void 결제가_성공적으로_처리된다() {
        // given
        when(paymentRepository.findByReservationId(1L)).thenReturn(Optional.of(payment));

        // when
        Payment result = paymentUseCase.processPayment(Reservation.builder().id(1L).userId(1L).price(1000).status(ReservationStatus.TEMP).build());

        // then
        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
        assertNotNull(result.getPaymentAt());
        verify(pointUseCase, times(1)).usePoints(payment.getUserId(), payment.getTotalAmount());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void 결제가_이미_완료된_경우_예외가_발생한다() {
        // given
        Payment payment = Payment.builder().status(PaymentStatus.COMPLETED).build();
        when(paymentRepository.findByReservationId(1L)).thenReturn(Optional.of(payment));

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentUseCase.processPayment(Reservation.builder().id(1L).paymentId(1L).build());
        });
        assertEquals("결제가 이미 완료되었거나 실패한 상태입니다.", exception.getMessage());
    }

    @Test
    void 결제가_존재하지_않는_경우() {
        // given
        when(paymentRepository.findByReservationId(1L)).thenReturn(Optional.of(Payment.builder().id(1L).build()));

        // when
        Payment result = paymentUseCase.processPayment(Reservation.builder().id(1L).paymentId(1L).build());
        // then
        assertNotNull(result);
        assertEquals(1L, result.getId()); // 기본 값 검증
    }
}