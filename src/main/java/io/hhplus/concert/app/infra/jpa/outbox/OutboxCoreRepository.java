package io.hhplus.concert.app.infra.jpa.outbox;

import io.hhplus.concert.app.domain.outbox.OutboxRepository;
import io.hhplus.concert.app.domain.outbox.Outbox;
import io.hhplus.concert.app.domain.outbox.OutboxStatus;
import io.hhplus.concert.app.domain.outbox.OutboxType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OutboxCoreRepository implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public Outbox findByOutboxTypeAndTargetIdWithOptimistLock(OutboxType outboxType, Long targetId) {
        return outboxJpaRepository.findByOutboxTypeAndTargetId(outboxType, targetId);
    }

    @Override
    public List<Outbox> findAllByOutboxTypeAndOutboxStatus(OutboxType outboxType, OutboxStatus outBoxStatus) {
        return outboxJpaRepository.findAllByOutboxTypeAndOutboxStatus (outboxType, outBoxStatus);
    }

    @Override
    public Outbox save(Outbox outbox) {
        return outboxJpaRepository.save(outbox);
    }
}
