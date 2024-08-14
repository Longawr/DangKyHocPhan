package com.dust.courseRegistration.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    SYSTEM_ERROR(9998, "System error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Invalid field", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Not have permission", HttpStatus.FORBIDDEN),
    INCORRECT_PASSWORD(1008, "Password incorrect", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1009, "Token expired", HttpStatus.UNAUTHORIZED),
    ITEM_NOT_EXISTED(1010, "Item not existed", HttpStatus.BAD_REQUEST),
    ITEM_EXISTED(1011, "Item existed", HttpStatus.BAD_REQUEST),
    ILLEGAL_ARGUMENT(1012, "Illegal Argument", HttpStatus.BAD_REQUEST),
    NOT_ACCESSABLE(1013, "Data cannot be modified now", HttpStatus.BAD_REQUEST),
    PAGE_NOT_FOUND(1014, "Url not recognize", HttpStatus.NOT_FOUND),
    MEDIA_TYPE_NOT_SUPPORTED(1015, "Content-Type of request is not supported", HttpStatus.BAD_REQUEST),
    MISSING_BODY(1016, "Missing body", HttpStatus.BAD_REQUEST),
    MISSING_PARAMS(1017, "Missing params", HttpStatus.BAD_REQUEST);

    private final int id;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int id, String message, HttpStatus statusCode) {
        this.id = id;
        this.message = message;
        this.statusCode = statusCode;
    }
}
