package io.start.demo.s3.service.port;

import io.start.demo.s3.domain.AmazonS3Upload;


public interface AmazonS3Repository {
    AmazonS3Upload save(AmazonS3Upload amazonS3Upload);
}
