package org.toll.services;

import lombok.extern.log4j.Log4j2;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Log4j2
public class HolidayService {
    private static final List<LocalDate> year23Holidays = List.of(
            LocalDate.parse("1-1-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("28-3-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("29-3-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("1-4-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("30-4-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("1-5-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("8-5-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("9-5-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("5-6-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("6-6-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("21-6-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("1-7-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("1-11-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("24-11-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("25-11-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("26-11-2023", DateTimeFormatter.ofPattern("d-M-yyyy")),
            LocalDate.parse("31-11-2023", DateTimeFormatter.ofPattern("d-M-yyyy"))
    );
    private static final Map<Integer, List<LocalDate>> yearToHolidaysMap = Map.of(
            2023, year23Holidays
    );

    public static boolean isHoliday(LocalDate date) {
        var year = date.getYear();
        if (!yearToHolidaysMap.containsKey(year)) {
            log.warn("Holiday not found for year {}, by default not holiday", year);
            return false;
        }
        return yearToHolidaysMap.get(year).contains(date);
    }
}
