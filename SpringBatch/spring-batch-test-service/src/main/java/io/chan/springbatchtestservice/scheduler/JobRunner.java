package io.chan.springbatchtestservice.scheduler;

import static org.quartz.JobBuilder.newJob;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class JobRunner implements ApplicationRunner {
  @Value("${scheduler.date}")
  private String date;

  @Override
  public void run(ApplicationArguments args) {
    doRun(args, date);
  }

  protected abstract void doRun(ApplicationArguments args, final String date);

  public Trigger buildJobTrigger(String scheduleExp) {
    return TriggerBuilder.newTrigger()
        .withSchedule(CronScheduleBuilder.cronSchedule(scheduleExp))
        .build();
  }

  public JobDetail buildJobDetail(
      Class<? extends QuartzJobBean> job,
      String name,
      String group,
      Map<? extends String, ?> params) {
    JobDataMap jobDataMap = new JobDataMap();
    jobDataMap.putAll(params);

    return newJob(job).withIdentity(name, group).usingJobData(jobDataMap).build();
  }
}
