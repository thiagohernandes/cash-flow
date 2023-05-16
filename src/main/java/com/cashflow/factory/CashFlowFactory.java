package com.cashflow.factory;

import com.cashflow.database.entity.CashFlowEntity;
import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.type.DatetimePatternType;
import com.cashflow.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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
}
