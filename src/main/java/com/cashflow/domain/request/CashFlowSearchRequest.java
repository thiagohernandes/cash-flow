package com.cashflow.domain.request;

import com.cashflow.type.SearchSortType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashFlowSearchRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate initialDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate finalDate;
    private String type;
    private Integer page;
    private Integer size;
    private SearchSortType sort;
}
