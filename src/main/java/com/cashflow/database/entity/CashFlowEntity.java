package com.cashflow.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(collection = "collection_cash_flow")
public class CashFlowEntity {

    @Id
    private String id;
    private LocalDate date;
    private String type;
    private BigDecimal value;
}
