package com.srivatsan.spring_batch.config;


import com.srivatsan.spring_batch.chunklet.ChunkletProcessor;
import com.srivatsan.spring_batch.chunklet.ChunkletReader;
import com.srivatsan.spring_batch.chunklet.ChunkletWriter;
import com.srivatsan.spring_batch.model.CustomerDataRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class BeanConfig {

    @Bean
    public CustomerDataRowMapper factTableRowMapper() {
        return new CustomerDataRowMapper();
    }

    @Bean
    public ChunkletReader chunkletReader(DataSource dataSource,
                                         JdbcTemplate jdbcTemplate,
                                         AppConfig appConfig,
                                         CustomerDataRowMapper customerDataRowMapper) {
        return new ChunkletReader(dataSource, jdbcTemplate, appConfig, customerDataRowMapper);
    }

    @Bean
    public ChunkletProcessor chunkletProcessor() {
        return new ChunkletProcessor();
    }

    @Bean
    public ChunkletWriter chunkletWriter() {
        return new ChunkletWriter();
    }

}
