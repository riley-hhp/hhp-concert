package io.hhplus.concert.app.domain.outbox;

import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "outbox")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Outbox extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OUTBOX_ID")
    private Long id;

    @Enumerated(EnumType.STRING)
    private OutboxType outboxType;

    private Long targetId;

    @Enumerated(EnumType.STRING)
    private OutboxStatus outboxStatus;

    @Column(name = "payload", columnDefinition = "json")
    private String payload;

    @Version
    private Long version;

    public Outbox(OutboxType outboxType, Long targetId, String payload) {
        super();
        this.outboxType = outboxType;
        this.targetId = targetId;
        this.payload = payload;
        this.outboxStatus = OutboxStatus.INIT;
        this.version = 0L;
    }

    public void outBoxPublish() {
        this.outboxStatus = OutboxStatus.PUBLISH;
    }

}
