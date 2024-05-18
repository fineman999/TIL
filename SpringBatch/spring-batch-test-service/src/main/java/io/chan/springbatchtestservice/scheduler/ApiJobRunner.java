package io.chan.springbatchtestservice.scheduler;

import java.util.HashMap;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApiJobRunner extends JobRunner {
  private final Scheduler scheduler;

  @SneakyThrows
  @Override
  protected void doRun(ApplicationArguments args, final String date) {

    JobDetail jobDetail = buildJobDetail(ApiSchedulerJob.class, "apiJob", "batch", new HashMap<>());
    // 30초마다 실행
    Trigger trigger = buildJobTrigger("0/30 * * * * ?");
    if (Objects.isNull(date)) {
      throw new IllegalArgumentException("date is null");
    }
    jobDetail.getJobDataMap().put("requestDate", date);

    scheduler.scheduleJob(jobDetail, trigger);
  }
}
