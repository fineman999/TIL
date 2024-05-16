package io.chan.springbatchtestservice.service;

import io.chan.springbatchtestservice.batch.domain.ApiInfo;
import io.chan.springbatchtestservice.batch.domain.ApiRequestVO;
import io.chan.springbatchtestservice.batch.domain.ApiResponseVO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractApiService {

    public ApiResponseVO service(List<? extends ApiRequestVO> apiRequest){
        final ApiInfo apiInfo = ApiInfo.builder()
                .apiRequests(apiRequest)
                .build();
        return doApiService(apiInfo);
    }

    protected abstract ApiResponseVO doApiService(final ApiInfo apiInfo);
}
