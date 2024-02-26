package io.chan.springbatch.section06.scope;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomJobListener implements JobExecutionListener {
    @Override
    public void beforeJob(final JobExecution jobExecution) {
        jobExecution.getExecutionContext().putString("message", "user1");
    }
}
