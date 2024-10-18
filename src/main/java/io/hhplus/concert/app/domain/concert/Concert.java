package io.hhplus.concert.app.domain.concert;

import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Concert extends BaseTimeEntity {

    @Column(name="CONCERT_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;
    String title;
    LocalDateTime startAt;
    LocalDateTime endAt;

    public Concert(long id, String title) {
        this.id = id;
        this.title = title;
    }

    @OneToMany(mappedBy = "concert")
    List<ConcertItem> concertItem;

    public void addConcertItem(ConcertItem concertItem) {
        if (this.concertItem == null) {
            return;
        }
        this.concertItem.add(concertItem);
    }
}
