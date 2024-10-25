package io.hhplus.concert.app.domain.payment;

import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends BaseTimeEntity {

    @Column(name = "PAYMENT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;
    long userId;
    long reservationId;
    double totalAmount;
    @Setter
    LocalDateTime paymentAt;
    @Setter
    PaymentStatus status;

}
