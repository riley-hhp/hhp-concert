package io.hhplus.concert.app.infra.provider.kafka;

import io.hhplus.concert.app.application.concert.ConcertEventPublisher;
import io.hhplus.concert.app.domain.concert.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Primary
@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertKafkaProvider implements ConcertEventPublisher {

    private final KafkaTemplate<String, Reservation> kafkaTemplate;

    @Value("${spring.kafka.topic.reservation}")
    private String reservationTopic;

    @Override
    public void successReservation( Reservation reservation ) {
        kafkaTemplate.send( reservationTopic, reservation );
    }
}
