package io.hhplus.concert.app.application.payment;

import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.payment.Payment;

public interface PaymentUsecase {

    public Payment processPayment(Reservation reservation);

}
