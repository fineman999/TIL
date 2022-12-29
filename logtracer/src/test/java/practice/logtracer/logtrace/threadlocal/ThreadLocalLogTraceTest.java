package practice.logtracer.logtrace.threadlocal;

import org.junit.jupiter.api.Test;
import practice.logtracer.logtrace.FieldLogTrace;
import practice.logtracer.logtrace.ThreadLocalLogTrace;
import practice.logtracer.trace.TraceStatus;

class ThreadLocalLogTraceTest {
    ThreadLocalLogTrace trace = new ThreadLocalLogTrace();

    @Test
    void begin_end_level2(){
        TraceStatus hello2 = trace.begin("hello2");
        TraceStatus hello3 = trace.begin("hello3");
        trace.end(hello3);
        trace.end(hello2);
    }
    @Test
    void begin_exception_level2(){
        TraceStatus hello2 = trace.begin("hello2");
        TraceStatus hello3 = trace.begin("hello3");
        trace.exception(hello3,new IllegalStateException());
        trace.exception(hello2, new IllegalStateException());
    }
}