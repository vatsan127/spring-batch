package com.srivatsan.spring_batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class SpringBatchJobListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(SpringBatchJobListener.class);

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        log.info("Job execution started  Job name : {} , JobInstance id : {}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Job execution ended  Job name : {} , JobInstance id : {}, TimeTaken : {}ms ",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId(),
                System.currentTimeMillis() - startTime);
    }

}
