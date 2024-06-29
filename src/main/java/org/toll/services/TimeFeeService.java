package org.toll.services;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.toll.utils.DateUtils;
import org.toll.utils.TimeRange;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
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
    private static List<DayOfWeek> WEEKEND_DAYS = List.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    private static Optional<TimeRange> getTimeRangeByDate(LocalDateTime date) {
        return timeRangeFeeFnMap.keySet().stream().filter(range -> range.contains(date.toLocalTime())).findFirst();
    }

    private static boolean isWeekend(LocalDateTime date) {
        return WEEKEND_DAYS.contains(date.getDayOfWeek());
    }


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

    public static boolean isDateFreeFee(LocalDateTime date) {
        return isWeekend(date) || HolidayService.isHoliday(date.toLocalDate());
    }
}
