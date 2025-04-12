package io.chan.queuingsystemforjava.domain.payment.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.IgnoredPropertyException;
import io.chan.queuingsystemforjava.common.error.ErrorCode;
import io.chan.queuingsystemforjava.common.error.IgnoreException;
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
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    ClientHttpResponse response = execution.execute(request, body);
    if (response.getStatusCode().isError()) {
      String errorBody = new String(response.getBody().readAllBytes());
      logger.error(
          "Payment API failed for request {} with status {}: {}",
          request.getURI(),
          response.getStatusCode().value(),
          errorBody);

      ErrorCode errorCode = mapToErrorCode(response.getStatusCode().value(), errorBody);
      switch (errorCode) {
        case INVALID_REQUEST:
          throw new IllegalArgumentException("Invalid payment request: " + errorBody);
        case ALREADY_PROCESSED_PAYMENT:
          throw new IllegalStateException("Payment already processed: " + errorBody);
        case UNAUTHORIZED_KEY:
          throw new IllegalArgumentException("Unauthorized payment key: " + errorBody);
        case PROVIDER_ERROR:
        case FAILED_INTERNAL_SYSTEM_PROCESSING:
          throw new PaymentException(errorCode, "Retryable error: " + errorBody);
        default:
          throw new PaymentException(errorCode, "Payment error: " + errorBody);
      }
    }
    return response;
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
        logger.warn(
            "Unknown error code '{}' in response: status={}, body={}",
            errorCodeStr,
            statusCode,
            errorBody);
        return ErrorCode.PAYMENT_ERROR;
      }
    } catch (Exception e) {
      logger.error(
          "Failed to parse error response: status={}, body={}, error={}",
          statusCode,
          errorBody,
          e.getMessage());
      return ErrorCode.PAYMENT_ERROR;
    }
  }
}
