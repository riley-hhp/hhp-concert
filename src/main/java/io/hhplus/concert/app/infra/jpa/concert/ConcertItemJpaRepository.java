package io.hhplus.concert.app.infra.jpa.concert;

import io.hhplus.concert.app.domain.concert.ConcertItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConcertItemJpaRepository extends JpaRepository<ConcertItem, Long> {

    List<ConcertItem> findByConcertIdAndSessionAtAfterAndCapacityGreaterThan(long concertId, LocalDateTime now, int capacity);
}
