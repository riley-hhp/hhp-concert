package io.hhplus.concert.app.infra.jpa.user;

import io.hhplus.concert.app.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> { }
