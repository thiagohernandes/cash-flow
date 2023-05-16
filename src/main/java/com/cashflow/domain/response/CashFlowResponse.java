package com.cashflow.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashFlowResponse {

    private String id;
    private String date;
    private String type;
    private BigDecimal value;
}
