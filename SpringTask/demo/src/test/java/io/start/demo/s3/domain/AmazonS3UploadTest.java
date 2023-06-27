package io.start.demo.s3.domain;

import io.start.demo.s3.mock.FakeFileName;
import io.start.demo.s3.mock.TestAmazonS3ClientHolder;
import io.start.demo.mock.TestUuidHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class AmazonS3UploadTest {


    @Test
    @DisplayName("AmazonS3Upload는 AmazonS3UploadCreate 객체로 생성할 수 있다.")
    void from() {
        // given
        TestUuidHolder uuidHolder = new TestUuidHolder("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
        FakeFileName fileName = new FakeFileName("originalFilename", uuidHolder);

        String bucket = "nuabo-bucket";

        String storeFilename = fileName.getStoreFilename();
        String originalFilename = fileName.getOriginalFilename();
        String keyName = fileName.getKeyName();
        TestAmazonS3ClientHolder s3ClientHolder = new TestAmazonS3ClientHolder(bucket);
        String imgUrl = s3ClientHolder.getAmazonS3FromAWS(keyName);

        // when
        AmazonS3Upload s3Upload = AmazonS3Upload.from(originalFilename, storeFilename, imgUrl);

        // then
        log.info("s3Upload getSavedFilename: {}", s3Upload.getSavedFilename());
        log.info("s3Upload getImgUrl: {}", s3Upload.getImgUrl());
        log.info("s3Upload getOriginalFilename: {}", s3Upload.getOriginalFilename());
        assertThat(s3Upload.getId()).isNull();
        assertThat(s3Upload.getImgUrl()).isEqualTo(imgUrl);
        assertThat(s3Upload.getSavedFilename()).isEqualTo(storeFilename);
        assertThat(s3Upload.getOriginalFilename()).isEqualTo(originalFilename);
    }
}