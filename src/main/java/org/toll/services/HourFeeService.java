package org.toll.services;

import lombok.Data;
import org.toll.utils.TimeRange;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Data
public class HourFeeService {
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
            return timeRangeFeeFnMap.get(timeRange.get()).apply(date);
        }
        return 0;
    }

    private static Optional<TimeRange> getTimeRangeByDate(LocalDateTime date) {
        return timeRangeFeeFnMap.keySet().stream().filter(range -> range.contains(date.toLocalTime())).findFirst();
    }
}
