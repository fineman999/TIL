package io.chan.productservice.service.synchronize;

import io.chan.productservice.repository.StockRepository;
import io.chan.productservice.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockBasicService implements StockService {
  private final StockRepository stockRepository;

  @Override
  @Transactional
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

  public synchronized void synchronizedDecrease(Long id, Long quantity) {
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
