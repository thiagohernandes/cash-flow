package com.cashflow.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "collection_cash_balance")
public class CashBalanceEntity {

    @Id
    private String uuid;
    @Field("input")
    private BigDecimal input;
    @Field("output")
    private BigDecimal output;
}
