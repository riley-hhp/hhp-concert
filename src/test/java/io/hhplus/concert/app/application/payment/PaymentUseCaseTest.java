package io.hhplus.concert.app.application.payment;

import static org.junit.jupiter.api.Assertions.*;

import io.hhplus.concert.app.domain.payment.Payment;
import io.hhplus.concert.app.domain.payment.PaymentRepository;
import io.hhplus.concert.app.domain.payment.PaymentStatus;
import io.hhplus.concert.app.application.point.PointService;
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
    private PaymentUseCase paymentUseCase;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PointService pointService;

    private Payment payment; // 결제 객체

    @BeforeEach
    void setUp() {
        payment = Payment.builder()
                .id(1L)
                .userId(1L)
                .totalAmount(1000)
                .status(PaymentStatus.PENDING)
                .paymentAt(null)
                .build();
    }

    @Test
    void 결제가_성공적으로_처리된다() {
        // given
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // when
        Payment result = paymentUseCase.processPayment(1L);

        // then
        assertEquals(PaymentStatus.COMPLETED, result.getStatus());
        assertNotNull(result.getPaymentAt());
        verify(pointService, times(1)).usePoints(payment.getUserId(), payment.getTotalAmount());
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void 결제가_이미_완료된_경우_예외가_발생한다() {
        // given
        payment.setStatus(PaymentStatus.COMPLETED);
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            paymentUseCase.processPayment(1L);
        });
        assertEquals("결제가 이미 완료되었거나 실패한 상태입니다.", exception.getMessage());
    }

    @Test
    void 결제가_존재하지_않는_경우() {
        // given
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        Payment result = paymentUseCase.processPayment(1L);
        // then
        assertNotNull(result);
        assertEquals(0L, result.getId()); // 기본 값 검증
    }
}