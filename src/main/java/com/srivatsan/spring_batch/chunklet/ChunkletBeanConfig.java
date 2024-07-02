package com.srivatsan.spring_batch.chunklet;

import com.srivatsan.spring_batch.app.AppConfig;
import com.srivatsan.spring_batch.model.CustomerDataRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ChunkletBeanConfig {
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
