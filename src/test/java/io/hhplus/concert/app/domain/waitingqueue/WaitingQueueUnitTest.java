package io.hhplus.concert.app.domain.waitingqueue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName(value = "WaitingQueue 단위 테스트")
class WaitingQueueUnitTest {

    @Nested
    @DisplayName("활성화 테스트")
    class ActivateTest {

        @Test
        void 이미_활성화된_토큰() {
            // given
            WaitingQueue waitingQueue = WaitingQueue.builder()
                                                    .token(UUID.randomUUID().toString())
                                                    .concertId(1L)
                                                    .status(WaitingQueueStatus.ACTIVE)
                                                    .build();

            // when
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, waitingQueue::activate);
        }

        @Test
        void 만료된_토큰() {
            // given
            WaitingQueue waitingQueue = WaitingQueue.builder()
                                                    .token(UUID.randomUUID().toString())
                                                    .concertId(1L)
                                                    .status(WaitingQueueStatus.EXPIRED)
                                                    .build();
            LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

            // when
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, waitingQueue::activate);
        }


        @Test
        void 활성화_성공() {
            // given
            WaitingQueue waitingQueue = WaitingQueue.builder()
                                                    .token(UUID.randomUUID().toString())
                                                    .concertId(1L)
                                                    .status(WaitingQueueStatus.WAITING)
                                                    .build();
            LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

            // when
            waitingQueue.activate();

            // then
            assertThat(waitingQueue.getStatus()).isEqualTo(WaitingQueueStatus.ACTIVE);
            assertThat(waitingQueue.getExpiredAt()).isCloseTo(expiredAt, within(1, ChronoUnit.SECONDS));
        }

    }

    @Nested
    @DisplayName("만료 테스트")
    class ExpireTest {
        @Test
        void 이미_만료된_토큰() {
            // given
            WaitingQueue waitingQueue = WaitingQueue.builder()
                                                    .token(UUID.randomUUID().toString())
                                                    .concertId(1L)
                                                    .status(WaitingQueueStatus.EXPIRED)
                                                    .build();
            LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(5);

            // when
            IllegalArgumentException result = assertThrows(IllegalArgumentException.class, waitingQueue::expire);
        }

        @Test
        void 만료_성공() {
            // given
            WaitingQueue waitingQueue = WaitingQueue.builder()
                                                    .token(UUID.randomUUID().toString())
                                                    .concertId(1L)
                                                    .status(WaitingQueueStatus.ACTIVE)
                                                    .build();

            // when
            waitingQueue.expire();

            // then
            assertThat(waitingQueue.getStatus()).isEqualTo(WaitingQueueStatus.EXPIRED);
        }
    }


}