package io.start.demo.common.domain.exception;

public class NotValidException extends RuntimeException{

    public NotValidException(String datasource, String value) {
        super(datasource + "에서" + value + "를 찾을 수 없습니다.");
    }

    public NotValidException(String datasource, Long value) {
        super(datasource + "에서 " + value + "를 찾을 수 없습니다.");
    }

    public NotValidException(String message, Throwable cause) {
        super(message, cause);
    }
}
