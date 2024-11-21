package io.hhplus.concert.app.interfaces.consumer.event;

import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.application.concert.ConcertEventListener;
import jdk.jfr.Event;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class ConcertSpringEventListener implements ConcertEventListener<Event> {

    @Async
    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void successReservationHandler(Reservation reservation) throws InterruptedException {

        log.info("Reservation {} success! reservation :{}", reservation.getId(), reservation);
        Thread.sleep(5000);
        log.info("ReservationEventHandler End");
    }
}
