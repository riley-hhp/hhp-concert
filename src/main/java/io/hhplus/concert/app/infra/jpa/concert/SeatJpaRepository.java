package io.hhplus.concert.app.infra.jpa.concert;

import io.hhplus.concert.app.domain.concert.Seat;
import io.hhplus.concert.app.domain.concert.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatJpaRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByConcertItemIdAndStatus(long concertItemId, SeatStatus status);

}
