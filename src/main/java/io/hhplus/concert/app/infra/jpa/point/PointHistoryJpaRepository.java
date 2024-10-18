package io.hhplus.concert.app.infra.jpa.point;

import io.hhplus.concert.app.domain.point.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {

    Optional<PointHistory> findByUserId(long userId);
}
