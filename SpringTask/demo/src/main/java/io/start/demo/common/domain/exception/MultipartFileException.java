package io.start.demo.common.domain.exception;


public class MultipartFileException  extends RuntimeException {

    public MultipartFileException(String datasource, long id) {
        super(datasource + "에서" + id + "를 찾을 수 없습니다.");
    }

    public MultipartFileException(String datasource, String id) {
        super(datasource + "에서 " + id + "를 찾을 수 없습니다.");
    }
}