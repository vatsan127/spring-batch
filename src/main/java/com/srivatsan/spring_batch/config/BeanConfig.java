package com.srivatsan.spring_batch.config;


import com.srivatsan.spring_batch.model.CustomerDataRowMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CustomerDataRowMapper factTableRowMapper() {
        return new CustomerDataRowMapper();
    }

}
