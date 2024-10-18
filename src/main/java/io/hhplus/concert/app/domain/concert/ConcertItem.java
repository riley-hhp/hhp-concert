package io.hhplus.concert.app.domain.concert;

import io.hhplus.concert.config.BaseTimeEntity; import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class ConcertItem extends BaseTimeEntity {

    @Column(name = "CONCERT_ITEM_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;
    String sessionTitle;
    LocalDateTime sessionAt;
    String location;
    Integer capacity;

    @ManyToOne
    @JoinColumn(name = "CONCERT_ID")
    Concert concert;

    @OneToMany(mappedBy = "concertItem")
    List<Seat> seats = new ArrayList<>();

    public ConcertItem(long l, Concert concert) {
        this.id = l;
        this.concert = concert;
    }

    // 좌석 감소
    public void decreaseCapacity() {

        if (this.capacity <= 0) {
            throw new IllegalStateException("더 이상 예약할 수 없습니다.");
        }
        this.capacity--;
    }

    // 좌석 복원
    public void increaseCapacity() {

        this.capacity++;
    }

    // 콘서트 세션 시간이 지났는지 확인
    public boolean isSessionExpired() {

        return this.sessionAt.isBefore(LocalDateTime.now());
    }
}
