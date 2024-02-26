package io.chan.springbatch.section06.scope;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class CustomStepListener implements StepExecutionListener {
    @Override
    public void beforeStep(final StepExecution stepExecution) {
        stepExecution.getExecutionContext().putString("name", "user2");
    }
}
