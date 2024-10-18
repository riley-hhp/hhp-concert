package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.application.payment.PaymentUseCase;
import io.hhplus.concert.app.domain.concert.*;
import io.hhplus.concert.app.domain.payment.Payment;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertUseCase {

    private final ConcertRepository concertRepository;
    private final PaymentUseCase paymentUseCase;


    // 예약 가능 날짜 조회 API
    public List<ConcertItem> getAvailableDates(long concertId) {

        return concertRepository.findAvailableConcertItems(concertId);
    }

    // 좌석 조회 API
    public List<Seat> getAvailableSeats(long concertItemId) {

        return concertRepository.findAvailableSeats(concertItemId);
    }

    // 좌석 예약 요청 API
    @Transactional
    public void reserveSeatAndPay(long userId, long concertItemId, long seatId) {

        // 가예약 생성
        Reservation reservation = concertRepository.createTemporaryReservation(userId, concertItemId, seatId);

        // 결제 처리
        Payment payment = paymentUseCase.processPayment(reservation.getPaymentId());

        // 결제 성공 확인 후 예약 확정
        concertRepository.confirmReservation(reservation.getId(), payment);
    }

    // 5분 이내 결제가 이루어지지 않은 가예약을 취소
    @Transactional
    public void cancelExpiredReservations() {

        concertRepository.cancelExpiredReservations();
    }

}
