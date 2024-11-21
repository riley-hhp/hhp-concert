package io.hhplus.concert.app.infra.provider.event;

import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.application.concert.ConcertEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConcertSpringEventPublisher implements ConcertEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void successReservation( Reservation reservation ) {
        applicationEventPublisher.publishEvent(reservation);
    }
}
