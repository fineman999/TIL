package io.chan.queuingsystemforjava.domain.payment.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.PaymentException;
import io.chan.queuingsystemforjava.common.error.PaymentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.util.Map;

public class PaymentExceptionInterceptor implements ClientHttpRequestInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(PaymentExceptionInterceptor.class);
    private final ObjectMapper objectMapper;

    public PaymentExceptionInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        try {
            ClientHttpResponse response = execution.execute(request, body);
            if (response.getStatusCode().isError()) {
                String errorBody = new String(response.getBody().readAllBytes());
                logger.error("Payment API failed for request {} with status {}: {}",
                        request.getURI(), response.getStatusCode().value(), errorBody);

                ErrorCode errorCode = mapToErrorCode(response.getStatusCode().value(), errorBody);
                throw new PaymentException(errorCode,
                        String.format("Status: %d, Body: %s", response.getStatusCode().value(), errorBody));
            }
            return response;
        } catch (IOException e) {
            logger.error("Failed to communicate with payment service for request {}", request.getURI(), e);
            throw new PaymentException(ErrorCode.PAYMENT_ERROR, "Failed to communicate with payment service", e);
        } catch (Exception e) {
            logger.error("Unexpected error during payment request {}", request.getURI(), e);
            throw new PaymentException(ErrorCode.PAYMENT_ERROR, "Unexpected error during payment request", e);
        }
    }

    private ErrorCode mapToErrorCode(int statusCode, String errorBody) {
        try {
            Map<String, String> errorResponse = objectMapper.readValue(errorBody, Map.class);
            String errorCodeStr = errorResponse.get("code");
            if (errorCodeStr == null) {
                logger.warn("No error code found in response: status={}, body={}", statusCode, errorBody);
                return ErrorCode.PAYMENT_ERROR;
            }

            try {
                return ErrorCode.valueOf(errorCodeStr);
            } catch (IllegalArgumentException e) {
                logger.warn("Unknown error code '{}' in response: status={}, body={}", errorCodeStr, statusCode, errorBody);
                return ErrorCode.PAYMENT_ERROR;
            }
        } catch (Exception e) {
            logger.error("Failed to parse error response: status={}, body={}, error={}", statusCode, errorBody, e.getMessage());
            return ErrorCode.PAYMENT_ERROR;
        }
    }
}