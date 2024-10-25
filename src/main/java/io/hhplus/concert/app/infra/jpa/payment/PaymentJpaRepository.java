package io.hhplus.concert.app.infra.jpa.payment;

import io.hhplus.concert.app.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByUserId(long userId);
    Optional<Payment> findByReservationId(long reservationId);
}
