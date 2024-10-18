package io.hhplus.concert.app.domain.concert;

import io.hhplus.concert.app.domain.payment.Payment;
import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Seat extends BaseTimeEntity {

    @Column(name = "SEAT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;
    long seatNumber;

    @Enumerated(EnumType.STRING)
    SeatStatus status;
    double price;

    @ManyToOne
    @JoinColumn(name = "CONCERT_ITEM_ID")
    ConcertItem concertItem;

    public Seat(long l, long l1) {
        this.id = l;
        this.seatNumber = l1;
    }

    // 가예약 처리
    public void reserveTemporarily() {

        if (this.status != SeatStatus.AVAILABLE) {
            throw new IllegalStateException("이미 예약된 좌석입니다.");
        }
        this.status = SeatStatus.TEMP;
    }

    // 예약 확정 처리
    public void confirmReservation() {

        if (this.status != SeatStatus.TEMP) {
            throw new IllegalStateException("가예약 상태가 아닙니다.");
        }
        this.status = SeatStatus.RESERVED;
    }

    // 5분 이내 결제 실패 시 상태 복구
    public void restoreIfExpired( Reservation reservation ) {

        if (this.status == SeatStatus.TEMP && reservation.getCreatedAt().plusMinutes(5).isBefore(LocalDateTime.now())) {
            this.status = SeatStatus.AVAILABLE;
        }
    }

}

