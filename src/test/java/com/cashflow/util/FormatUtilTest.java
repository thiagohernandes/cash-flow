package com.cashflow.util;

import com.cashflow.type.DatetimePatternType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static com.mongodb.assertions.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class FormatUtilTest {

    @InjectMocks
    private FormatUtil formatUtil;

    @Test
    void shouldFormatDateTimeTest() {
        String formated = formatUtil.dateTimeFormated();

        assertNotNull(formated);
    }

    @Test
    void shouldFormatDateToStringTest() {
        String formated = formatUtil.formatLocalDateToString(LocalDate.now(), DatetimePatternType.DD_MM_YYYY);

        assertNotNull(formated);
    }
}
