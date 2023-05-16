package com.cashflow.util;

import com.cashflow.type.DatetimePatternType;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Component
public class FormatUtil {

    public String dateTimeFormated() {
        return LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public String formatLocalDateToString(final LocalDate date,
                                          final DatetimePatternType patternType) {
        if (Objects.isNull(date)) {
            return null;
        }

        final DateTimeFormatter formatter = DateTimeFormatter
            .ofPattern(patternType.getPattern());

        return date.format(formatter);
    }
}
