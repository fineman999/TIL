package io.chan.springbatchtestservice.batch.partition;

import io.chan.springbatchtestservice.batch.domain.ProductVO;
import io.chan.springbatchtestservice.batch.job.api.QueryGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ProductPartitioner implements Partitioner {
    private final DataSource dataSource;
    // ExecutionContext란 JobExecution과 StepExecution에 대한 정보를 담고 있는 인터페이스
    @Override
    public Map<String, ExecutionContext> partition(final int gridSize) {
        ProductVO[] productList = QueryGenerator.getProductList(dataSource);
        Map<String, ExecutionContext> result = new HashMap<>();
        int number = 0;
        for (final ProductVO productVO : productList) {
            ExecutionContext value = new ExecutionContext();
            value.put("product", productVO);
            result.put("partition" + number, value);
            number++;
        }
        return result;
    }
}
