package io.start.demo.s3.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AmazonS3Upload {

    private final Long id;
    private final String originalFilename;
    private final String savedFilename;
    private final String imgUrl;

    @Builder
    public AmazonS3Upload(Long id, String originalFilename, String savedFilename, String imgUrl) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.savedFilename = savedFilename;
        this.imgUrl = imgUrl;
    }

    public static AmazonS3Upload from(String originalFilename, String savedFilename, String imgUrl) {
       return AmazonS3Upload.builder()
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .imgUrl(imgUrl)
                .build();
    }

}
