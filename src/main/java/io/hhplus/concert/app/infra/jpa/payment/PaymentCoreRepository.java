package io.hhplus.concert.app.infra.jpa.payment;

import io.hhplus.concert.app.domain.payment.Payment;
import io.hhplus.concert.app.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentCoreRepository implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Optional<Payment> findById(long paymentId) {
        return paymentJpaRepository.findById(paymentId);
    }

    @Override
    public Payment save(Payment payment) {
        return paymentJpaRepository.save(payment);
    }

}
