package io.start.demo.s3.mock;

import io.start.demo.common.service.port.UuidHolder;
import io.start.demo.s3.controller.AmazonS3Controller;
import io.start.demo.s3.infrastructure.SystemObjectMetadataHolder;
import io.start.demo.s3.service.AmazonS3Service;
import io.start.demo.s3.service.port.AmazonS3Repository;
import lombok.Builder;


public class TestAmazonS3Container {

    public final AmazonS3Repository amazonS3Repository;

    public final AmazonS3Controller amazonS3Controller;

    @Builder
    public TestAmazonS3Container(UuidHolder uuidHolder, String bucket) {
        TestAmazonS3ClientHolder s3ClientHolder = new TestAmazonS3ClientHolder(bucket);
        FakeAmazonS3Repository amazonS3Repository = new FakeAmazonS3Repository();


        this.amazonS3Repository = new FakeAmazonS3Repository();
        AmazonS3Service amazonS3Service = AmazonS3Service.builder()
                .amazonS3ClientHolder(s3ClientHolder)
                .amazonS3Repository(amazonS3Repository)
                .objectMetadataHolder(new SystemObjectMetadataHolder())
                .uuidHolder(uuidHolder)
                .build();

        this.amazonS3Controller = AmazonS3Controller.builder()
                .amazonS3Service(amazonS3Service)
                .build();

    }
}
