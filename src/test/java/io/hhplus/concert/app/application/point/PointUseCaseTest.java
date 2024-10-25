package io.hhplus.concert.app.application.point;

import io.hhplus.concert.app.domain.point.Point;
import io.hhplus.concert.app.domain.point.PointHistory;
import io.hhplus.concert.app.domain.point.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointUseCaseTest {

    @InjectMocks
    private PointService pointUseCase;

    @Mock
    private PointRepository pointRepository;

    private Point point;

    @BeforeEach
    void setUp() {
        point = Point.builder().userId(1L).balance(1000).build();
    }

    @Test
    void 사용자의_포인트를_정상적으로_조회한다() {
        // given
        when(pointRepository.findPoinByUserId(1L)).thenReturn(Optional.of(point));

        // when
        int balance = pointUseCase.getUserPoints(1L);

        // then
        assertEquals(1000, balance);
    }

    @Test
    void 사용자의_포인트를_찾을_수_없을_경우_예외가_발생한다() {
        // given
        when(pointRepository.findPoinByUserId(1L)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pointUseCase.getUserPoints(1L);
        });
        assertEquals("사용자의 포인트를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 포인트를_정상적으로_충전한다() {
        // given
        when(pointRepository.findPoinByUserId(1L)).thenReturn(Optional.of(point));

        // when
        pointUseCase.chargePoints(1L, 500);

        // then
        assertEquals(1500, point.getBalance());
        verify(pointRepository, times(1)).save(point);
        verify(pointRepository, times(1)).save(any(PointHistory.class)); // 포인트 내역 저장 검증
    }

    @Test
    void 포인트를_충전할_사용자_정보를_찾을_수_없을_경우_예외가_발생한다() {
        // given
        when(pointRepository.findPoinByUserId(1L)).thenReturn(Optional.empty());

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pointUseCase.chargePoints(1L, 500);
        });
        assertEquals("사용자의 포인트 정보를 찾을 수 없습니다.", exception.getMessage());
    }

    @Test
    void 포인트가_부족할_경우_예외가_발생한다() {
        // given
        when(pointRepository.findPoinByUserId(1L)).thenReturn(Optional.of(point));

        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pointUseCase.usePoints(1L, 1500); // 잔액보다 많은 포인트 사용 요청
        });
        assertEquals("포인트가 부족합니다.", exception.getMessage());
    }

    @Test
    void 포인트를_정상적으로_사용한다() {
        // given
        when(pointRepository.findPoinByUserId(1L)).thenReturn(Optional.of(point));

        // when
        pointUseCase.usePoints(1L, 500);

        // then
        assertEquals(500, point.getBalance());
        verify(pointRepository, times(1)).save(point);
        verify(pointRepository, times(1)).save(any(PointHistory.class)); // 포인트 내역 저장 검증
    }
}