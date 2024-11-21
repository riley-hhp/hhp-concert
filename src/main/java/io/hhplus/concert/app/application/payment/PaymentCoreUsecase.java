package io.hhplus.concert.app.application.payment;

import io.hhplus.concert.app.application.point.PointUsecase;
import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.payment.Payment;
import io.hhplus.concert.app.domain.payment.PaymentRepository;
import io.hhplus.concert.app.domain.payment.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PaymentCoreUsecase implements PaymentUsecase {

    private final PaymentRepository paymentRepository;
    private final PointUsecase pointUsecase;

    @Transactional
    public Payment processPayment(Reservation reservation) {

        Payment payment = paymentRepository.findByReservationId(reservation.getId()).orElse(Payment.builder()
                                                                                                    .userId(reservation.getUserId())
                                                                                                    .reservationId(reservation.getId())
                                                                                                    .totalAmount(reservation.getPrice())
                                                                                                    .build());

        // 결제 처리 로직
        if ( payment.getStatus() != PaymentStatus.COMPLETED ) {
            pointUsecase.usePoints(reservation.getUserId(), reservation.getPrice());      // 포인트 차감
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setPaymentAt(LocalDateTime.now()); // 결제 완료 시간 설정
            paymentRepository.save(payment);  // 업데이트된 결제 정보 저장
        }
        else {
            throw new RuntimeException("결제가 이미 완료되었거나 실패한 상태입니다.");
        }
        return payment;
    }
}
