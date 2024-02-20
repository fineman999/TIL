package io.chan.springbatch.section04;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.lang.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomJobParametersIncrementer implements JobParametersIncrementer {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-hhmmss");

    @Override
    public JobParameters getNext(@Nullable JobParameters parameters) {
        final String id = dateFormat.format(new Date());
        return new JobParametersBuilder()
                .addString("run.id", id)
                .toJobParameters();
    }
}
