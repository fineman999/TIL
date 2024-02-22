package io.chan.springbatch.section05.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CustomTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(final StepContribution contribution, final ChunkContext chunkContext) throws Exception {
        final String stepName = contribution.getStepExecution().getStepName();
        final String jobName = chunkContext.getStepContext().getJobName();

        System.out.println("Job Name: " + jobName);
        System.out.println("Step Name: " + stepName);

        return RepeatStatus.FINISHED;
    }
}
