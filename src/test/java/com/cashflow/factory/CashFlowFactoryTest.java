package com.cashflow.factory;

import com.cashflow.database.entity.CashFlowEntity;
import com.cashflow.domain.request.CashFlowRequest;
import com.cashflow.domain.response.CashFlowResponse;
import com.cashflow.support.SupportTests;
import com.cashflow.type.DatetimePatternType;
import com.cashflow.util.FormatUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.format.DateTimeFormatter;

import static com.mongodb.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CashFlowFactoryTest extends SupportTests {

    @Mock
    private FormatUtil formatUtil;
    @InjectMocks
    private CashFlowFactory cashFlowFactory;

    @Test
    void shouldMapperDtoToEntityTest() {
        CashFlowRequest request = generator.nextObject(CashFlowRequest.class);
        CashFlowEntity entity = cashFlowFactory.dtoToEntity(request);

        assertNotNull(entity);
        assertEquals(entity.getValue(), request.getValue());
        assertEquals(entity.getDate(), request.getDate());
        assertEquals(entity.getType(), request.getType());
    }

    @Test
    void shouldMapperEntityToResponseTest() {
        CashFlowEntity entity = generator.nextObject(CashFlowEntity.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormated = formatter.format(entity.getDate());

        when(formatUtil.formatLocalDateToString(entity.getDate(),
            DatetimePatternType.DD_MM_YYYY))
            .thenReturn(dateFormated);

        CashFlowResponse response = cashFlowFactory.entityToResponse(entity);

        assertNotNull(response);
        assertEquals(response.getValue(), entity.getValue());
        assertEquals(response.getType(), entity.getType());
        assertEquals(response.getId(), entity.getId());
    }

}
