package io.start.demo.s3.infrastructure;

import io.start.demo.s3.domain.AmazonS3Upload;
import jakarta.persistence.*;

@Entity
@Table(name = "amazon_s3")
public class AmazonS3Entity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalFilename;

    private String savedFilename;

    private String imgUrl;

    public static AmazonS3Entity from(AmazonS3Upload amazonS3Upload) {
        AmazonS3Entity amazonS3Entity = new AmazonS3Entity();
        amazonS3Entity.id = amazonS3Upload.getId();
        amazonS3Entity.originalFilename = amazonS3Upload.getOriginalFilename();
        amazonS3Entity.savedFilename = amazonS3Upload.getSavedFilename();
        amazonS3Entity.imgUrl = amazonS3Upload.getImgUrl();
        return amazonS3Entity;
    }

    public AmazonS3Upload toModel() {
        return AmazonS3Upload.builder()
                .id(id)
                .originalFilename(originalFilename)
                .savedFilename(savedFilename)
                .imgUrl(imgUrl)
                .build();
    }
}
