package io.chan.springbatchtestservice.batch.chunk.processor;

import io.chan.springbatchtestservice.batch.domain.ApiRequestVO;
import io.chan.springbatchtestservice.batch.domain.ProductVO;
import org.springframework.batch.item.ItemProcessor;

public class ApiItemProcessorB implements ItemProcessor<ProductVO, ApiRequestVO> {
    @Override
    public ApiRequestVO process(final ProductVO item) {
        return ApiRequestVO.builder()
                .id(item.id())
                .productVO(item)
                .build();
    }
}
