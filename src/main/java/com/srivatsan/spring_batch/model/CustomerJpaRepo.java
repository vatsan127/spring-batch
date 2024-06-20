package com.srivatsan.spring_batch.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerJpaRepo extends JpaRepository<CustomerData, Integer> {
}
