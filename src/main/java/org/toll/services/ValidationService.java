package org.toll.services;

import org.apache.commons.lang3.tuple.Pair;
import org.toll.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class ValidationService {
    public static Pair<Boolean, String> validateDates(List<Date> dates) {
        if (dates.isEmpty()) {
            return Pair.of(true, "");
        }
        var firstDate = DateUtils.convertDateToLocalDateTime(dates.get(0)).toLocalDate();
        var hasDatesInDifferentDays = dates.stream().anyMatch(d -> !DateUtils.convertDateToLocalDateTime(d).toLocalDate().isEqual(firstDate));
        if (hasDatesInDifferentDays) {
            return Pair.of(false, "Dates are not in the same day ");
        }
        return Pair.of(true, "");
    }
}
