package io.chan.springbatch.session12.parallel;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.concurrent.atomic.AtomicLong;

public class CustomTasklet implements Tasklet {
    // tasklet은 자원을 공유하고 있으므로 멀티스레드 환경에서 안전하게 사용할 수 있도록
    // 스레드 안전하게 만들어야 한다.
    private final AtomicLong sum = new AtomicLong(0);
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        for (int i = 0; i < 1000000000; i++) {
            sum.addAndGet(i);
        }
        System.out.printf("%s has been executed on thread %s%n",
                chunkContext.getStepContext().getStepName(), Thread.currentThread().getName());
        System.out.println("Sum: " + sum.get());
        return RepeatStatus.FINISHED;
    }
}
