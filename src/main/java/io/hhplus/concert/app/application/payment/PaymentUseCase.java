package io.hhplus.concert.app.application.payment;

import io.hhplus.concert.app.application.point.PointService;
import io.hhplus.concert.app.domain.payment.Payment;
import io.hhplus.concert.app.domain.payment.PaymentRepository;
import io.hhplus.concert.app.domain.payment.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final PointService pointService;

    @Transactional
    public Payment processPayment(long paymentId) {

        // paymentId로 결제 정보 조회
        Payment payment = paymentRepository.findById(paymentId).orElse(new Payment());

        // 결제 처리 로직
        if (payment.getStatus() != PaymentStatus.COMPLETED) {

            pointService.usePoints(payment.getUserId(), payment.getTotalAmount());      // 포인트 차감
            payment.setStatus(PaymentStatus.COMPLETED);  // 결제 완료 상태로 변경
            payment.setPaymentAt(LocalDateTime.now());   // 결제 완료 시간 설정
            paymentRepository.save(payment);  // 업데이트된 결제 정보 저장
        }
        else {

            throw new RuntimeException("결제가 이미 완료되었거나 실패한 상태입니다.");
        }

        return payment;  // 업데이트된 결제 정보 반환
    }
}
