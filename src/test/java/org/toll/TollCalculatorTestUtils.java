package org.toll;

import org.toll.services.HourFeeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TollCalculatorTestUtils {


    public static final List<LocalDate> nonRushHours = List.of(
            LocalDate.parse("1-1-2024 01:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDate.parse("1-1-2024 01:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDate.parse("1-1-2024 02:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDate.parse("1-1-2024 03:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDate.parse("1-1-2024 04:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDate.parse("1-1-2024 05:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDate.parse("1-1-2024 05:59", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDate.parse("1-1-2024 09:15", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDate.parse("1-1-2024 09:31", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm"))
    );

    public static List<Date> getNonRushHours() {
        return nonRushHours.stream().map(s -> Date.from(s.atStartOfDay(ZoneId.systemDefault()).toInstant())).distinct().collect(Collectors.toList());
    }

    public static LocalDateTime getDateWithFeeHour() {
        var feeRange = HourFeeService.timeRangeFeeFnMap.keySet().stream().findFirst().get();
        var feeDate = LocalDate.parse("1-1-2024");
        return feeDate.atTime(feeRange.getStartTime());
    }
}
