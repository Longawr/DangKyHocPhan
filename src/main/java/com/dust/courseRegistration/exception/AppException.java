package com.dust.courseRegistration.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private static final long serialVersionUID = -7726343484848132176L;

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
