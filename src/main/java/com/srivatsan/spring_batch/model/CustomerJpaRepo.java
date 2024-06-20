package com.srivatsan.spring_batch.model;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Transactional
public interface CustomerJpaRepo extends JpaRepository<CustomerData, Integer> {
}
