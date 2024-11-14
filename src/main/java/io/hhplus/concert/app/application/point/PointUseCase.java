package io.hhplus.concert.app.application.point;

public interface PointUseCase {

    // 포인트 조회
    public int getUserPoints(long userId);

    // 포인트 충전
    public void chargePoints(long userId, int amount);

    // 포인트 사용
    public void usePoints(long userId, double amount);

}