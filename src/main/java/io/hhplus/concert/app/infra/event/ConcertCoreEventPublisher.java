package io.hhplus.concert.app.infra.event;

import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.event.ConcertEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertCoreEventPublisher implements ConcertEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void successReservation( Reservation reservation ) {
        applicationEventPublisher.publishEvent(reservation);
    }
}
