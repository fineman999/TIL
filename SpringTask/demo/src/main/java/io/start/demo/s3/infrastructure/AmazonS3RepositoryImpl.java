package io.start.demo.s3.infrastructure;

import io.start.demo.s3.domain.AmazonS3Upload;
import io.start.demo.s3.service.port.AmazonS3Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AmazonS3RepositoryImpl implements AmazonS3Repository {

    private final AmazonS3JpaRepository amazonS3JpaRepository;
    @Override
    public AmazonS3Upload save(AmazonS3Upload amazonS3Upload) {
        return amazonS3JpaRepository.save(AmazonS3Entity.from(amazonS3Upload)).toModel();
    }

}
