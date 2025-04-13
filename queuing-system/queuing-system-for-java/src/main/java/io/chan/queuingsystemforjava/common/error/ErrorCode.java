package io.chan.queuingsystemforjava.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "C400", "올바르지 않은 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "C400-1", "입력값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "C401", "인증되지 않은 사용자 요청입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "C403", "권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "C404", "존재하지 않는 리소스입니다."),
    CONFLICT(HttpStatus.CONFLICT, "C409", "서버 리소스와 충돌이 발생했습니다."),
    VALIDATION_FAILED(HttpStatus.UNPROCESSABLE_ENTITY, "C422", "유효성 검증에 실패하였습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C500", "서버 내부 에러입니다."),
    INVALID_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "C401-1", "인증 정보가 올바르지 않습니다."),
    INVALID_PRINCIPAL(HttpStatus.UNAUTHORIZED, "C401-2", "인증된 사용자가 아닙니다."),

    /*
       Member Error
    */
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "M404-1", "존재하지 않는 회원입니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "M409-1", "이메일이 중복되었습니다."),

    /*
       Access Token Error
    */
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "T401-1", "토큰이 만료되었습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "T401-2", "토큰이 유효하지 않습니다."),
    INVALID_TOKEN_HEADER(HttpStatus.UNAUTHORIZED, "T401-2", "토큰 형식이 올바르지 않습니다."),

    /*
       Performance Error
    */
    NOT_FOUND_PERFORMANCE(HttpStatus.NOT_FOUND, "P404-1", "존재하지 않는 공연입니다."),

    /*
       Zone Error
    */
    NOT_FOUND_ZONE(HttpStatus.NOT_FOUND, "Z404-1", "존재하지 않는 구역입니다."),

    /*
       Seat Grade Error
    */
    NOT_FOUND_SEAT_GRADE(HttpStatus.NOT_FOUND, "SG404-1", "존재하지 않는 구역입니다."),
    NOT_MATCH_AMOUNT(HttpStatus.BAD_REQUEST, "SG400-1", "금액이 일치하지 않습니다."),
    /*
       Seat Error
    */
    NOT_FOUND_SEAT(HttpStatus.NOT_FOUND, "S404-1", "존재하지 않는 좌석입니다."),
    NOT_SELECTABLE_SEAT(HttpStatus.CONFLICT, "S409-1", "이미 선택된 좌석입니다."),
    INVALID_SEAT_STATUS(HttpStatus.CONFLICT, "S409-2", "해당 좌석에는 접근할 수 없습니다."),

    /*
       Payment Error
    */
    PAYMENT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "P500-1", "결제에 실패했습니다."),

    /*
       Waiting Error
    */
    WAITING_WRITE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "W500-1", "대기열 쓰기에 실패했습니다."),
    WAITING_READ_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "W500-2", "대기열 읽기에 실패했습니다."),
    NOT_FOUND_WAITING_MEMBER(HttpStatus.NOT_FOUND, "W404-1", "대기열에 회원이 존재하지 않습니다."),
    NOT_CONTAINS_PERFORMANCE_INFO(HttpStatus.BAD_REQUEST, "W400-1", "공연 정보가 포함되어 있지 않습니다"),

    /*
      Toss Error
     */
    PAYMENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PAYMENT_500", "Unknown payment error"),

    /**
     * Order Error
     */
    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "ORDER_404", "존재하지 않는 주문입니다."),

    // 400 Bad Request
    ALREADY_PROCESSED_PAYMENT(HttpStatus.BAD_REQUEST, "PAYMENT_400_ALREADY_PROCESSED", "This is a payment that has already been processed"),
    PROVIDER_ERROR(HttpStatus.BAD_REQUEST, "PAYMENT_400_PROVIDER", "This is temporary error. Please try again in a few minutes"),
    EXCEED_MAX_CARD_INSTALLMENT_PLAN(HttpStatus.BAD_REQUEST, "PAYMENT_400_EXCEED_INSTALLMENT", "Maximum number of installment months exceeded"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "PAYMENT_400_INVALID", "The bad request"),
    NOT_ALLOWED_POINT_USE(HttpStatus.BAD_REQUEST, "PAYMENT_400_NO_POINT", "Card point payment failed because the card cannot be used points"),
    INVALID_API_KEY(HttpStatus.BAD_REQUEST, "PAYMENT_400_INVALID_API_KEY", "Incorrect secret key"),
    INVALID_REJECT_CARD(HttpStatus.BAD_REQUEST, "PAYMENT_400_REJECT_CARD", "Refer to card issuer/decline"),
    BELOW_MINIMUM_AMOUNT(HttpStatus.BAD_REQUEST, "PAYMENT_400_MIN_AMOUNT", "Payment can be made from 100 won or more by credit card, and 200 won or more for account"),
    INVALID_CARD_EXPIRATION(HttpStatus.BAD_REQUEST, "PAYMENT_400_CARD_EXPIRY", "Please check the card expiration date information again"),
    INVALID_STOPPED_CARD(HttpStatus.BAD_REQUEST, "PAYMENT_400_STOPPED_CARD", "This is a suspended card"),
    EXCEED_MAX_DAILY_PAYMENT_COUNT(HttpStatus.BAD_REQUEST, "PAYMENT_400_MAX_DAILY_COUNT", "You have exceeded the number of daily payments"),
    NOT_SUPPORTED_INSTALLMENT_PLAN_CARD_OR_MERCHANT(HttpStatus.BAD_REQUEST, "PAYMENT_400_NO_INSTALLMENT", "This card or merchant does not support installment"),
    INVALID_CARD_INSTALLMENT_PLAN(HttpStatus.BAD_REQUEST, "PAYMENT_400_INVALID_INSTALLMENT", "The installment month information is incorrect"),
    NOT_SUPPORTED_MONTHLY_INSTALLMENT_PLAN(HttpStatus.BAD_REQUEST, "PAYMENT_400_NO_MONTHLY_INSTALLMENT", "This card does not support installment"),
    EXCEED_MAX_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "PAYMENT_400_MAX_AMOUNT", "You have exceeded the amount you can pay per day"),
    NOT_FOUND_TERMINAL_ID(HttpStatus.BAD_REQUEST, "PAYMENT_400_NO_TERMINAL", "There is no Terminal Id. Please contact Toss Payments"),
    INVALID_AUTHORIZE_AUTH(HttpStatus.BAD_REQUEST, "PAYMENT_400_INVALID_AUTH", "Invalid authentication"),
    INVALID_CARD_LOST_OR_STOLEN(HttpStatus.BAD_REQUEST, "PAYMENT_400_LOST_CARD", "This is a lost or stolen card"),
    RESTRICTED_TRANSFER_ACCOUNT(HttpStatus.BAD_REQUEST, "PAYMENT_400_RESTRICTED_ACCOUNT", "You can withdraw from this bank account after 12 hours from initial register"),
    INVALID_CARD_NUMBER(HttpStatus.BAD_REQUEST, "PAYMENT_400_INVALID_CARD", "Please check your card number again"),
    INVALID_UNREGISTERED_SUBMALL(HttpStatus.BAD_REQUEST, "PAYMENT_400_NO_SUBMALL", "Not registered PG sub-mall business number"),
    NOT_REGISTERED_BUSINESS(HttpStatus.BAD_REQUEST, "PAYMENT_400_NO_BUSINESS", "Unregistered business registration number"),
    EXCEED_MAX_ONE_DAY_WITHDRAW_AMOUNT(HttpStatus.BAD_REQUEST, "PAYMENT_400_MAX_DAY_WITHDRAW", "You have exceeded the one-day withdrawal limit"),
    EXCEED_MAX_ONE_TIME_WITHDRAW_AMOUNT(HttpStatus.BAD_REQUEST, "PAYMENT_400_MAX_ONE_WITHDRAW", "You have exceeded the one-time withdrawal limit"),
    CARD_PROCESSING_ERROR(HttpStatus.BAD_REQUEST, "PAYMENT_400_CARD_PROCESSING", "The card company was not able to process the request"),
    EXCEED_MAX_AMOUNT(HttpStatus.BAD_REQUEST, "PAYMENT_400_MAX_LIMIT", "The transaction amount limit has been exceeded"),
    INVALID_ACCOUNT_INFO_RE_REGISTER(HttpStatus.BAD_REQUEST, "PAYMENT_400_INVALID_ACCOUNT", "Invalid account. Please re-register the account and try again"),
    NOT_AVAILABLE_PAYMENT(HttpStatus.BAD_REQUEST, "PAYMENT_400_NOT_AVAILABLE", "Payment is unavailable at this time"),
    UNAPPROVED_ORDER_ID(HttpStatus.BAD_REQUEST, "PAYMENT_400_UNAPPROVED_ORDER", "This order id has not been approved for payment"),
    EXCEED_MAX_MONTHLY_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "PAYMENT_400_MAX_MONTHLY", "You have exceeded the allowed monthly payment amount of 1,000,000 KRW"),

    // 401 Unauthorized
    UNAUTHORIZED_KEY(HttpStatus.UNAUTHORIZED, "PAYMENT_401_UNAUTHORIZED", "Unauthorized secretKey or clientKey"),

    // 403 Forbidden
    REJECT_ACCOUNT_PAYMENT(HttpStatus.FORBIDDEN, "PAYMENT_403_REJECT_ACCOUNT", "Payment declined due to insufficient balance"),
    REJECT_CARD_PAYMENT(HttpStatus.FORBIDDEN, "PAYMENT_403_REJECT_CARD", "Payment failed due to limit exceeded or insufficient balance"),
    REJECT_CARD_COMPANY(HttpStatus.FORBIDDEN, "PAYMENT_403_REJECT_COMPANY", "Payment confirm is rejected"),
    FORBIDDEN_REQUEST(HttpStatus.FORBIDDEN, "PAYMENT_403_FORBIDDEN", "Not allowed request"),
    REJECT_TOSSPAY_INVALID_ACCOUNT(HttpStatus.FORBIDDEN, "PAYMENT_403_INVALID_TOSSPAY", "Your account invalidated. Please register another account"),
    EXCEED_MAX_AUTH_COUNT(HttpStatus.FORBIDDEN, "PAYMENT_403_MAX_AUTH", "Maximum authentication attempts exceeded"),
    EXCEED_MAX_ONE_DAY_AMOUNT(HttpStatus.FORBIDDEN, "PAYMENT_403_MAX_DAY_AMOUNT", "You have exceeded your daily limit"),
    NOT_AVAILABLE_BANK(HttpStatus.FORBIDDEN, "PAYMENT_403_BANK_HOUR", "It's not banking hour"),
    INVALID_PASSWORD(HttpStatus.FORBIDDEN, "PAYMENT_403_INVALID_PASSWORD", "Incorrect password"),
    INCORRECT_BASIC_AUTH_FORMAT(HttpStatus.FORBIDDEN, "PAYMENT_403_INVALID_AUTH_FORMAT", "Invalid request. Please encode including the ':' character"),
    FDS_ERROR(HttpStatus.FORBIDDEN, "PAYMENT_403_FDS", "A fraudulent transaction has been detected. To complete the payment, complete the authentication process"),

    // 404 Not Found
    NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND, "PAYMENT_404_NOT_FOUND", "Not found payment"),
    NOT_FOUND_PAYMENT_SESSION(HttpStatus.NOT_FOUND, "PAYMENT_404_SESSION_EXPIRED", "Payment session does not exist because the session time has expired"),

    // 500 Internal Server Error
    FAILED_PAYMENT_INTERNAL_SYSTEM_PROCESSING(HttpStatus.INTERNAL_SERVER_ERROR, "PAYMENT_500_FAILED", "Payment has not been completed. please try again"),
    FAILED_INTERNAL_SYSTEM_PROCESSING(HttpStatus.INTERNAL_SERVER_ERROR, "PAYMENT_500_SYSTEM", "Internal system processing operation has failed"),
    UNKNOWN_PAYMENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "PAYMENT_500_UNKNOWN", "Payment failed. If the same problem occurs, please contact your bank or credit card company"),
    INVALID_TICKET_STATUS(HttpStatus.INTERNAL_SERVER_ERROR, "T500-1", "티켓 상태가 올바르지 않습니다.");
    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public int getHttpStatusValue() {
        return httpStatus.value();
    }
}
