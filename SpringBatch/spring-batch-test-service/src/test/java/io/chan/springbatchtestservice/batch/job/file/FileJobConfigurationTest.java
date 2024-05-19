package io.chan.springbatchtestservice.batch.job.file;

import static org.assertj.core.api.Assertions.assertThat;

import io.chan.springbatchtestservice.TestBatchConfig;
import io.chan.springbatchtestservice.setup.AcceptanceTest;
import java.util.Date;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringBatchTest
@SpringJUnitConfig(classes = {FileJobConfiguration.class, TestBatchConfig.class})
public class FileJobConfigurationTest extends AcceptanceTest {

  @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
  @Autowired private JdbcTemplate jdbcTemplate;

  @DisplayName("파일을 읽어서 DB에 저장하는 Job - End-To-End 테스트")
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

  @DisplayName("파일을 읽어서 DB에 저장하는 Job - Step 테스트")
  @Test
  public void fileStepTest(
      @Autowired @Qualifier("fileJob") Job fileJob, @Autowired @Qualifier("fileStep") Step fileStep)
      throws Exception {
    jobLauncherTestUtils.setJob(fileJob);
    // given
    JobParameters jobParameters =
        new JobParametersBuilder()
            .addString("requestDate", "20210101")
            .addLong("date", new Date().getTime())
            .toJobParameters();
    // when
    final JobExecution jobExecution =
        jobLauncherTestUtils.launchStep(fileStep.getName(), jobParameters);

    // then
    assertThat(jobExecution.getExitStatus().getExitCode())
        .isEqualTo(ExitStatus.COMPLETED.getExitCode());

    final StepExecution stepExecution = jobExecution.getStepExecutions().stream().toList().get(0);
    Assertions.assertAll(
        () -> assertThat(stepExecution.getReadCount()).isEqualTo(30),
        () -> assertThat(stepExecution.getWriteCount()).isEqualTo(30),
        () -> assertThat(stepExecution.getFilterCount()).isEqualTo(0),
        () -> assertThat(stepExecution.getCommitCount()).isEqualTo(4));
    // CommitCount가 4인 이유는 3번째 Commit 후 다시 ChunkSize만큼 읽어야 하기 때문에 4번
  }
}
