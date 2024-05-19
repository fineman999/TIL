package io.chan.springbatchtestservice.scheduler;

import java.util.HashMap;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileJobRunner extends JobRunner {
  private final Scheduler scheduler;

  @Override
  protected void doRun(final ApplicationArguments args, final String date) {
    JobDetail jobDetail =
        buildJobDetail(FileSchedulerJob.class, "fileJob", "batch", new HashMap<>());
    // 30초마다 실행
    Trigger trigger = buildJobTrigger("0/50 * * * * ?");
    if (Objects.isNull(date)) {
      throw new IllegalArgumentException("date is null");
    }
    jobDetail.getJobDataMap().put("requestDate", date);

    try {
      scheduler.scheduleJob(jobDetail, trigger);
    } catch (SchedulerException e) {
      e.printStackTrace();
    }
  }
}
