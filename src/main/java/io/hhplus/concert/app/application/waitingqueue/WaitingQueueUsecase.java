package io.hhplus.concert.app.application.waitingqueue;

import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;

public interface WaitingQueueUsecase {

    //토큰 발급
    public WaitingQueue issueToken(Long concertId);

    //토큰 조회
    public WaitingQueue getToken(String token);

    //토큰 활성화
    public void activeToken();

    //토큰 만료
    public void expireToken();

}
