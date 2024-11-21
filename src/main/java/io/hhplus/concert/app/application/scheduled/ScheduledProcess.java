package io.hhplus.concert.app.application.scheduled;

import io.hhplus.concert.app.application.concert.ConcertUsecase;
import io.hhplus.concert.app.application.waitingqueue.WaitingQueueUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledProcess {

    private final WaitingQueueUsecase waitingQueueUsecase;
    private final ConcertUsecase concertUsecase;

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
}
