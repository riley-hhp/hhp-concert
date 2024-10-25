package io.hhplus.concert.app.infra.jpa.concert;

import io.hhplus.concert.app.domain.concert.*;
import io.hhplus.concert.app.domain.payment.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ConcertCoreRepository implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertItemJpaRepository concertItemJpaRepository;
    private final SeatJpaRepository seatJpaRepository;
    private final ReservationJpaRepository reservationJpaRepository;


    // 콘서트 ID로 콘서트 조회
    public Concert findConcertById(long concertId) {

        return concertJpaRepository.findById(concertId)
                .orElseThrow(() -> new RuntimeException("콘서트를 찾을 수 없습니다."));
    }

    // 예약 가능한 콘서트 아이템 조회
    public List<ConcertItem> findAvailableConcertItems(long concertId) {

        findConcertById(concertId);
        return concertItemJpaRepository.findByConcertIdAndSessionAtBeforeAndCapacityGreaterThan(
                concertId, LocalDateTime.now(), 0);
    }

    // 콘서트 아이템 ID로 좌석 조회
    public List<Seat> findAvailableSeats(long concertItemId) {

        return seatJpaRepository.findByConcertItemIdAndStatus(concertItemId, SeatStatus.AVAILABLE);
    }


    @Transactional
    public Reservation createTemporaryReservation(long userId, long concertItemId, long seatId) {

        Seat seat = seatJpaRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("좌석을 찾을 수 없습니다."));
        ConcertItem concertItem = concertItemJpaRepository.findById(concertItemId)
                .orElseThrow(() -> new RuntimeException("콘서트 아이템을 찾을 수 없습니다."));

        // 좌석 임시 예약
        seat.reserveTemporarily();
        seatJpaRepository.save(seat);

        // 콘서트 아이템 좌석 감소
        concertItem.decreaseCapacity();
        concertItemJpaRepository.save(concertItem);

        // 가예약 생성
        Reservation reservation = Reservation.builder()
                                             .userId(userId)
                                             .concertItemId(concertItemId)
                                             .seatId(seatId)
                                             .reservedAt(LocalDateTime.now())
                                             .status(ReservationStatus.TEMP)
                                             .build();
        reservationJpaRepository.save(reservation);

        return reservation;
    }

    @Transactional
    public void confirmReservation(long reservationId, Payment payment) {

        Reservation reservation = reservationJpaRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("예약을 찾을 수 없습니다."));

        Seat seat = seatJpaRepository.findById(reservation.getSeatId())
                .orElseThrow(() -> new RuntimeException("좌석을 찾을 수 없습니다."));

        // 5분 이내 결제 확인 후 예약 확정
        if (reservation.getReservedAt().plusMinutes(5).isAfter(payment.getPaymentAt())) {
            seat.confirmReservation();
            seatJpaRepository.save(seat);
            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservation.setPaymentId(payment.getId());
            reservationJpaRepository.save(reservation);
        }
        else {
            throw new RuntimeException("결제 시간이 초과되었습니다.");
        }
    }

    @Transactional
    public void cancelExpiredReservations() {

        List<Reservation> expiredReservations = reservationJpaRepository.findAllByStatusAndReservedAtBefore(ReservationStatus.TEMP, LocalDateTime.now().minusMinutes(5));

        for (Reservation reservation : expiredReservations) {
            Seat seat = seatJpaRepository.findById(reservation.getSeatId())
                    .orElseThrow(() -> new RuntimeException("좌석을 찾을 수 없습니다."));
            ConcertItem concertItem = concertItemJpaRepository.findById(reservation.getConcertItemId())
                    .orElseThrow(() -> new RuntimeException("콘서트 아이템을 찾을 수 없습니다."));

            // 좌석 상태 복원, 콘서트 아이템 좌석 복원
            seat.restoreIfExpired(reservation);
            seatJpaRepository.save(seat);
            concertItem.decreaseCapacity();
            concertItemJpaRepository.save(concertItem);

            reservation.setStatus(ReservationStatus.FAIL);
            reservationJpaRepository.save(reservation);
        }
    }

}
