package io.chan.appleinapptest.utils;

import io.chan.appleinapptest.model.AppStoreResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.Map;

public class WebClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebClientUtils.class);
    private static final RestClient REST_CLIENT = RestClient.create();

    public static AppStoreResponse doPost(String url, Map<String, String> param) {
        try {
            AppStoreResponse appStoreResponse = REST_CLIENT.post()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(param)
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                            (request, response) -> {
                                logger.error(String.format("[doPost] post url(%s) failed. param:%s", url, param));
                                throw new IllegalArgumentException(String.format("[doPost] post url(%s) failed. param:%s", url, param));
                            })
                    .body(AppStoreResponse.class);

            logger.info(String.format("[doPost] post url(%s) successful. result:%s", url, appStoreResponse));
            return appStoreResponse;
        } catch (Exception e) {
            logger.error(String.format("[doPost] post url(%s) failed. param:%s", url, param), e);
            throw new IllegalArgumentException(String.format("[doPost] post url(%s) failed. param:%s", url, param));
        }
    }
}
