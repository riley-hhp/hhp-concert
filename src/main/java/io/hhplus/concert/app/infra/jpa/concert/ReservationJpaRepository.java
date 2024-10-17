package io.hhplus.concert.app.infra.jpa.concert;

import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.concert.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationJpaRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByStatusAndReservedAtBefore( ReservationStatus status, LocalDateTime reservedAt );
}
