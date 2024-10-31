package io.hhplus.concert.app.domain.point;

import io.hhplus.concert.config.BaseTimeEntity;
import io.hhplus.concert.config.exception.CoreException;
import io.hhplus.concert.config.exception.ErrorCode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Point extends BaseTimeEntity {

    @Id
    long userId;
    double balance;

    // 포인트 충전
    public void addPoints(double amount) {
        if (amount <= 0) {
            throw new CoreException(ErrorCode.NO_SUCH_TIER);
        }
        this.balance += amount;
    }

    // 포인트 차감
    public void subtractPoints(double amount) {
        if (amount <= 0) {
            throw new CoreException(ErrorCode.TIER_AMOUNT_INVALID);
        }
        if (this.balance < amount) {
            throw new CoreException(ErrorCode.POINTS_LACK);
        }
        this.balance -= amount;
    }

}
