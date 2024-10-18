package io.hhplus.concert.app.infra.jpa.waitingqueue;

import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;
import io.hhplus.concert.app.domain.waitingqueue.WaitingQueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaitingQueueJpaRepository extends JpaRepository<WaitingQueue, Long> {

    Optional<WaitingQueue> findByToken(String token);
    List<WaitingQueue> findByExpiredAtBefore(LocalDateTime now);
    List<WaitingQueue> findTop10ByStatusOrderByCreatedAtAsc(WaitingQueueStatus waitingQueueStatus);

}
