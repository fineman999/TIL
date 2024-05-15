package io.chan.springbatchtestservice.batch.processor;

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
                .id(item.id())
                .name(item.name())
                .price(item.price())
                .type(item.type())
                .build();
    }
}
