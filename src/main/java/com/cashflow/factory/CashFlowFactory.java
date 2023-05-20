package com.cashflow.factory;

import com.cashflow.database.entity.CashFlowEntity;
import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.type.DatetimePatternType;
import com.cashflow.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CashFlowFactory {

    private final FormatUtil formatUtil;

    public CashFlowEntity dtoToEntity(final CashFlowRequest cashFlowRequest) {
        return CashFlowEntity.builder()
            .value(cashFlowRequest.getValue())
            .date(cashFlowRequest.getDate())
            .type(cashFlowRequest.getType())
            .build();
    }

    public CashFlowResponse entityToResponse(final CashFlowEntity cashFlowEntity) {
        return CashFlowResponse.builder()
            .value(cashFlowEntity.getValue())
            .date(formatUtil.formatLocalDateToString(cashFlowEntity.getDate(),
                DatetimePatternType.DD_MM_YYYY))
            .type(cashFlowEntity.getType())
            .id(cashFlowEntity.getId())
            .build();
    }

    public List<CashFlowResponse> entityToResponse(final Page<CashFlowEntity> page) {
        return page.getContent().stream()
            .map(mapped -> CashFlowResponse.builder()
                .id(mapped.getId())
                .value(mapped.getValue())
                .date(formatUtil.formatLocalDateToString(mapped.getDate(), DatetimePatternType.DD_MM_YYYY))
                .type(mapped.getType())
                .build())
            .collect(Collectors.toList());
    }
}
