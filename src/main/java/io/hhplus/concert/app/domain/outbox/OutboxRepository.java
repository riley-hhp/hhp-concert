package io.hhplus.concert.app.domain.outbox;

import java.util.List;

public interface OutboxRepository {

    Outbox findByOutboxTypeAndTargetIdWithOptimistLock ( OutboxType outboxType, Long targetId );

    List<Outbox> findAllByOutboxTypeAndOutboxStatus ( OutboxType outboxType, OutboxStatus outBoxStatus );

    Outbox save(Outbox outbox);

}
