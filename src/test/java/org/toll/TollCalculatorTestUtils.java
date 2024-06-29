package org.toll;

import lombok.extern.log4j.Log4j2;
import org.toll.services.TimeFeeService;
import org.toll.utils.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class TollCalculatorTestUtils {


    public static final List<LocalDateTime> nonRushHours = List.of(
            LocalDateTime.parse("1-1-2024 01:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDateTime.parse("1-1-2024 01:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDateTime.parse("1-1-2024 02:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDateTime.parse("1-1-2024 03:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDateTime.parse("1-1-2024 04:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDateTime.parse("1-1-2024 05:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDateTime.parse("1-1-2024 05:59", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDateTime.parse("1-1-2024 09:15", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
            LocalDateTime.parse("1-1-2024 09:31", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm"))
    );

    public static List<Date> getNonRushHours() {
        return nonRushHours.stream().map(s -> Date.from(s.toLocalDate().atStartOfDay(ZoneId.of("UTC")).toInstant())).distinct().collect(Collectors.toList());
    }

    public static LocalDateTime getDateWithFeeHour() {
        var feeRange = TimeFeeService.timeRangeFeeFnMap.keySet().stream().findFirst().get();
        var feeDate = LocalDate.parse("1-1-2024");
        return feeDate.atTime(feeRange.getStartTime());
    }

    public static Date getTripDateFromString(String s) {
        return DateUtils.convertLocalDateTimeToDate(LocalDateTime.parse(s, DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")));
    }
}
