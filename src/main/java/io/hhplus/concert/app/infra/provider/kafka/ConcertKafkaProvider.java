package io.hhplus.concert.app.infra.provider.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.hhplus.concert.app.application.concert.ConcertEventPublisher;
import io.hhplus.concert.app.application.outbox.OutboxService;
import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.outbox.Outbox;
import io.hhplus.concert.app.domain.outbox.OutboxType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Primary
@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertKafkaProvider implements ConcertEventPublisher {

    private final KafkaTemplate<String, Reservation> kafkaTemplate;
    private final OutboxService outboxService;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.reservation}")
    private String reservationTopic;

    @Override
    public void successReservation( Reservation reservation ) {

        Outbox outbox = null;
        try {
            outbox = new Outbox(OutboxType.RESERVATION, reservation.getId(), objectMapper.writeValueAsString(reservation));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        outboxService.save(outbox);

        CompletableFuture<SendResult<String, Reservation>> future = kafkaTemplate.send(reservationTopic, reservation).toCompletableFuture();
        // 성공 시 처리
        future.thenAccept(result -> {
            outboxService.publishOutbox(OutboxType.RESERVATION, reservation.getId());
        });
        // 실패 시 처리
        future.exceptionally(ex -> {
            ex.printStackTrace();
            return null;
        });
    }
}
