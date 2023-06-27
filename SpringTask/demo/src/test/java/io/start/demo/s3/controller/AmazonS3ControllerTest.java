package io.start.demo.s3.controller;

import io.start.demo.common.domain.utils.ApiUtils.ApiResult;
import io.start.demo.common.service.port.UuidHolder;
import io.start.demo.mock.TestUuidHolder;
import io.start.demo.s3.controller.response.AmazonS3Response;
import io.start.demo.s3.domain.AmazonS3Upload;
import io.start.demo.s3.mock.TestAmazonS3Container;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


class AmazonS3ControllerTest {

    @Test
    @DisplayName("AmazonS3ControllerTest - saveUploadFile")
    void saveUploadFile() throws IOException {

        // given
        UuidHolder uuidHolder = new TestUuidHolder("aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa");
        TestAmazonS3Container container = TestAmazonS3Container.builder()
                .bucket("nuabo-bucket")
                .uuidHolder(uuidHolder)
                .build();

        String fileName = "testImg";
        String contentType = "jpg";
        String filePath = "src/test/resources/img/testImg.png";
        MockMultipartFile getMockMultipartFile = getMockMultipartFile(fileName, contentType, filePath);

        container.amazonS3Repository.save(
                AmazonS3Upload.builder()
                        .originalFilename("originalFilename")
                        .savedFilename("savedFilename")
                        .imgUrl("imgUrl")
                        .build()
        );
        ResponseEntity<ApiResult<AmazonS3Response>> result = container.amazonS3Controller.saveUploadFile(getMockMultipartFile);
        assertThat(result.getBody().getResponse().getImgUrl()).isEqualTo("https://nuabo-bucket.s3.ap-northeast-2.amazonaws.com/nuabo/aaaaaaa-aaaa-aaaa-aaaaaaaaaaaa.jpg");
        assertThat(result.getBody().getResponse().getOriginalFilename()).isEqualTo("testImg.jpg");
        assertThat(result.getBody().getResponse().getId()).isEqualTo(1L);

    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException, IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }
}