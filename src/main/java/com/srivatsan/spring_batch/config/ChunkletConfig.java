package com.srivatsan.spring_batch.config;


import com.srivatsan.spring_batch.chunklet.FirstItemProcessor;
import com.srivatsan.spring_batch.chunklet.FirstItemWriter;
import com.srivatsan.spring_batch.listener.FirstJobListener;
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

    private static final Logger log = LoggerFactory.getLogger(ChunkletConfig.class);
    private final JobRepository jobRepository;
    private final PlatformTransactionManager platformTransactionManager;

    private final FirstItemProcessor firstItemProcessor;
    private final FirstItemWriter firstItemWriter;
    private final FirstJobListener firstJobListener;
    private final AppConfig appConfig;
    public DataSource dataSource;
    private CustomerDataRowMapper customerDataRowMapper;

    /* with multiple threads it will not work , reads row by row */
    public JdbcCursorItemReader<CustomerData> jdbcCursorItemReader() {
        JdbcCursorItemReader<CustomerData> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("select * from fact_table;");
        reader.setRowMapper(customerDataRowMapper);
        return reader;
    }

    /* For Multi Threaded Environment */
    @Bean
    public JdbcPagingItemReader<CustomerData> jdbcItemReaderPaging() throws Exception {
        log.info("chunkSize : {}", appConfig.getChunkSize());
        log.info("orderColumn : {}", appConfig.getOrderColumn());

        SqlPagingQueryProviderFactoryBean factoryBean = new SqlPagingQueryProviderFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setSelectClause("select * ");
        factoryBean.setFromClause("from fact_table");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put(appConfig.getOrderColumn(), Order.DESCENDING);
        factoryBean.setSortKeys(sortKeys);

        JdbcPagingItemReader<CustomerData> itemReader = new JdbcPagingItemReader<>();
        itemReader.setDataSource(dataSource);
        itemReader.setQueryProvider(factoryBean.getObject());
        itemReader.setPageSize(appConfig.getChunkSize());
        itemReader.setRowMapper(customerDataRowMapper);
        return itemReader;
    }


    private Step dbChunkletStep() throws Exception {
        return new StepBuilder("read_db", jobRepository)
                .<CustomerData, CustomerData>chunk(appConfig.getChunkSize(), platformTransactionManager)
                .reader(jdbcItemReaderPaging())
                .processor(firstItemProcessor)
                .writer(firstItemWriter)
                .taskExecutor(taskExecutor())
                .build();
    }

    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(appConfig.getThreadSize());
        return asyncTaskExecutor;
    }

    @Bean
    public Job dbReadChunkletJob() throws Exception {
        return new JobBuilder("DB_READ_JOB", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(firstJobListener)
                .start(dbChunkletStep())
                .build();
    }


}
