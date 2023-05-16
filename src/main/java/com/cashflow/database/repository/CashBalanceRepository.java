package com.cashflow.database.repository;

import com.cashflow.database.entity.CashBalanceEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashBalanceRepository extends MongoRepository<CashBalanceEntity, String> {
}
