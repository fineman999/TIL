package practice.logtracer.app.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import practice.logtracer.trace.TraceId;
import practice.logtracer.trace.TraceStatus;
import practice.logtracer.trace.practicetrace.PracticeTraceV2;

@Service
@RequiredArgsConstructor
public class OrderServiceV2 {
    private final OrderRepositoryV2 orderRepositoryV2;
    private final PracticeTraceV2 trace;

    public void orderItem(TraceId traceId, String itemId){
        TraceStatus status = null;
        try{
            status = trace.beginSync(traceId, "OrderService.orderItem()");
            orderRepositoryV2.save(status.getTraceId(), itemId);
            trace.end(status);

        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }

    }
}
