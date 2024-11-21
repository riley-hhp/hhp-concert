package io.hhplus.concert.app.application.point;

import io.hhplus.concert.app.domain.point.Point;
import io.hhplus.concert.app.domain.point.PointHistory;
import io.hhplus.concert.app.domain.point.PointRepository;
import io.hhplus.concert.app.domain.point.TransactionType;
import io.hhplus.concert.config.exception.CoreException;
import io.hhplus.concert.config.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PointService implements PointUsecase {

    private final PointRepository pointRepository;

    // 포인트 조회
    public int getUserPoints(long userId) {

        return (int) ((Point) pointRepository.findPoinByUserId(userId)
                .orElseThrow(() -> new CoreException(ErrorCode.USER_POINTS_NOT_FOUND)))
                .getBalance(); // 사용자의 포인트 잔액 반환
    }

    // 포인트 충전
    @Transactional
    public void chargePoints(long userId, int amount) {

        Point point = (Point) pointRepository.findPoinByUserIdWithLock(userId)
                .orElseThrow(() -> new CoreException(ErrorCode.USER_POINTS_NOT_FOUND));

        // 포인트 충전
        point.addPoints(amount);
        pointRepository.save(point);

        // 포인트 충전 내역 기록
        PointHistory history = PointHistory.builder()
                                            .userId(userId)
                                            .amount(amount)
                                            .transactionTyp(TransactionType.CHARGE)
                                            .build();
        pointRepository.save(history);
    }

    // 포인트 사용
    @Transactional
    public void usePoints(long userId, double amount) {

        Point point = (Point) pointRepository.findPoinByUserIdWithLock(userId)
                .orElseThrow(() -> new CoreException(ErrorCode.USER_POINTS_NOT_FOUND));

        if (point.getBalance() < amount) {
            throw new CoreException(ErrorCode.POINTS_LACK);
        }

        // 포인트 차감
        point.subtractPoints(amount);
        pointRepository.save(point);

        // 포인트 사용 내역 기록
        PointHistory history = PointHistory.builder()
                                            .userId(userId)
                                            .amount(-amount)  // 차감된 포인트 기록
                                            .transactionTyp(TransactionType.USE)
                                            .build();
        pointRepository.save(history);
    }

}