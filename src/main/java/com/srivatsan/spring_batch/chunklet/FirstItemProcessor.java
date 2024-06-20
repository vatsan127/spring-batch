package com.srivatsan.spring_batch.chunklet;

import com.srivatsan.spring_batch.model.CustomerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FirstItemProcessor implements ItemProcessor<CustomerData, CustomerData> {

    private static final Logger log = LoggerFactory.getLogger(FirstItemProcessor.class);
    private int batch = 0;

    @Override
    public CustomerData process(CustomerData item) throws Exception {
        log.info("Processing FactTable : item = {}", item);
        return item;
    }
}
