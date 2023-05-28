package hello.advanced.app.v5_1;

import hello.advanced.trace.callback.TraceTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV5_1 {


    private final OrderServiceV5_1 orderService;

    private final TraceTemplate template;


    @GetMapping("/v5-1/request/{itemId}")
    public String request(@PathVariable String itemId) {
        return template.execute("OrderController.request()", () -> {
            orderService.orderItem(itemId);
            return "ok";
        });
    }
}
