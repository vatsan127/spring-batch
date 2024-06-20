package com.srivatsan.spring_batch.config;


import com.srivatsan.spring_batch.listener.FirstJobListener;
import com.srivatsan.spring_batch.service.SecondTasklet;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class TaskletConfig {

    private static final Logger log = LoggerFactory.getLogger(TaskletConfig.class);
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final SecondTasklet secondTasklet;
    private final FirstJobListener firstJobListener;
    public DataSource dataSource;

    /* create the first Task */
    public Tasklet firstTasklet() {
        log.info("firstTasklet");
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is the first tasklet");
                return RepeatStatus.FINISHED;
            }
        };
    }

    /* create the first Step */
    public Step firstStep() {
        return new StepBuilder("FIRST_STEP", jobRepository)
                .tasklet(firstTasklet(), platformTransactionManager)
                .build();
    }

    /* create the second Step */
    public Step secondStep() {
        return new StepBuilder("SECOND_STEP", jobRepository)
                .tasklet(secondTasklet, platformTransactionManager)
                .build();
    }

    /* create JOB with job_name and repo */
    @Bean
    public Job firstJob() {
        return new JobBuilder("FIRST_JOB", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(firstJobListener)
                .start(firstStep())
                .next(secondStep())
                .build();
    }
}
