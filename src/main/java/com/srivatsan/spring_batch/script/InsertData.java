/*
package com.srivatsan.spring_batch.script;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class InsertData {
    private final Random random = new Random();

    private final FactTableRepo repository;
    private final Logger log = LoggerFactory.getLogger(InsertData.class);
    private AppConfig appConfig;

    public InsertData(FactTableRepo repository, AppConfig appConfig) {
        this.repository = repository;
        this.appConfig = appConfig;
    }

    public void insertData() {
        String trnxId;
        String MSISDN;
        String step;
        String[] steps = {"WAIT", "COMM"};

        log.info("chunk size :: {}", appConfig.getChunkSize());
        log.info("sampleDataSize :: {}", appConfig.getSampleDataSize());

        int i = 0;
        while (i < appConfig.getSampleDataSize()) {

            trnxId = TransactionId.generateTransactionId();
            step = steps[random.nextInt(steps.length)];
            long tenDigitNumber = 1000000000L + (long) (random.nextDouble() * 9000000000L);
            MSISDN = Long.toString(tenDigitNumber);

            FactTable entry = new FactTable();
            entry.setMSISDN(MSISDN);
            entry.setTrnxId(trnxId);
            entry.setStepType(step);
            entry.setMilliSec(System.currentTimeMillis());

            repository.save(entry);
            i++;
        }

    }
}
*/
