package com.srivatsan.spring_batch.chunklet;


import com.srivatsan.spring_batch.model.CustomerData;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class FirstItemWriter implements ItemWriter<CustomerData> {

    @Override
    public void write(Chunk<? extends CustomerData> chunk) throws Exception {

    }
}
