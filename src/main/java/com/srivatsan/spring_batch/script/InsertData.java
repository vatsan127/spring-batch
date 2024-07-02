package com.srivatsan.spring_batch.script;


import com.srivatsan.spring_batch.app.AppConfig;
import com.srivatsan.spring_batch.model.CustomerData;
import com.srivatsan.spring_batch.model.CustomerJpaRepo;
import lombok.AllArgsConstructor;

import java.util.Random;

@AllArgsConstructor
public class InsertData {
    private final Random random = new Random();

    private final CustomerJpaRepo repository;
    private AppConfig appConfig;

    public void insertRandomData() {
        String trnxId;
        String MSISDN;

        int i = 0;
        while (i < appConfig.getSampleDataSize()) {

            trnxId = TransactionId.generateTransactionId();
            long tenDigitNumber = 1000000000L + (long) (random.nextDouble() * 9000000000L);
            MSISDN = Long.toString(tenDigitNumber);

            CustomerData entry = new CustomerData();
            entry.setMSISDN(MSISDN);
            entry.setTrnxId(trnxId);

            repository.save(entry);
            i++;
        }
    }
}
