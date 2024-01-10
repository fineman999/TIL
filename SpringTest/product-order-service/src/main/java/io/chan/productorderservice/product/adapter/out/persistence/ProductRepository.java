package io.chan.productorderservice.product.adapter.out.persistence;

import io.chan.productorderservice.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
