package io.chan.springbatch.section03;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JobParameterRunner implements ApplicationRunner {

    private final JobLauncher jobLauncher;
    private final Job parameterJob;
    @Override
    public void run(ApplicationArguments args) throws Exception {

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("name", "chan")
                    .addLong("seq", 1L)
                    .addDate("date", new java.util.Date())
                    .addLocalDateTime("localDateTime", java.time.LocalDateTime.now())
                    .addDouble("double", 1.1)
                    .toJobParameters();

            jobLauncher.run(parameterJob, jobParameters);
    }
}

