package io.chan.springbatchtestservice.batch.chunk.writer;

import io.chan.springbatchtestservice.batch.domain.ApiRequestVO;
import io.chan.springbatchtestservice.batch.domain.ApiResponseVO;
import io.chan.springbatchtestservice.service.AbstractApiService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;

@Slf4j
public class ApiItemWriterA extends FlatFileItemWriter<ApiRequestVO> {
  private final AbstractApiService apiService;

  public ApiItemWriterA(final AbstractApiService apiService) {
    this.apiService = apiService;
  }

  @Override
  public void write(final Chunk<? extends ApiRequestVO> chunk) throws Exception {
    final ApiResponseVO responseVO = apiService.service(chunk.getItems());
    log.info("responseVO: {}", responseVO);
    final List<ApiRequestVO> apiRequestVOS =
        chunk.getItems().stream()
            .map(item -> new ApiRequestVO(item.id(), item.productVO(), responseVO))
            .toList();
    super.setResource(new FileSystemResource("src/main/resources/product1.txt"));
    super.setLineAggregator(
        new DelimitedLineAggregator<>()); // DelimitedLineAggregator는 각 항목을 구분자로 구분하여 한 줄로 작성합니다.
    super.open(new ExecutionContext());
    Chunk<ApiRequestVO> newChunk = new Chunk<>(apiRequestVOS);
    super.write(newChunk);
  }
}
