package com.srivatsan.spring_batch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerData {
    @Id
    private Long id;
    private String MSISDN;
    private String trnxId;
}

