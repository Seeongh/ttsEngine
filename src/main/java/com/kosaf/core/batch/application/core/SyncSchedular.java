package com.kosaf.core.batch.application.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SyncSchedular {
    @Autowired
    private Job syncJob;

    @Autowired
    private JobLauncher jobLauncher;

   // @Scheduled(cron = "0 0 0 * * ?")
   @Scheduled(cron = "0 * * * * ?")
    public void perform() throws Exception {
        log.info("Scheduled job running");
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(syncJob, jobParameters);
    }
}
