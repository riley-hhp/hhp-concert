package io.hhplus.concert.app.domain.event;

import io.hhplus.concert.app.domain.concert.Reservation;

public interface ConcertEventPublisher {

    void successReservation(Reservation reservation);
}
