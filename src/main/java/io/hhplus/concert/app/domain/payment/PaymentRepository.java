package io.hhplus.concert.app.domain.payment;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository {

    Optional<Payment> findById(long paymentId);
    Optional<Payment> findByUserId(long userId);
    Optional<Payment> findByReservationId(long reservationId);
    Payment save(Payment payment);
}
