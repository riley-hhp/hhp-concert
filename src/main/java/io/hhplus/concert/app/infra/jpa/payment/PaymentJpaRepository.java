package io.hhplus.concert.app.infra.jpa.payment;

import io.hhplus.concert.app.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentJpaRepository extends JpaRepository<Payment, Long> {

}
