package io.hhplus.concert.app.infra.jpa.concert;

import io.hhplus.concert.app.domain.concert.Seat;
import io.hhplus.concert.app.domain.concert.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatJpaRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByConcertItemIdAndStatus(long concertItemId, SeatStatus status);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId")
    Optional<Seat> findSeatForUpdate(@Param("seatId") Long seatId);
}
