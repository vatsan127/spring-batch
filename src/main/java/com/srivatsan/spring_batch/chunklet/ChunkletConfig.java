package com.srivatsan.spring_batch.chunklet;


import com.srivatsan.spring_batch.config.AppConfig;
import com.srivatsan.spring_batch.listener.SpringBatchJobListener;
import com.srivatsan.spring_batch.model.CustomerData;
import com.srivatsan.spring_batch.model.CustomerDataRowMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
@EnableBatchProcessing
public class ChunkletConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;
    private final SpringBatchJobListener springBatchJobListener;

    private final ChunkletReader chunkletReader;
    private final ChunkletProcessor chunkletProcessor;
    private final ChunkletWriter chunkletWriter;

    private final AppConfig appConfig;
    public DataSource dataSource;
    private CustomerDataRowMapper customerDataRowMapper;

    /* For Single Thread */
    public JdbcCursorItemReader<CustomerData> jdbcCursorItemReader() {
        JdbcCursorItemReader<CustomerData> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("select * from customer_data;");
        reader.setRowMapper(customerDataRowMapper);
        return reader;
    }

    /* For Multi Thread */
   /* @Bean
    public JdbcPagingItemReader<CustomerData> jdbcItemReaderPaging() throws Exception {

        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setSelectClause("select * ");
        factoryBean.setFromClause("from customer_data");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put(appConfig.getOrderColumn(), Order.DESCENDING);
        factoryBean.setSortKeys(sortKeys);

        JdbcPagingItemReader<CustomerData> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setQueryProvider(factoryBean.getObject());
        itemReader.setPageSize(appConfig.getChunkSize());
        itemReader.setRowMapper(customerDataRowMapper);
        return itemReader;
    }*/


    private Step readDatabaseStep() throws Exception {
        return new StepBuilder("READ_DB_STEP", jobRepository)
                .<CustomerData, CustomerData>chunk(appConfig.getChunkSize(), platformTransactionManager)
                /*.reader(jdbcItemReaderPaging())*/
                .reader(chunkletReader.read())
                .processor(chunkletProcessor)
                .writer(chunkletWriter)
                .taskExecutor(taskExecutor())
                .build();
    }

    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(appConfig.getThreadSize());
        return asyncTaskExecutor;
    }

    @Bean
    public Job readDatabaseJob() throws Exception {
        return new JobBuilder("READ_DB_JOB", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(springBatchJobListener)
                .start(readDatabaseStep())
                .build();
    }
}
