package io.hhplus.concert.app.application.concert;

import io.hhplus.concert.app.application.payment.PaymentCoreUsecase;
import io.hhplus.concert.app.domain.concert.*;
import io.hhplus.concert.app.domain.payment.Payment;
import io.hhplus.concert.config.lock.DistributedLock;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertCoreUsecase implements ConcertUsecase {

    private final ConcertRepository concertRepository;
    private final PaymentCoreUsecase paymentCoreUsecase;
    private final ConcertEventPublisher concertEventPublisher;

    // 예약 가능 날짜 조회 API
    @Cacheable(value = "concerts", key = "#concertId")
    public List<ConcertItem> getAvailableDates(long concertId) {

        return concertRepository.findAvailableConcertItems(concertId);
    }

    // 좌석 조회 API
    @Cacheable(value = "seats", key = "#concertItemId")
    public List<Seat> getAvailableSeats(long concertItemId) {

        return concertRepository.findAvailableSeats(concertItemId);
    }

    // 좌석 예약 요청 API
    @DistributedLock(key = "T(String).valueOf(#concertItemId) + '-' + T(String).valueOf(#seatId)")
    public Reservation reserveSeatAndPay(long userId, long concertItemId, long seatId) {

        // 가예약 생성
        Reservation reservation = concertRepository.createTemporaryReservation(userId, concertItemId, seatId);

        // 결제 처리
        Payment payment = paymentCoreUsecase.processPayment(reservation);

        // 결제 성공 확인 후 예약 확정
        Reservation confirmed = concertRepository.confirmReservation(reservation.getId(), payment);

        // 예약 성공시 처리될 부가적인 외부로직(이벤트발행)
        concertEventPublisher.successReservation(confirmed);
        return confirmed;
    }

    // 5분 이내 결제가 이루어지지 않은 가예약을 취소
    @Transactional
    public void cancelExpiredReservations() {

        concertRepository.cancelExpiredReservations();
    }

}
