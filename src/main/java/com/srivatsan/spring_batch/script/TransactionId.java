package com.srivatsan.spring_batch.script;

import java.security.SecureRandom;

public class TransactionId {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ID_LENGTH = 7;

    public static String generateTransactionId() {
        StringBuilder transactionId = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < ID_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            transactionId.append(randomChar);
        }
        return transactionId.toString();
    }

}
