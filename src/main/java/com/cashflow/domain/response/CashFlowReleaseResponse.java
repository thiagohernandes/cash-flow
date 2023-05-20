package com.cashflow.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashFlowReleaseResponse {

    private String type;
    private BigDecimal amount;
    private List<CashFlowResponse> releases;
    private Boolean isLast;
    private Integer totalPages;
}
