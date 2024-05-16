package io.chan.springbatchtestservice.batch.chunk.writer;

import io.chan.springbatchtestservice.batch.domain.ApiRequestVO;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class ApiItemWriterA implements ItemWriter<ApiRequestVO> {
    @Override
    public void write(final Chunk<? extends ApiRequestVO> chunk) throws Exception {

    }
}
