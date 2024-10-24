package io.hhplus.concert.app.application.scheduled;

import io.hhplus.concert.app.application.concert.ConcertUseCase;
import io.hhplus.concert.app.application.waitingqueue.WaitingQueueUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledProcess {

    private final WaitingQueueUseCase waitingQueueUseCase;
    private final ConcertUseCase concertUseCase;

    @Scheduled(cron = "0 0/1 * * * *")
    public void cancelExpiredReservations() {

        System.out.println("cancelExpiredReservations");
        concertUseCase.cancelExpiredReservations();
    }

    @Scheduled(cron = "0 0/5 * * * *")
    public void expireAndActiveToken() {

        System.out.println("expireAndActiveToken");
        waitingQueueUseCase.expireToken();
        waitingQueueUseCase.activeToken();
    }
}
