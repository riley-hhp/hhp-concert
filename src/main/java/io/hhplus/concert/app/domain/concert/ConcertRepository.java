package io.hhplus.concert.app.domain.concert;

import io.hhplus.concert.app.domain.payment.Payment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConcertRepository {

    // 콘서트 ID로 콘서트 조회
    Concert findConcertById(long concertId);

    // 예약 가능한 콘서트 아이템 조회
    List<ConcertItem> findAvailableConcertItems(long concertId);

    // 콘서트 아이템 ID로 좌석 조회
    List<Seat> findAvailableSeats(long concertItemId);

    // 임시 예약 생성
    Reservation createTemporaryReservation(long userId, long concertItemId, long seatId);

    // 예약 확정
    Reservation confirmReservation(long reservationId, Payment payment);

    // 만료된 예약 취소
    void cancelExpiredReservations();

    Reservation findReservationByUserId(long userId);
    Reservation findReservationById(long reservationId);

    void save(Concert concert);
    void save(ConcertItem concertItem);
    void save(Seat seat);

    void deleteAll();

    List<Reservation> findReservationsBySeatId(Long seatId);

    List<Reservation> findExpiredReservations();
}