package org.toll.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.toll.utils.TimeRange;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Log4j2
@Data
@AllArgsConstructor
public class TimeFeeService {
    public static final Map<TimeRange, Function<LocalDateTime, Integer>> timeRangeFeeFnMap = Map.of(
            new TimeRange(LocalTime.of(6, 0), LocalTime.of(6, 29)), (s) -> 8,
            new TimeRange(LocalTime.of(6, 30), LocalTime.of(6, 59)), (s) -> 13,
            new TimeRange(LocalTime.of(7, 0), LocalTime.of(7, 59)), (s) -> 18,
            new TimeRange(LocalTime.of(8, 0), LocalTime.of(8, 29)), (s) -> 13,
            new TimeRange(LocalTime.of(8, 30), LocalTime.of(14, 59)), (s) -> {
                if (s.getMinute() >= 30) {
                    return 8;
                }
                return 0;
            },
            new TimeRange(LocalTime.of(15, 0), LocalTime.of(15, 29)), (s) -> 13,
            new TimeRange(LocalTime.of(18, 0), LocalTime.of(18, 29)), (s) -> 8
    );

    public static int getTimeFee(LocalDateTime date) {
        Optional<TimeRange> timeRange = getTimeRangeByDate(date);
        if (timeRange.isPresent()) {
            var fee = timeRangeFeeFnMap.get(timeRange.get()).apply(date);
            log.info("Time range found: {} for date: {} with fee: {}", timeRange.get().toString(), date, fee);
            return fee;
        }
        log.info("No time range found for {}", date);
        return 0;
    }

    private static Optional<TimeRange> getTimeRangeByDate(LocalDateTime date) {
        return timeRangeFeeFnMap.keySet().stream().filter(range -> range.contains(date.toLocalTime())).findFirst();
    }

    public static boolean isDateFreeFee(Date date) {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) return true;

        if (year == 2013) {
            if (month == Calendar.JANUARY && day == 1 ||
                    month == Calendar.MARCH && (day == 28 || day == 29) ||
                    month == Calendar.APRIL && (day == 1 || day == 30) ||
                    month == Calendar.MAY && (day == 1 || day == 8 || day == 9) ||
                    month == Calendar.JUNE && (day == 5 || day == 6 || day == 21) ||
                    month == Calendar.JULY ||
                    month == Calendar.NOVEMBER && day == 1 ||
                    month == Calendar.DECEMBER && (day == 24 || day == 25 || day == 26 || day == 31)) {
                return true;
            }
        }
        return false;
    }
}
