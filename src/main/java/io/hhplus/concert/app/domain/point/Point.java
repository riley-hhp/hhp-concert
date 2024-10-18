package io.hhplus.concert.app.domain.point;

import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@Entity
public class Point extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    long id;
    long userId;
    double balance;

    // 포인트 충전
    public void addPoints(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        this.balance += amount;
    }

    // 포인트 차감
    public void subtractPoints(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("차감 금액은 0보다 커야 합니다.");
        }
        if (this.balance < amount) {
            throw new RuntimeException("잔액이 부족합니다.");
        }
        this.balance -= amount;
    }

}
