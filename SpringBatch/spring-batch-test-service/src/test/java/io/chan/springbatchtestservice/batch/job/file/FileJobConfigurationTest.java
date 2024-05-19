package io.chan.springbatchtestservice.batch.job.file;

import static org.assertj.core.api.Assertions.assertThat;

import io.chan.springbatchtestservice.TestBatchConfig;
import io.chan.springbatchtestservice.setup.AcceptanceTest;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBatchTest
@SpringJUnitConfig(classes = {FileJobConfiguration.class, TestBatchConfig.class}) //
public class FileJobConfigurationTest extends AcceptanceTest{

  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired private JdbcTemplate jdbcTemplate;

  @Test
  public void fileJobTest(@Autowired @Qualifier("fileJob") Job fileJob) throws Exception {
    // given
    jobLauncherTestUtils.setJob(fileJob);
    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("requestDate", "20210101")
            .addLong("date", new Date().getTime())
            .toJobParameters();
    // when
    final JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

    // then
    assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    assertThat(jobExecution.getExitStatus().getExitCode())
        .isEqualTo(BatchStatus.COMPLETED.toString());

    assertThat(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM product", Integer.class))
        .isEqualTo(30);
  }

}
