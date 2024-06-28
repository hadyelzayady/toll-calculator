package org.toll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.toll.entities.Car;
import org.toll.entities.Motorbike;
import org.toll.services.HourFeeService;
import org.toll.utils.DateUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.stream.Collectors;

public class TollCalculatorUnitTests {
    private final TollCalculator tollCalculator = new TollCalculator();

    public TollCalculatorUnitTests() throws ParseException {
    }

    @Test
    void test_car_trip_fee_at_non_rush_hours() throws ParseException {
        Car car = new Car();
        Integer totalFee = tollCalculator.getTollFee(car, TollCalculatorTestUtils.getNonRushHours().toArray(new Date[0]));
        Assertions.assertEquals(totalFee, 0);
    }

    @Test
    void test_car_trip_two_fees_at_same_hour_in_rush_hour() throws ParseException {
        Car car = new Car();
        Integer totalFee = tollCalculator.getTollFee(car,
                LocalDate.parse("1-1-2024 01:00", DateTimeFormatter.ofPattern("d-M-yyyy HH:mm")),
                );
        Assertions.assertEquals(totalFee, 8);
    }

    @Test
    void test_car_trip_in_holidays_in_rush_hour() throws ParseException {
        Car car = new Car();
//        Integer totalFee = tollCalculator.getTollFee(car, feeHour);
//        Assertions.assertEquals(totalFee, 8);
    }

    @Test
    void test_car_trip_in_weekends_in_rush_hour() throws ParseException {
        Car car = new Car();
//        Integer totalFee = tollCalculator.getTollFee(car, formatter.parse("1-1-2024 06:00"));
//        Assertions.assertEquals(totalFee, 8);
    }

    @Test
    void test_car_trip_with_fees_greater_than_max() throws ParseException {
        Car car = new Car();
//        Integer totalFee = tollCalculator.getTollFee(car, formatter.parse());
//        Assertions.assertEquals(totalFee, 8);
    }

    @Test
    void test_fee_free_vehicle() throws ParseException {
//        Motorbike vehicle = new Motorbike();
//        TimeRange feeHour = HourFeeService.timeRangeFeeFnMap.keySet().stream().findFirst().get();
//        Integer totalFee = tollCalculator.getTollFee(vehicle, feeHour.getStartTime())
//        Assertions.assertEquals(totalFee, 0);
    }
}
