package io.hhplus.concert.app.infra.jpa.point;

import io.hhplus.concert.app.domain.point.Point;
import io.hhplus.concert.app.domain.point.PointHistory;
import io.hhplus.concert.app.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointCoreRepository implements PointRepository {

    private final PointJpaRepository pointJpaRepository;
    private final PointHistoryJpaRepository pointHistoryJpaRepository;


    PointHistory savePointHistory ( PointHistory pointHistory) {

        return pointHistoryJpaRepository.save(pointHistory);
    }


    @Override
    public Optional<Point> findPoinByUserId(long userId) {
        return pointJpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<PointHistory> findPointHistoryByUserId(long userId) {
        return pointHistoryJpaRepository.findByUserId(userId);
    }

    @Override
    public void save(Point point) {
        pointJpaRepository.save(point);
    }

    @Override
    public void save(PointHistory pointHistory) {
        pointHistoryJpaRepository.save(pointHistory);
    }
}
