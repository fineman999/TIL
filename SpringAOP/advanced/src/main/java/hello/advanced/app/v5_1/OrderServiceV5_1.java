package hello.advanced.app.v5_1;

import hello.advanced.trace.callback.TraceTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV5_1 {

    private final OrderRepositoryV5_1 orderRepository;
    private final TraceTemplate template;


    public void orderItem(String itemId) {
        template.execute("OrderService.orderItem()", () -> {
            orderRepository.save(itemId);
            return null;
        });
    }
}
