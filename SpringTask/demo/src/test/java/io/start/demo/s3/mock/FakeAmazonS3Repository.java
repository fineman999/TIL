package io.start.demo.s3.mock;

import io.start.demo.s3.domain.AmazonS3Upload;
import io.start.demo.s3.service.port.AmazonS3Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FakeAmazonS3Repository implements AmazonS3Repository {
    private final AtomicLong autoGeneratedId = new AtomicLong(0);
    private final List<AmazonS3Upload> data = new ArrayList<>();

    @Override
    public AmazonS3Upload save(AmazonS3Upload amazonS3Upload) {
        if (amazonS3Upload.getId() == null || amazonS3Upload.getId() == 0) {
            AmazonS3Upload newAmazonS3Upload = AmazonS3Upload.builder()
                    .id(autoGeneratedId.incrementAndGet())
                    .originalFilename(amazonS3Upload.getOriginalFilename())
                    .savedFilename(amazonS3Upload.getSavedFilename())
                    .imgUrl(amazonS3Upload.getImgUrl())
                    .build();
            data.add(newAmazonS3Upload);
            return newAmazonS3Upload;
         }

        data.removeIf(item -> item.getId().equals(amazonS3Upload.getId()));
        data.add(amazonS3Upload);
        return amazonS3Upload;

    }
}
