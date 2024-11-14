package io.hhplus.concert.app.domain.concert;

import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class Reservation extends BaseTimeEntity {

    @Column(name = "RESERVATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;
    long userId;

    @Setter
    long paymentId;
    long concertItemId;
    long seatId;
    double price;
    LocalDateTime reservedAt;
    LocalDateTime expiredAt;

    @Setter
    @Enumerated(EnumType.STRING)
    ReservationStatus status;

}
