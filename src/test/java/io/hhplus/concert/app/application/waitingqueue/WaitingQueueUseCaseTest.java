package io.hhplus.concert.app.application.waitingqueue;

import io.hhplus.concert.app.domain.waitingqueue.WaitingQueue;
import io.hhplus.concert.app.infra.jpa.waitingqueue.WaitingQueueCoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName(value = "WaitingQueueService 단위 테스트")
@ExtendWith(MockitoExtension.class)
class WaitingQueueUseCaseTest {

    @Mock
    WaitingQueueCoreRepository waitingQueueCoreRepository;

    @InjectMocks
    WaitingQueueService waitingQueueUseCase;

    @Test
    void 토큰_발급_성공() {
        // Given
        Long concertId = 1L;
        WaitingQueue expectedQueue = WaitingQueue.builder()
                                                .token("test-token")
                                                .concertId(concertId)
                                                .build();

        when(waitingQueueCoreRepository.issueToken(concertId)).thenReturn(expectedQueue);

        // When
        WaitingQueue actualQueue = waitingQueueUseCase.issueToken(concertId);

        // Then
        assertThat(actualQueue.getToken()).isEqualTo(expectedQueue.getToken());
        assertThat(actualQueue.getConcertId()).isEqualTo(expectedQueue.getConcertId());
    }

    @Test
    void 토큰_조회_성공() {
        // Given
        String token = "test-token";
        WaitingQueue expectedQueue = WaitingQueue.builder()
                                                .token(token)
                                                .concertId(1L)
                                                .build();

        when(waitingQueueCoreRepository.getToken(token)).thenReturn(expectedQueue);

        // When
        WaitingQueue actualQueue = waitingQueueUseCase.getToken(token);

        // Then
        assertThat(actualQueue.getToken()).isEqualTo(expectedQueue.getToken());
    }

    @Test
    void 토큰_활성화_성공() {

        // When
        waitingQueueUseCase.activeToken();

        // Then
        verify(waitingQueueCoreRepository).activeToken();
    }

    @Test
    void 토큰_만료_성공() {

        // When
        waitingQueueUseCase.expireToken();

        // Then
        verify(waitingQueueCoreRepository).expireToken();
    }
}