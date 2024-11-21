package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.domain.concert.Reservation;
import jdk.jfr.Event;

public interface ConcertEventListener<T extends Event> {

    void successReservationHandler(Reservation reservation) throws InterruptedException;
}
