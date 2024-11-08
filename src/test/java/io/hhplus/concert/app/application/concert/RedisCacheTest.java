package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.domain.concert.ConcertItem;
import io.hhplus.concert.app.domain.concert.ConcertRepository;
import io.hhplus.concert.app.domain.concert.Seat;
import io.hhplus.concert.app.domain.concert.SeatStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@DisplayName("레디스 캐시 테스트")
@SpringBootTest
public class RedisCacheTest {

    @Autowired
    private ConcertUseCase concertUseCase;

    @MockBean
    private ConcertRepository concertRepository;

    @Autowired
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        // 테스트 전에 캐시를 비워줍니다.
        cacheManager.getCache("concerts").clear();
        cacheManager.getCache("seats").clear();
    }

    @Test
    void 예약가능한_날짜_조회하기_캐시테스트() {

        // given
        ConcertItem concertItem = new ConcertItem();
        concertItem.setCapacity(50);
        concertItem.setSessionAt(LocalDateTime.now().plusDays(10));

        ConcertItem concertItem1 = new ConcertItem();
        concertItem1.setCapacity(50);
        concertItem1.setSessionAt(LocalDateTime.now().plusDays(10));
        List<ConcertItem> availableConcertItems = List.of(concertItem, concertItem1);
        when(concertRepository.findAvailableConcertItems(1L)).thenReturn(availableConcertItems);

        // when
        // 첫 번째 호출
        List<ConcertItem> firstCallItems = concertUseCase.getAvailableDates(1L);

        // 두 번째 호출
        List<ConcertItem> secondCallItems = concertUseCase.getAvailableDates(1L);

        // then
        // 캐시 적용 여부 확인
        verify(concertRepository, times(1)).findAvailableConcertItems(1L);

    }

    @Test
    void 예약가능한_좌석_조회하기_캐시테스트() {

        long concertItemId = 1L;

        // given
        Seat seat1 = new Seat();
        seat1.setSeatNumber(1);
        seat1.setStatus(SeatStatus.AVAILABLE);

        Seat seat2 = new Seat();
        seat2.setSeatNumber(2);
        seat2.setStatus(SeatStatus.AVAILABLE);

        List<Seat> availableSeats = List.of(seat1, seat2); // 예시 좌석 2개 생성
        when(concertRepository.findAvailableSeats(concertItemId)).thenReturn(availableSeats);

        // when
        // 첫 번째 호출
        List<Seat> firstCallSeats = concertUseCase.getAvailableSeats(concertItemId);

        // 두 번째 호출
        List<Seat> secondCallSeats = concertUseCase.getAvailableSeats(concertItemId);

        // then
        // 캐시 적용 여부 확인
        verify(concertRepository, times(1)).findAvailableSeats(concertItemId);

    }
}