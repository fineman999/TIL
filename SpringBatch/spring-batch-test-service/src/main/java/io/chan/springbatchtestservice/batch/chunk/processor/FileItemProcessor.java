package io.chan.springbatchtestservice.batch.chunk.processor;

import io.chan.springbatchtestservice.batch.domain.Product;
import io.chan.springbatchtestservice.batch.domain.ProductVO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class FileItemProcessor implements ItemProcessor<ProductVO, Product> {
    @Override
    public Product process(@NonNull final ProductVO item) {
        log.info("convert {} to {}", item, Product.class);
        return Product.builder()
//                .id(item.getId()) // auto increment
                .name(item.getName())
                .price(item.getPrice())
                .type(item.getType())
                .build();
    }
}
