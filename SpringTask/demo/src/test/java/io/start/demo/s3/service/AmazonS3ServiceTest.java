package io.start.demo.s3.service;

import io.start.demo.s3.mock.FakeFileName;
import io.start.demo.s3.mock.TestAmazonS3ClientHolder;
import io.start.demo.s3.mock.FakeAmazonS3Repository;
import io.start.demo.mock.TestUuidHolder;
import io.start.demo.s3.domain.AmazonS3Upload;
import io.start.demo.s3.infrastructure.SystemObjectMetadataHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
class AmazonS3ServiceTest {

    private AmazonS3Service amazonS3Service;

    @BeforeEach
    void init() {

        // given
        String bucket = "nuabo-bucket";
        TestAmazonS3ClientHolder s3ClientHolder = new TestAmazonS3ClientHolder(bucket);
        FakeAmazonS3Repository amazonS3Repository = new FakeAmazonS3Repository();
        TestUuidHolder uuidHolder = new TestUuidHolder("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");

        amazonS3Service = AmazonS3Service.builder()
                .amazonS3ClientHolder(s3ClientHolder)
                .amazonS3Repository(amazonS3Repository)
                .objectMetadataHolder(new SystemObjectMetadataHolder())
                .uuidHolder(uuidHolder)
                .build();

        // case 1

        FakeFileName fileName = new FakeFileName("originalFilename", uuidHolder);
        String storeFilename = fileName.getStoreFilename();
        String originalFilename = fileName.getOriginalFilename();
        String keyName = fileName.getKeyName();
        String imgUrl = s3ClientHolder.getAmazonS3FromAWS(keyName);

        amazonS3Repository.save(
                AmazonS3Upload.builder()
                        .id(1L)
                        .originalFilename(originalFilename)
                        .savedFilename(storeFilename)
                        .imgUrl(imgUrl)
                        .build()
        );

        // case 2

        FakeFileName fileName2 = new FakeFileName("originalFilename2", uuidHolder);
        String storeFilename2 = fileName2.getStoreFilename();
        String originalFilename2 = fileName2.getOriginalFilename();
        String keyName2 = fileName2.getKeyName();
        String imgUrl2 = s3ClientHolder.getAmazonS3FromAWS(keyName2);

        amazonS3Repository.save(
                AmazonS3Upload.builder()
                        .id(2L)
                        .originalFilename(originalFilename2)
                        .savedFilename(storeFilename2)
                        .imgUrl(imgUrl2)
                        .build()
        );
    }
    @Test
    @DisplayName("AmazonS3Service는 AmazonS3Upload 객체를 업로드할 수 있다.")
    void upload() throws IOException {

        // given
        String fileName = "testImg";
        String contentType = "jpg";
        String filePath = "src/test/resources/img/testImg.png";

        MockMultipartFile getMockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        // when
        AmazonS3Upload upload = amazonS3Service.upload(getMockMultipartFile);

        // then
        log.info("upload getSavedFilename= {}", upload.getSavedFilename());
        log.info("upload getOriginalFilename = {}", upload.getOriginalFilename());
        log.info("upload getImgUrl = {}", upload.getImgUrl());

        assertThat(upload.getSavedFilename()).isEqualTo("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa.jpg");
        assertThat(upload.getOriginalFilename()).isEqualTo("testImg.jpg");
        assertThat(upload.getImgUrl()).isEqualTo("https://nuabo-bucket.s3.ap-northeast-2.amazonaws.com/nuabo/aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa.jpg");

    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException, IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}