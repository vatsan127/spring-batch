package com.srivatsan.spring_batch.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring-batch")
public class AppConfig {
    private static final Logger log = LoggerFactory.getLogger(AppConfig.class);
    private int chunkSize;
    private Long sampleDataSize;
    private String orderColumn;
    private int threadSize;

    @PostConstruct
    public void init() {
        log.info("Initializing AppConfig");
    }
}
