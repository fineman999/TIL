package io.chan.paymentservice.config.exception.handler;

import io.chan.paymentservice.config.exception.CustomIOException;
import io.chan.paymentservice.config.exception.CustomIamportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GeneralExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    private ResponseEntity<?> newResponse(String message, HttpStatus status) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return new ResponseEntity<>(message, headers, status);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, IllegalArgumentException.class, CustomIamportException.class})
    public ResponseEntity<?> handleException(HttpMessageNotReadableException e) {
        logger.error("HttpMessageNotReadableException: {}", e.getMessage());
        return newResponse(e.getMostSpecificCause().getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({CustomIOException.class})
    public ResponseEntity<?> handleException(CustomIamportException e) {
        logger.error("MailSendException: {}", e.getMessage());
        return newResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
