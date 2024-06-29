package org.toll.services;

import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDateTime;
import java.util.List;

public class ValidationService {
    public static Pair<Boolean, String> validateDates(List<LocalDateTime> dates) {
        if (dates.isEmpty()) {
            return Pair.of(true, "");
        }
        var firstDate = dates.get(0).toLocalDate();
        var hasDatesInDifferentDays = dates.stream().anyMatch(d -> !d.toLocalDate().isEqual(firstDate));
        if (hasDatesInDifferentDays) {
            return Pair.of(false, "Dates are not in the same day ");
        }
        return Pair.of(true, "");
    }
}
