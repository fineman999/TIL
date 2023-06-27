package io.start.demo.common.domain.exception;

public class SaveAmazonS3Exception extends RuntimeException {
    public SaveAmazonS3Exception(Exception e) {
        super("Amazon S3에 파일을 저장하는데 실패했습니다. AWS가 문제가 있습니다", e);
    }
}
