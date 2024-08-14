package com.dust.courseRegistration.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.dust.courseRegistration.dto.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<Void>> handlingRuntimeException(RuntimeException exception) {
        log.error("System Exception: ", exception);
        var error = ErrorCode.UNCATEGORIZED_EXCEPTION;
        var apiResponse = ApiResponse.<Void>builder()
                .id(error.getId())
                .message(error.getMessage())
                .result(null)
                .build();

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(AppException ex) {
        log.error("Application exception occurred: " + ex.getMessage());
        ErrorCode errorCode = ex.getErrorCode();
        var apiResponse = ApiResponse.<Void>builder()
                .id(errorCode.getId())
                .message(errorCode.getMessage())
                .build();

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        var errorCode = ErrorCode.INVALID_KEY;
        var errors = new HashMap<String, String>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        var apiResponse = ApiResponse.<Map<String, String>>builder()
                .id(errorCode.getId())
                .message(errorCode.getMessage())
                .result(errors)
                .build();

        log.error("Validation error occurred: ", errors);
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleParametersIllegalException(IllegalArgumentException ex) {
        log.error("Illegal Argument Exception: {}", ex.getMessage());
        var error = ErrorCode.ILLEGAL_ARGUMENT;
        var apiResponse = ApiResponse.<Void>builder()
                .id(error.getId())
                .message(error.getMessage() + ": " + ex.getMessage())
                .build();

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingPathVariableException(MissingPathVariableException ex) {
        log.error("Missing Path Variable Exception:", ex);
        var error = ErrorCode.SYSTEM_ERROR;
        var apiResponse = ApiResponse.<Void>builder()
                .id(error.getId())
                .message(error.getMessage())
                .build();

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException ex) {
        log.error("No Resource Found Exception: Path: /{}, Message: {}", ex.getResourcePath(), ex.getMessage());

        var error = ErrorCode.PAGE_NOT_FOUND;
        var apiResponse = ApiResponse.<Void>builder()
                .id(error.getId())
                .message(error.getMessage())
                .build();

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleJwtException(HttpMediaTypeNotSupportedException ex) {
        log.error("Http Media Type Not Supported Exception: {}", ex.getMessage());
        var error = ErrorCode.MEDIA_TYPE_NOT_SUPPORTED;
        var apiResponse = ApiResponse.<Void>builder()
                .id(error.getId())
                .message(error.getMessage())
                .build();

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleJwtException(AuthorizationDeniedException ex) {
        log.error("Authorization Denied Exception: {}", ex.getMessage());
        var error = ErrorCode.UNAUTHORIZED;
        var apiResponse = ApiResponse.<Void>builder()
                .id(error.getId())
                .message(error.getMessage())
                .build();

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error("Http Message Not Readable Exception: {}", ex.getMessage());
        var error = ErrorCode.MISSING_BODY;
        var apiResponse = ApiResponse.<Void>builder()
                .id(error.getId())
                .message(error.getMessage())
                .build();

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalStateException(MissingServletRequestParameterException ex) {
        log.error("Missing Servlet Request Parameter Exception: {}", ex.getMessage());
        var error = ErrorCode.MISSING_PARAMS;
        var apiResponse = ApiResponse.<Void>builder()
                .id(error.getId())
                .message(error.getMessage())
                .build();

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }
}
