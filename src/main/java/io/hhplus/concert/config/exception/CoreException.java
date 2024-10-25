package io.hhplus.concert.config.exception;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {

    private final ErrorCode errorCode;

    public CoreException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
