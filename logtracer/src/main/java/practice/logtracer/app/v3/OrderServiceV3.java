package practice.logtracer.app.v3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.logtracer.logtrace.LogTrace;
import practice.logtracer.trace.TraceId;
import practice.logtracer.trace.TraceStatus;
import practice.logtracer.trace.practicetrace.PracticeTraceV2;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {
    private final OrderRepositoryV3 orderRepositoryV3;
    private final LogTrace trace;

    public void orderItem(String itemId){
        TraceStatus status = null;
        try{
            status = trace.begin("OrderService.orderItem()");
            orderRepositoryV3.save(itemId);
            trace.end(status);

        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }

    }
}
