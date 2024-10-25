package io.hhplus.concert.app.infra.jpa.waitingqueue;

import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;
import io.hhplus.concert.app.domain.waitingqueue.WaitingQueueRepository;
import io.hhplus.concert.app.domain.waitingqueue.WaitingQueueStatus;
import io.hhplus.concert.config.exception.CoreException;
import io.hhplus.concert.config.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WaitingQueueCoreRepository implements WaitingQueueRepository {

    private final WaitingQueueJpaRepository waitingQueJpaRepository;


    @Override
    public WaitingQueue issueToken(long concertId) {

        return waitingQueJpaRepository.save( WaitingQueue.issue(concertId) );
    }

    @Override
    public WaitingQueue getToken(String token) {

        return waitingQueJpaRepository.findByToken(token)
                                      .orElseThrow(()-> new CoreException(ErrorCode.TOKEN_NOT_FOUND));
    }

    @Override
    public void activeToken() {

        List<WaitingQueue> toActiveList = waitingQueJpaRepository.findTop10ByStatusOrderByCreatedAtAsc(WaitingQueueStatus.WAITING)
                                                                 .stream()
                                                                 .map(WaitingQueue::activate)
                                                                 .toList();
        waitingQueJpaRepository.saveAll(toActiveList);
    }

    @Override
    public void expireToken() {

        List<WaitingQueue> toExpireList = waitingQueJpaRepository.findByExpiredAtBefore(LocalDateTime.now())
                                                                 .stream()
                                                                 .map(WaitingQueue::expire)
                                                                 .toList();
        waitingQueJpaRepository.saveAll(toExpireList);
    }

    @Override
    public List<WaitingQueue> findAll() {
        return waitingQueJpaRepository.findAll();
    }

    @Override
    public void deleteAll() {
        waitingQueJpaRepository.deleteAll();
    }

    @Override
    public void save(WaitingQueue waitingQueue) {
        waitingQueJpaRepository.save(waitingQueue);
    }
}
