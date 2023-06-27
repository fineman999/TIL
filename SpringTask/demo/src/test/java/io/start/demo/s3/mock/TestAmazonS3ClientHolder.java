package io.start.demo.s3.mock;

import com.amazonaws.services.s3.model.ObjectMetadata;
import io.start.demo.s3.service.port.AmazonS3ClientHolder;
import lombok.RequiredArgsConstructor;

import java.io.InputStream;
@RequiredArgsConstructor
public class TestAmazonS3ClientHolder implements AmazonS3ClientHolder {

    private final String bucket;
    @Override
    public String getAmazonS3FromAWS(String keyName) {
        return "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/"+keyName;
    }

    @Override
    public void saveAmazonS3FromAWS(ObjectMetadata objectMetadata, String key, InputStream inputStream) {

    }
}
