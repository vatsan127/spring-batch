package com.srivatsan.spring_batch.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping
public class BatchController {

    private static final Logger log = LoggerFactory.getLogger(BatchController.class);
    private JobLauncher jobLauncher;
    private JobOperator jobOperator;
    private Job firstJob;
    private Job firstChunkJob;

    public BatchController(JobLauncher jobLauncher, JobOperator jobOperator,
                           @Qualifier("firstJob") Job firstJob,
                           @Qualifier("readDatabaseJob") Job firstChunkJob) {
        this.jobLauncher = jobLauncher;
        this.jobOperator = jobOperator;
        this.firstJob = firstJob;
        this.firstChunkJob = firstChunkJob;
    }


    @PostMapping(value = "/start/firstjob",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public void jobController(@RequestBody Map<String, Object> jobRequest) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("Start JobIncoming Request :: {} ", jobRequest.toString());
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("bulkAction", jobRequest.get("bulkAction")
                        .toString())
                .toJobParameters();

        jobLauncher.run(firstJob, jobParameters);

    }

    @PostMapping("/start/secondjob")
    public void jobController() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.info("Start JobIncoming ");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("secondJob", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        log.info("BatchController : jobParameters : {}", jobParameters.toString());
        jobLauncher.run(firstChunkJob, jobParameters);


    }

    @GetMapping("/stop/{executionId}")
    public void jobController(@PathVariable long executionId) throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, NoSuchJobExecutionException, JobExecutionNotRunningException {
        log.info("BatchController : Stop Job, Incoming executionId : {} ", executionId);
        jobOperator.stop(executionId);
    }

    @PostMapping("/insert/customer/data")
    public ResponseEntity insertCustomerData() {
        log.info("BatchController : Insert Customer Data ");
        jobLauncher.run();
    }
}
