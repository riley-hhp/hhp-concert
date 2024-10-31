package io.hhplus.concert.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND,  "콘서트를 찾을 수 없습니다."),

    CONCERT_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "콘서트 아이템을 찾을 수 없습니다."),

    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "좌석을 찾을 수 없습니다."),

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약을 찾을 수 없습니다."),

    USER_RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자의 예약이 없습니다."),

    PAYMENT_TIME_OUT(HttpStatus.REQUEST_TIMEOUT, "결제 시간이 초과되었습니다."),

    ALREADY_RESERVED_SEAT(HttpStatus.CONFLICT, "이미 예약된 좌석입니다."),

    INVALID_SEAT_STATE(HttpStatus.BAD_REQUEST, "거래가 상태가 아닙니다."),

    PAYMENT_ALREADY_COMPLETED(HttpStatus.CONFLICT, "결제가 이미 완료되었거나 실패한 상태입니다."),

    USER_POINTS_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 포인트를 찾을 수 없습니다."),

    POINTS_LACK(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),

    NO_MORE_SEATS(HttpStatus.FORBIDDEN, "더 이상 예약할 수 없습니다."),

    NO_SUCH_TIER(HttpStatus.BAD_REQUEST, "충전 금액은 0보다 커야 합니다."),

    TIER_AMOUNT_INVALID(HttpStatus.BAD_REQUEST, "차감 금액은 0보다 커야 합니다."),

    AMOUNT_LACK(HttpStatus.BAD_REQUEST, "잔액이 부족합니다."),

    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),

    CANNOT_ACTIVATE_PROMO(HttpStatus.BAD_REQUEST, "만료된 대기열을 활성화할 수 없습니다."),

    CANNOT_ACTIVATE_PROMO_STATE(HttpStatus.BAD_REQUEST, "활성상태의 대기열을 활성화할 수 없습니다."),

    CANNOT_COMPLETE_PROMO(HttpStatus.BAD_REQUEST, "만료된 대기열을 만료할 수 없습니다."),

    NOT_FOUND(HttpStatus.NOT_FOUND,  "찾을 수 없습니다."),

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,  "서버 에러 발생."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
