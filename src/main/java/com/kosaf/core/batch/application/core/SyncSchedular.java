package com.kosaf.core.batch.application.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
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


   //@Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
   @Scheduled(cron = "0 * * * * ?") //매분 1초에 실행
    public void perform() throws Exception {
       long page = 1L;
       long pageScale = 10L;

       JobParameters jobParameters = new JobParametersBuilder()
               .addLong("time", System.currentTimeMillis())
               .addLong("page", page)
               .addLong("pageScale", pageScale)
               .toJobParameters();

       jobLauncher.run(syncJob, jobParameters);
    }
}
