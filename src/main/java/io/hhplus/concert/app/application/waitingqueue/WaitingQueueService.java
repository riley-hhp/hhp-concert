package io.hhplus.concert.app.application.waitingqueue;

import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;
import io.hhplus.concert.app.domain.waitingqueue.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueService implements WaitingQueueUseCase {

    private final WaitingQueueRepository waitingQueRepository;

    //토큰 발급
    public WaitingQueue issueToken(Long concertId) {

        return waitingQueRepository.issueToken(concertId);
    }

    //토큰 조회
    public WaitingQueue getToken(String token) {

        return waitingQueRepository.getToken(token);
    }

    //토큰 활성화
    public void activeToken() {

        waitingQueRepository.activeToken();
    }

    //토큰 만료
    public void expireToken() {

        waitingQueRepository.expireToken();
    }

}
