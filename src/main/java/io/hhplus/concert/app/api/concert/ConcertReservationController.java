package io.hhplus.concert.app.api.concert;

import io.hhplus.concert.app.application.concert.ConcertUseCase;
import io.hhplus.concert.app.domain.concert.ConcertItem;
import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.concert.Seat;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/concerts")
@RequiredArgsConstructor
public class ConcertReservationController {

    private final ConcertUseCase concertUseCase;

    // 예약 가능 날짜 조회 API
    @Operation(summary = "예약 가능 날짜 조회", description = "특정 콘서트 ID에 대한 예약 가능한 날짜를 조회합니다.")
    @GetMapping("/{concertId}/available-dates")
    public ResponseEntity<List<ConcertItem>> getAvailableDates(@PathVariable long concertId) {
        List<ConcertItem> availableDates = concertUseCase.getAvailableDates(concertId);
        return ResponseEntity.ok(availableDates);
    }

    // 좌석 조회 API
    @Operation(summary = "좌석 조회", description = "특정 콘서트 아이템 ID에 대한 예약 가능한 좌석을 조회합니다.")
    @GetMapping("/concert-items/{concertItemId}/available-seats")
    public ResponseEntity<List<Seat>> getAvailableSeats(@PathVariable long concertItemId) {
        List<Seat> availableSeats = concertUseCase.getAvailableSeats(concertItemId);
        return ResponseEntity.ok(availableSeats);
    }

    // 좌석 예약 요청 API
    @Operation(summary = "좌석 예약 요청", description = "사용자가 지정한 콘서트 아이템 ID와 좌석 ID로 좌석을 예약하고 결제를 처리합니다.")
    @PostMapping("/reserve")
    public ResponseEntity<Reservation> reserveSeatAndPay(
            @RequestParam long userId,
            @RequestParam long concertItemId,
            @RequestParam long seatId) {

        concertUseCase.reserveSeatAndPay(userId, concertItemId, seatId);
        return ResponseEntity.ok().build();
    }

    // 만료된 예약 취소 API
    @Operation(summary = "만료된 예약 취소", description = "5분 이내 결제가 이루어지지 않은 가예약을 취소합니다.")
    @DeleteMapping("/cancel-expired-reservations")
    public ResponseEntity<Void> cancelExpiredReservations() {
        concertUseCase.cancelExpiredReservations();
        return ResponseEntity.noContent().build();
    }
}