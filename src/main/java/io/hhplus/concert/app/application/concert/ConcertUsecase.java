package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.domain.concert.ConcertItem;
import io.hhplus.concert.app.domain.concert.Reservation;
import io.hhplus.concert.app.domain.concert.Seat;

import java.util.List;

public interface ConcertUsecase {


    // 예약 가능 날짜 조회 API
    public List<ConcertItem> getAvailableDates(long concertId);

    // 좌석 조회 API
    public List<Seat> getAvailableSeats(long concertItemId);

    // 좌석 예약 요청 API
    public Reservation reserveSeatAndPay(long userId, long concertItemId, long seatId);

    // 5분 이내 결제가 이루어지지 않은 가예약을 취소
    public void cancelExpiredReservations();

}
