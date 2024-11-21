package io.hhplus.concert.app.infra.jpa.outbox;

import io.hhplus.concert.app.domain.outbox.Outbox;
import io.hhplus.concert.app.domain.outbox.OutboxStatus;
import io.hhplus.concert.app.domain.outbox.OutboxType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OutboxJpaRepository extends JpaRepository<Outbox, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    Outbox findByOutboxTypeAndTargetId(OutboxType outboxType, Long targetId);

    List<Outbox> findAllByOutboxTypeAndOutboxStatus ( OutboxType outboxType, OutboxStatus outBoxStatus );

}
