package io.hhplus.concert.app.application.outbox;

import io.hhplus.concert.app.domain.outbox.Outbox;
import io.hhplus.concert.app.domain.outbox.OutboxRepository;
import io.hhplus.concert.app.domain.outbox.OutboxStatus;
import io.hhplus.concert.app.domain.outbox.OutboxType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService implements OutboxUsecase{

    private final OutboxRepository outboxRepository;


    @Transactional
    public void publishOutbox( OutboxType outboxType, Long targetId ) {

        Outbox outbox = outboxRepository.findByOutboxTypeAndTargetIdWithOptimistLock( outboxType, targetId );
        outbox.outBoxPublish();
        outboxRepository.save(outbox);
        log.info("아웃박스 상태변경 : {}", outbox);
    }

    public List<Outbox> getRemainEvents( OutboxType outboxType ) {

        List<Outbox> remainEvents = outboxRepository.findAllByOutboxTypeAndOutboxStatus( outboxType, OutboxStatus.INIT );
        log.info("재발행할 이벤트 : {}", remainEvents.size());
        return remainEvents;
    }

    @Override
    public Outbox save(Outbox outbox) {

        return outboxRepository.save(outbox);
    }

}
