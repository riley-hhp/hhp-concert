package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.domain.concert.Reservation;

public interface ConcertEventPublisher {

    void successReservation(Reservation reservation);
}
