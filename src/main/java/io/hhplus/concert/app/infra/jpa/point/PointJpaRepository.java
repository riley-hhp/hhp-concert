package io.hhplus.concert.app.infra.jpa.point;

import io.hhplus.concert.app.domain.point.Point;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PointJpaRepository extends JpaRepository<Point, Long> {

    // 락 없이 조회하는 메서드
    Optional<Point> findByUserId(long userId);

    // PESSIMISTIC_WRITE 락을 적용한 조회 메서드
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p WHERE p.userId = :userId")
    Optional<Point> findByUserIdWithLock(long userId);
}
