package io.start.demo.s3.controller.response;

import io.start.demo.s3.domain.AmazonS3Upload;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AmazonS3Response {

    private Long id;
    private String originalFilename;
    private String imgUrl;

    public static AmazonS3Response from(AmazonS3Upload amazonS3Upload) {
        return AmazonS3Response.builder()
                .id(amazonS3Upload.getId())
                .originalFilename(amazonS3Upload.getOriginalFilename())
                .imgUrl(amazonS3Upload.getImgUrl())
                .build();
    }
}
