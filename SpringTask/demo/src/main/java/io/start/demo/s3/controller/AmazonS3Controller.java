package io.start.demo.s3.controller;

import io.start.demo.common.domain.utils.ApiUtils;
import io.start.demo.common.domain.utils.ApiUtils.ApiResult;
import io.start.demo.s3.controller.response.AmazonS3Response;
import io.start.demo.s3.domain.AmazonS3Upload;
import io.start.demo.s3.service.AmazonS3Service;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Builder
@Tag(name = "AmazonS3")
@RestController
@RequestMapping("/api/v1/amazons3")
@RequiredArgsConstructor
public class AmazonS3Controller {

    private final AmazonS3Service amazonS3Service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResult<AmazonS3Response>> saveUploadFile(@RequestParam MultipartFile multipartFile) {
        AmazonS3Upload upload = amazonS3Service.upload(multipartFile);
        return ResponseEntity.ok().body(ApiUtils.success(AmazonS3Response.from(upload)));
    }

}
