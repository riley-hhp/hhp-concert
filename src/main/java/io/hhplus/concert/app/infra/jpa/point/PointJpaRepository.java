package io.hhplus.concert.app.infra.jpa.point;

import io.hhplus.concert.app.domain.point.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointJpaRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByUserId(long userId);
}
