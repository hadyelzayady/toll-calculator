package org.toll.utils;

import lombok.Data;

import java.time.LocalTime;
import java.util.Objects;

@Data
public class TimeRange {
    private final LocalTime startTime;
    private final LocalTime endTime;

    public TimeRange(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Start time must be before or equal to end time");
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean contains(LocalTime time) {
        return time.isAfter(startTime.minusMinutes(1)) && time.isBefore(endTime.plusMinutes(1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeRange timeRange = (TimeRange) o;
        return startTime.equals(timeRange.startTime) && endTime.equals(timeRange.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime, endTime);
    }

    @Override
    public String toString() {
        return "TimeRange{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}

