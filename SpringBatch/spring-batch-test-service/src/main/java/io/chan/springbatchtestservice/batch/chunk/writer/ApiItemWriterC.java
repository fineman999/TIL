package io.chan.springbatchtestservice.batch.chunk.writer;

import io.chan.springbatchtestservice.batch.domain.ApiRequestVO;
import io.chan.springbatchtestservice.batch.domain.ApiResponseVO;
import io.chan.springbatchtestservice.service.AbstractApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@Slf4j
public class ApiItemWriterC implements ItemWriter<ApiRequestVO> {
  private final AbstractApiService apiService;

  public ApiItemWriterC(final AbstractApiService apiService) {
    this.apiService = apiService;
  }

  @Override
  public void write(final Chunk<? extends ApiRequestVO> chunk) {
    final ApiResponseVO responseVO = apiService.service(chunk.getItems());
    log.info("responseVO: {}", responseVO);
  }
}
