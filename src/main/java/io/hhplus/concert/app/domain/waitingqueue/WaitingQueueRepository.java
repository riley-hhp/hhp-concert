package io.hhplus.concert.app.domain.waitingqueue;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaitingQueueRepository {

    // 토큰 발급
    WaitingQueue issueToken(long concertId);
    // 토큰 조회
    WaitingQueue getToken(String token);
    // 토큰 활성화
    void activeToken();
    // 토큰 비활성화
    void expireToken();

    List<WaitingQueue> findAll();

    void deleteAll();

    void save(WaitingQueue waitingQueue);
}
