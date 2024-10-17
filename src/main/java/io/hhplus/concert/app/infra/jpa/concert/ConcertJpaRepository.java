package io.hhplus.concert.app.infra.jpa.concert;

import io.hhplus.concert.app.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertJpaRepository extends JpaRepository<Concert, Long> {



}
