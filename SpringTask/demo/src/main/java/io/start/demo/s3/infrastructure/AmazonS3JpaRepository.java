package io.start.demo.s3.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AmazonS3JpaRepository extends JpaRepository<AmazonS3Entity, Long> {

}
