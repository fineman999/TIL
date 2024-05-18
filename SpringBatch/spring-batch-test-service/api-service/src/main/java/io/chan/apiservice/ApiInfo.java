package io.chan.apiservice;

import java.util.List;

public record ApiInfo(
        String url,
        List<? extends ApiRequestVO> apiRequests
) {}
