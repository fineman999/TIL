package practice.logtracer.app.v2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import practice.logtracer.trace.TraceId;
import practice.logtracer.trace.TraceStatus;
import practice.logtracer.trace.practicetrace.PracticeTraceV2;


@Repository
@RequiredArgsConstructor
public class OrderRepositoryV2 {
    private final PracticeTraceV2 trace;
    public void save(String itemId) {
        TraceStatus status = null;
        try{
            status = trace.begin("OrderRepository.save()");
            // 저장 로직
            if(itemId.equals("ex")){
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);
            trace.end(status);
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }

    }

    private void sleep(int millis) {
        try{
            Thread.sleep(millis);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void save(TraceId traceId, String itemId) {
        TraceStatus status = null;
        try{
            status = trace.beginSync(traceId, "OrderRepository.save()");
            // 저장 로직
            if(itemId.equals("ex")){
                throw new IllegalStateException("예외 발생!");
            }
            sleep(1000);
            trace.end(status);
        }catch (Exception e){
            trace.exception(status, e);
            throw e;
        }
    }
}
