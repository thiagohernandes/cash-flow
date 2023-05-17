package com.cashflow.database.repository;

import com.cashflow.database.entity.CashFlowEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CashFlowRepository extends MongoRepository<CashFlowEntity, String> {

    @Query("{$and: [{'date' : { $gte: ?0 } }, {'date' : {$lte: ?1 } }], 'type' : ?2 }")
    List<CashFlowEntity> searchDatesType(LocalDate initialDate,
                                         LocalDate finalDate,
                                         String cashFlowType);
}
