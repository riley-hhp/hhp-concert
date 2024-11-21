package io.hhplus.concert.app.interfaces.consumer.kafka;

import io.hhplus.concert.app.application.concert.ConcertEventListener;
import io.hhplus.concert.app.domain.concert.Reservation;
import jdk.jfr.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Primary
@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertKafkaConsumer implements ConcertEventListener<Event> {

    @Override
    @KafkaListener(topics = "${spring.kafka.topic.reservation}", groupId = "${spring.kafka.consumer.reservation.group-id}")
    public void successReservationHandler(@Payload Reservation reservation) throws InterruptedException {

        log.info("Kafka ::::: Reservation {} success!", reservation.getId());
        Thread.sleep(5000);
        log.info("Kafka ::::: ReservationEventHandler End");
    }
}
