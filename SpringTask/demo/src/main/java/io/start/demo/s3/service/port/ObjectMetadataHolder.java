package io.start.demo.s3.service.port;

import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectMetadataHolder {

    ObjectMetadata initAmazonS3Upload(MultipartFile multipartFile);
}
