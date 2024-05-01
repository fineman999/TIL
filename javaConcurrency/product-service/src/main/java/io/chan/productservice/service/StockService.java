package io.chan.productservice.service;

import io.chan.productservice.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

@Service
@RequiredArgsConstructor
public class StockService {
  private final StockRepository stockRepository;

  public void decrease(Long id, Long quantity) {
    stockRepository
        .findById(id)
        .map(
            stock -> {
              stock.decrease(quantity);
              return stockRepository.save(stock);
            })
        .orElseThrow(() -> new RuntimeException
                ("Stock not found"));
  }
}
