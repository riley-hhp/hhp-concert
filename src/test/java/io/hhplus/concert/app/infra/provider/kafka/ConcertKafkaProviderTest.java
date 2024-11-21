package io.hhplus.concert.app.infra.provider.kafka;

import io.hhplus.concert.app.config.TestContainerSupport;
import io.hhplus.concert.app.domain.concert.ReservationStatus;
import io.hhplus.concert.app.domain.outbox.Outbox;
import io.hhplus.concert.app.domain.outbox.OutboxType;
import io.hhplus.concert.app.application.outbox.OutboxService;
import io.hhplus.concert.app.domain.concert.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CompletableFuture;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("카프카 콘서트 아웃박스 테스트")
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ConcertKafkaProviderTest extends TestContainerSupport {

    @Autowired
    private KafkaTemplate<String, Reservation> kafkaTemplate;

    @Autowired
    private ConcertKafkaProvider concertKafkaProvider;

    @MockBean
    private OutboxService outboxService;

    private String reservationTopic;

    Reservation reservation;

    @BeforeEach
    public void setUp() {
        reservationTopic = "reservation-topic";
        reservation = getReservation();
    }

    @DisplayName("예약 성공 시 Outbox 저장 및 Kafka 전송 성공")
    @Test
    public void testSuccessReservation() throws Exception {

        concertKafkaProvider.successReservation(reservation);

        verify(outboxService, times(1)).save(any(Outbox.class));

        CompletableFuture<SendResult<String, Reservation>> future = kafkaTemplate.send(reservationTopic, reservation).toCompletableFuture();
        SendResult<String, Reservation> result = future.get();

        assertNotNull(result);
        assertEquals(reservation.getId(), result.getProducerRecord().value().getId());

        verify(outboxService, times(1)).publishOutbox(eq(OutboxType.RESERVATION), eq(reservation.getId()));
    }


    private static Reservation getReservation() {

        long userId = 1L;
        long concertItemId = 1L;
        long seatId = 1L;
        long reservationId = 100L;
        double price = 50.0;

        Reservation reservation = Reservation.builder()
                .id(reservationId)
                .userId(userId)
                .concertItemId(concertItemId)
                .seatId(seatId)
                .price(price)
                .status(ReservationStatus.TEMP)
                .reservedAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .build();
        return reservation;
    }
}

