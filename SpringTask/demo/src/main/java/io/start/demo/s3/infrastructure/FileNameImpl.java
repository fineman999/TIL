package io.start.demo.s3.infrastructure;

import io.start.demo.common.service.port.UuidHolder;
import io.start.demo.s3.service.port.FileName;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class FileNameImpl implements FileName {

    private final String originalFilename;
    private final String storeFilename;
    private final String keyName;

    @Builder
    public FileNameImpl(MultipartFile multipartFile, UuidHolder uuidHolder) {
        this.originalFilename = multipartFile.getOriginalFilename();
        int index = getOriginalFilename().lastIndexOf(".");
        String ext = getOriginalFilename().substring(index + 1);
        this.storeFilename = uuidHolder.random() + "." + ext;
        this.keyName = "nuabo/" + getStoreFilename();
    }

}
