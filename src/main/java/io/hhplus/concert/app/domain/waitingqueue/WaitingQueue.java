package io.hhplus.concert.app.domain.waitingqueue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.hhplus.concert.config.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WaitingQueue extends BaseTimeEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    private long concertId;
    private String token;
    @Setter
    private LocalDateTime expiredAt;

    @Enumerated(EnumType.STRING)
    private WaitingQueueStatus status;

    public static final int ACTIVE_SIZE = 10;

    public static WaitingQueue issue(long concertId) {

        return WaitingQueue.builder()
                .token(UUID.randomUUID().toString())
                .status(WaitingQueueStatus.WAITING)
                .concertId(concertId).build();
    }

    public WaitingQueue activate() {

        if ( this.status == WaitingQueueStatus.EXPIRED || this.expiredAt != null ) {
            throw new IllegalArgumentException("만료된 대기열을 활성화할 수 없습니다.");
        }
        if ( this.status == WaitingQueueStatus.ACTIVE ) {
            throw new IllegalArgumentException("활성상태의 대기열을 활성화할 수 없습니다.");
        }
        this.status = WaitingQueueStatus.ACTIVE;
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
        return this;
    }

    public WaitingQueue expire() {

        if ( this.status == WaitingQueueStatus.EXPIRED ) {
            throw new IllegalArgumentException("만료된 대기열을 만료할 수 없습니다.");
        }

        this.status = WaitingQueueStatus.EXPIRED;
        return this;
    }

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static String stringify(Object waitingQueue) {
        try {
            return objectMapper.writeValueAsString(waitingQueue);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static WaitingQueue parseWaitingQueue(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, WaitingQueue.class);
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
