package io.hhplus.concert.app.domain.point;

import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class PointHistory extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;
    long userId;
    long pointId;
    double amount;

    @Enumerated(EnumType.STRING)
    TransactionType transactionTyp;
    String description;

}
