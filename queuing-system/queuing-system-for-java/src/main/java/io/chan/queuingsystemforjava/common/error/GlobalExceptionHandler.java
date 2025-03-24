package io.chan.queuingsystemforjava.common.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> handleAllExceptions(Exception e) {
        log.error("예측하지 못한 예외 발생. 메시지={}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(TicketingException.class)
    public ResponseEntity<ErrorResponse<Void>> handleTicketingException(TicketingException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("예외 발생. 메세지={}", errorCode.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusValue())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<List<ValidationErrorDetail>>>
    handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse<List<ValidationErrorDetail>> errorResponse = ErrorResponse.of(e);
        log.info("API 요청 데이터 오류. 메세지={}", e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
