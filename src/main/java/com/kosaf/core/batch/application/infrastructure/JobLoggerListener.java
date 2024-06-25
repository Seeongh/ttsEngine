package com.kosaf.core.batch.application.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class JobLoggerListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("{} jpb is Running", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("{} jpb is Done. (Status : {})", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());

        if(jobExecution.getStatus() == BatchStatus.FAILED) {
            log.info("job is failed");
        }
    }

}
