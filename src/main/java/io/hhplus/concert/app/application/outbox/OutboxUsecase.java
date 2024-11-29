package io.hhplus.concert.app.application.outbox;

import io.hhplus.concert.app.domain.outbox.Outbox;
import io.hhplus.concert.app.domain.outbox.OutboxType;

import java.util.List;

public interface OutboxUsecase {

    void publishOutbox( OutboxType outboxType, Long targetId );

    List<Outbox> getRemainEvents( OutboxType outboxType );

    Outbox save( Outbox outbox );

}
