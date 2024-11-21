package io.hhplus.concert.app.application.scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concert.app.application.concert.ConcertEventPublisher;
import io.hhplus.concert.app.application.concert.ConcertUsecase;
import io.hhplus.concert.app.application.outbox.OutboxUsecase;
import io.hhplus.concert.app.application.waitingqueue.WaitingQueueUsecase;
import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.outbox.Outbox;
import io.hhplus.concert.app.domain.outbox.OutboxType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledProcess {

    private final WaitingQueueUsecase waitingQueueUsecase;
    private final ConcertUsecase concertUsecase;
    private final OutboxUsecase outboxUsecase;
    private final ConcertEventPublisher concertEventPublisher;
    private final ObjectMapper objectMapper;

    @Scheduled(cron = "0 0/1 * * * *")
    public void cancelExpiredReservations() {

        System.out.println("cancelExpiredReservations");
        concertUsecase.cancelExpiredReservations();
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void expireAndActiveToken() {

        System.out.println("expireAndActiveToken");
        waitingQueueUsecase.expireToken();
        waitingQueueUsecase.activeToken();
    }

    @Scheduled(cron = "0 * * * * *")
    public void rePublishEvent( OutboxType outboxType ) {

        List<Outbox> remainEvents = outboxUsecase.getRemainEvents(OutboxType.RESERVATION);
        for (Outbox reservationOutbox : remainEvents) {
            try {
                concertEventPublisher.successReservation(objectMapper.readValue(reservationOutbox.getPayload(), Reservation.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
