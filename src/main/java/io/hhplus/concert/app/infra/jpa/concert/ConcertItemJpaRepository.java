package io.hhplus.concert.app.infra.jpa.concert;

import io.hhplus.concert.app.domain.concert.ConcertItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertItemJpaRepository extends JpaRepository<ConcertItem, Long> {

    List<ConcertItem> findByConcertIdAndSessionAtAfterAndCapacityGreaterThan(long concertId, LocalDateTime now, int capacity);

//    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ci FROM ConcertItem ci WHERE ci.id = :concertItemId")
    Optional<ConcertItem> findConcertItemForUpdate(@Param("concertItemId") Long concertItemId);
}
