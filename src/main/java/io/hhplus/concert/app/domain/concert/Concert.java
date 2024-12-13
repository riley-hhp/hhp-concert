package io.hhplus.concert.app.domain.concert;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
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

    @ToString.Exclude
    @OneToMany(mappedBy = "concert")
    @JsonManagedReference
    List<ConcertItem> concertItem;

    public void addConcertItem(ConcertItem concertItem) {
        if (this.concertItem == null) {
            return;
        }
        this.concertItem.add(concertItem);
    }
}
