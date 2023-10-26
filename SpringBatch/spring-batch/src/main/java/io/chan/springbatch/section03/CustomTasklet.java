package io.chan.springbatch.section03;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

@Slf4j
public class CustomTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("--------------------");
        log.info("Hello, World! Using Custom Tasklet!");
        log.info("--------------------");
        ExecutionContext jobExecutionContext = contribution.getStepExecution().getJobExecution().getExecutionContext();
        ExecutionContext stepExecutionContext = contribution.getStepExecution().getExecutionContext();

        String jobName = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getJobName();
        String stepName = chunkContext.getStepContext().getStepExecution().getStepName();

        if (!jobExecutionContext.containsKey("jobName")) {
            jobExecutionContext.put("jobName", jobName);
        }

        if (!stepExecutionContext.containsKey("stepName")) {
            stepExecutionContext.put("stepName", stepName);
        }

        log.info("jobName: {}", jobExecutionContext.getString("jobName"));
        log.info("stepName: {}", stepExecutionContext.getString("stepName"));

        return RepeatStatus.FINISHED;
    }
}
