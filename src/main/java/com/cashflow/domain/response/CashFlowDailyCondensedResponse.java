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
public class CashFlowDailyCondensedResponse {

    private String initialDate;
    private String finalDate;
    private BigDecimal input;
    private BigDecimal output;
    private BigDecimal balance;
}
