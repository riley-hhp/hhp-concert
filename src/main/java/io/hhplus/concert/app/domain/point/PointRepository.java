package io.hhplus.concert.app.domain.point;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointRepository{

    Optional<Point> findPoinByUserId(long userId);
    Optional<Point> findPoinByUserIdWithLock(long userId);
    Optional<PointHistory> findPointHistoryByUserId(long userId);

    void save(Point point);
    void save(PointHistory pointHistory);
}
