package io.chan.queuingsystemforjava.common.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse<Void>> handleAllExceptions(Exception e, HttpServletRequest request) {
        logException("에러발생", e, e.getMessage(), "ERROR");
        if (MediaType.TEXT_EVENT_STREAM_VALUE.equals(request.getHeader("Accept"))) {
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(TicketingException.class)
    public ResponseEntity<ErrorResponse<Void>> handleTicketingException(TicketingException e) {
        ErrorCode errorCode = e.getErrorCode();
        logException("예외발생", e, errorCode.getMessage(), "WARN");
        return ResponseEntity.status(errorCode.getHttpStatusValue())
                .body(ErrorResponse.of(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<List<ValidationErrorDetail>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse<List<ValidationErrorDetail>> errorResponse = ErrorResponse.of(e);
        logException("API요청오류", e, e.getMessage(), "INFO");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    private void logException(String prefix, Exception e, String message, String logLevel) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String requestUri = attributes != null ? attributes.getRequest().getRequestURI() : "unknown";
        String requestMethod = attributes != null ? attributes.getRequest().getMethod() : "unknown";

        StackTraceElement[] stackTrace = e.getStackTrace();
        String errorLocation = Arrays.stream(stackTrace)
                .filter(element -> element.getClassName().startsWith("io.chan.queuingsystemforjava"))
                .findFirst()
                .map(element -> String.format("[%s.java] 파일의 [%d] 라인", element.getClassName(), element.getLineNumber()))
                .orElse("[알 수 없는 위치]");

        switch (logLevel) {
            case "ERROR":
                log.error("##{}: {}에서 발생한 에러 - 요청 URI: {}, 메서드: {}, 메시지: {}",
                        prefix, errorLocation, requestUri, requestMethod, message, e);
                break;
            case "WARN":
                log.warn("##{}: {}에서 발생한 에러 - 요청 URI: {}, 메서드: {}, 메시지: {}",
                        prefix, errorLocation, requestUri, requestMethod, message, e);
                break;
            case "INFO":
                log.info("##{}: {}에서 발생한 에러 - 요청 URI: {}, 메서드: {}, 메시지: {}",
                        prefix, errorLocation, requestUri, requestMethod, message, e);
                break;
        }
    }
}
