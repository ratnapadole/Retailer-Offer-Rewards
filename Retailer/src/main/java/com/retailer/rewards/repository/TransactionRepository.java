package com.retailer.rewards.repository;

import com.retailer.rewards.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
/**
* Repository interface for Transaction entity.
*/
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}