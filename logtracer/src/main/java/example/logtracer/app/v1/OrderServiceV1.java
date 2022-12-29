package example.logtracer.app.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.logtracer.trace.TraceStatus;
import practice.logtracer.trace.practicetrace.PracticeTraceV1;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {
    private final OrderRepositoryV1 orderRepositoryV1;
    private final PracticeTraceV1 trace;

    public void orderItem(String itemId){
        TraceStatus status = null;
        try{
            status = trace.begin("OrderService.orderItem()");
            orderRepositoryV1.save(itemId);
            trace.end(status);

        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }

    }
}
